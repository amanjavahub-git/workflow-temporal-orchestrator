package com.workflow.orchestrator.temporal.lab2;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface HelloWorkFlow {

    @WorkflowMethod
    String greetSomeone(String name);
}