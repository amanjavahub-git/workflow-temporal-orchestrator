package com.workflow.orchestrator.temporal.lab3.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface PharmacyWorkflow {
    @WorkflowMethod
    String processOrder(String prescriptionData);
}
