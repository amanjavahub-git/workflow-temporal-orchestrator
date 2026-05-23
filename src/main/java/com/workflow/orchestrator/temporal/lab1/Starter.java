package com.workflow.orchestrator.temporal.lab1;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class Starter {
    public static void main(String[] args) {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        WorkflowClient client = WorkflowClient.newInstance(service);

        // Create a workflow stub
        Greeting workflow = client.newWorkflowStub(
                Greeting.class,
                WorkflowOptions.newBuilder()
                        .setTaskQueue("greeting-tasks")   // same task queue as worker
                        .build());

        // Start workflow execution
        String result = workflow.greetSomeone("Captain");
        System.out.println("Workflow result: " + result);
    }
}
