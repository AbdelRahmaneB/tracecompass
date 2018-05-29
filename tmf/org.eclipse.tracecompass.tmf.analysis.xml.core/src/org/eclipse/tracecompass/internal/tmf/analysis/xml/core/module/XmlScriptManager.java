package org.eclipse.tracecompass.internal.tmf.analysis.xml.core.module;

import java.util.Collections;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.internal.tmf.analysis.xml.core.Activator;
import org.eclipse.tracecompass.internal.tmf.analysis.xml.core.fsm.model.values.DataDrivenValue;
import org.eclipse.tracecompass.internal.tmf.analysis.xml.core.fsm.model.values.DataDrivenValueScript;
import org.eclipse.tracecompass.tmf.analysis.xml.core.module.TmfXmlStrings;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlScriptManager {

    /** the default script engine */
    public static final String DEFAULT_SCRIPT_ENGINE = "nashorn"; //$NON-NLS-1$
    private final Element fSourceElement;
    private final String fScriptEngineName;
    private final @Nullable ScriptEngine fScriptEngine;

    public XmlScriptManager(Element scriptBlockNode) {
        fSourceElement = scriptBlockNode;
        if(fSourceElement == null){
            fScriptEngineName = DEFAULT_SCRIPT_ENGINE;
            fScriptEngine = new ScriptEngineManager().getEngineByName(fScriptEngineName);

        }else{
            String scriptEngineName = scriptBlockNode.getAttribute(TmfXmlStrings.SCRIPT_ENGINE);
            fScriptEngineName = !scriptEngineName.isEmpty() ? scriptEngineName : DEFAULT_SCRIPT_ENGINE ;
            fScriptEngine = new ScriptEngineManager().getEngineByName(fScriptEngineName);
            String blockScript = XmlUtils.getCDataFromElement(fSourceElement);
            executeScript(blockScript, fScriptEngineName);
        }
    }

    public Object executeScript(String script, String scriptEngineName){
        return executeScript(script, scriptEngineName, Collections.emptyMap());
    }

    public Object executeScript(String script, String scriptEngineName,  Map<String, Object> values){
        if(!fScriptEngineName.equals(scriptEngineName)){
            Activator.logError("The provided script cannot be executed using the script provider : "+ scriptEngineName); //$NON-NLS-1$
            return null;
        }
        return executeScript(script, values);
    }

    private Object executeScript(String script, Map<String, Object> values) {
        if (fScriptEngine == null) {
            Activator.logError("Unknown script engine: " + fScriptEngineName); //$NON-NLS-1$
            return null;
        }
        Object result = null;
        values.forEach((id, value) -> fScriptEngine.put(id, value));

        try {
            result = fScriptEngine.eval(script);
        } catch (ScriptException e) {
            Activator.logError("Script execution failed", e); //$NON-NLS-1$
        }
        return result;
    }
}
