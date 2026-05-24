package com.workflow.orchestrator.temporal.lab2;

import com.workflow.orchestrator.temporal.lab1.Greeting;

public class HelloWorkFlowImpl implements Greeting {

    @Override
    public String greetSomeone(String name) {
        return "Hello " +  name  + "!";
    }
}