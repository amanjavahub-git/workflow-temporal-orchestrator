package com.workflow.orchestrator.temporal.lab1;

public class GreetingImpl implements Greeting {

    @Override
    public String greetSomeone(String name) {
        return "Hello " +  name  + "!";
    }
}