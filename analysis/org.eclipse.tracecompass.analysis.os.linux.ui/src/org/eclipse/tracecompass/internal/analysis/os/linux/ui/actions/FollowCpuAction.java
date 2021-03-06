/*******************************************************************************
 * Copyright (c) 2016 Ericsson
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.internal.analysis.os.linux.ui.actions;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jface.action.Action;
import org.eclipse.tracecompass.analysis.os.linux.core.signals.TmfCpuSelectedSignal;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.views.TmfView;

/**
 * CPU Selection Action
 *
 * @author Matthew Khouzam
 */
public class FollowCpuAction extends Action {

    private final @NonNull TmfView fView;
    private final int fCpu;
    private final @NonNull ITmfTrace fTrace;

    /**
     * Contructor
     *
     * @param view
     *            the view to send a signal
     * @param cpu
     *            the cpu number
     * @param trace
     *            the trace
     */
    public FollowCpuAction(@NonNull TmfView view, int cpu, @NonNull ITmfTrace trace) {
        fView = view;
        fCpu = cpu;
        fTrace = trace;
    }

    @Override
    public String getText() {
        return Messages.CpuSelectionAction_followCpu + ' ' + fCpu;
    }

    @Override
    public void run() {
        fView.broadcast(new TmfCpuSelectedSignal(fView, fCpu, fTrace));
        super.run();
    }

}
