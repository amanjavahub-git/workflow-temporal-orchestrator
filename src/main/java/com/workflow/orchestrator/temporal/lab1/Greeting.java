package com.workflow.orchestrator.temporal.lab1;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface Greeting {

    @WorkflowMethod
    String greetSomeone(String name);
}