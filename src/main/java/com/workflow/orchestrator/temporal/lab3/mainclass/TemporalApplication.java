package com.workflow.orchestrator.temporal.lab3.mainclass;

import com.workflow.orchestrator.temporal.lab3.TemporalServiceFactory;
import com.workflow.orchestrator.temporal.lab3.activity.PharmacyActivitiesImpl;
import com.workflow.orchestrator.temporal.lab3.workflow.PharmacyWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class TemporalApplication {
    public static void main(String[] args) {
        // Service stubs banaye (local/cloud config ke basis par)
        WorkflowServiceStubs service = TemporalServiceFactory.createServiceStubs();

        // Workflow client banaye
        WorkflowClient client = TemporalServiceFactory.createClient(service);

        // Worker factory banaye
        WorkerFactory factory = TemporalServiceFactory.createWorkerFactory(client);

        // Worker register kare Pharmacy workflow ke liye
        Worker worker = TemporalServiceFactory.createWorker(factory, "PHARMACY_TASK_QUEUE", PharmacyWorkflowImpl.class);

        // Activity implementation register kare
        worker.registerActivitiesImplementations(new PharmacyActivitiesImpl());

        // Factory start kare
        factory.start();
    }
}
