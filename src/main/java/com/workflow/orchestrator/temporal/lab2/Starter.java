package com.workflow.orchestrator.temporal.lab2;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class Starter {
    public static void main(String[] args) throws Exception {
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();

        WorkflowClient client = WorkflowClient.newInstance(service);

        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setWorkflowId("my-first-workflow")
                .setTaskQueue("greeting-tasks")
                .build();

        HelloWorkFlow workflow = client.newWorkflowStub(HelloWorkFlow.class, options);

        String greeting = workflow.greetSomeone("greet-1");

        String workflowId = WorkflowStub.fromTyped(workflow).getExecution().getWorkflowId();

        System.out.println(workflowId + "-" + greeting);
    }
}
