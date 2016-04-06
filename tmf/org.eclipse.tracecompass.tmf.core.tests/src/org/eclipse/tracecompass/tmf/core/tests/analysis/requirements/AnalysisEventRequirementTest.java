/*******************************************************************************
 * Copyright (c) 2016 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.tracecompass.tmf.core.tests.analysis.requirements;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.tmf.core.analysis.requirements.TmfAnalysisEventRequirement;
import org.eclipse.tracecompass.tmf.core.analysis.requirements.TmfAnalysisRequirement;
import org.eclipse.tracecompass.tmf.core.analysis.requirements.TmfAnalysisRequirement.ValuePriorityLevel;
import org.eclipse.tracecompass.tmf.core.event.ITmfEventField;
import org.eclipse.tracecompass.tmf.core.event.ITmfEventType;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTraceWithPreDefinedEvents;
import org.eclipse.tracecompass.tmf.core.trace.TmfTrace;
import org.eclipse.tracecompass.tmf.tests.stubs.trace.TmfTraceStub;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Test the {@link TmfAnalysisEventRequirement} class
 *
 * @author Geneviève Bastien
 */
public class AnalysisEventRequirementTest {

    private static final @NonNull String EVENT1 = "abc";
    private static final @NonNull String EVENT2 = "def";
    private static final @NonNull String EVENT3 = "ghi";

    /* A trace class with pre-defined events */
    private static class TraceWithEvents extends TmfTraceStub implements ITmfTraceWithPreDefinedEvents {

        @Override
        public @NonNull Set<? extends @NonNull ITmfEventType> getContainedEventTypes() {
            return ImmutableSet.of(
                    new ITmfEventType() {

                        @Override
                        public @NonNull String getName() {
                            return EVENT1;
                        }

                        @Override
                        public ITmfEventField getRootField() {
                            return null;
                        }

                        @Override
                        public Collection<String> getFieldNames() {
                            return null;
                        }
                    },
                    new ITmfEventType() {

                        @Override
                        public @NonNull String getName() {
                            return EVENT2;
                        }

                        @Override
                        public ITmfEventField getRootField() {
                            return null;
                        }

                        @Override
                        public Collection<String> getFieldNames() {
                            return null;
                        }
                    });
        }

    }

    private final @NonNull TmfTrace fTrace = new TraceWithEvents();

    /**
     * Test with optional requirements
     */
    @Test
    public void testOptionalRequirements() {
        /* Test optional requirement */
        TmfAnalysisRequirement req = new TmfAnalysisEventRequirement(ImmutableSet.of(EVENT1));
        assertTrue(req.test(fTrace));

        req = new TmfAnalysisEventRequirement(ImmutableSet.of(EVENT1, EVENT3));
        assertTrue(req.test(fTrace));

        /* FIXME: if no optional requirement is fulfilled it should fail */
        req = new TmfAnalysisEventRequirement(ImmutableSet.of(EVENT3));
        assertTrue(req.test(fTrace));
    }

    /**
     * Test with mandatory requirements
     */
    @Test
    public void testMandatoryRequirements() {
        /* Test mandatory requirement */
        TmfAnalysisRequirement req = new TmfAnalysisEventRequirement(ImmutableSet.of(EVENT1), ValuePriorityLevel.MANDATORY);
        assertTrue(req.test(fTrace));

        req = new TmfAnalysisEventRequirement(ImmutableSet.of(EVENT1, EVENT2), ValuePriorityLevel.MANDATORY);
        assertTrue(req.test(fTrace));

        /* Event 3 is not an event of the trace */
        req = new TmfAnalysisEventRequirement(ImmutableSet.of(EVENT1, EVENT2, EVENT3), ValuePriorityLevel.MANDATORY);
        assertFalse(req.test(fTrace));

        req = new TmfAnalysisEventRequirement(ImmutableSet.of(EVENT3), ValuePriorityLevel.MANDATORY);
        assertFalse(req.test(fTrace));
    }

    /**
     * Test event requirements on a trace with no pre-defined events. They
     * should all pass
     */
    @Test
    public void testNoPreDefinedEvents() {
        /* A simple trace with no pre-defined events */
        TmfTrace traceNoEvents = new TmfTraceStub();

        TmfAnalysisRequirement req = new TmfAnalysisEventRequirement(ImmutableSet.of(EVENT1, EVENT3), ValuePriorityLevel.MANDATORY);
        assertTrue(req.test(traceNoEvents));

        req = new TmfAnalysisEventRequirement(ImmutableSet.of(EVENT1, EVENT2), ValuePriorityLevel.OPTIONAL);
        assertTrue(req.test(traceNoEvents));

    }

}
