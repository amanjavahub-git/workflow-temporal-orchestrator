package com.workflow.orchestrator.temporal.lab3;

// Package declaration: project ke namespace ko define karta hai.

import com.workflow.orchestrator.temporal.lab3.workflow.PharmacyWorkflow;
import com.workflow.orchestrator.temporal.lab3.workflow.PharmacyWorkflowImpl;
import io.temporal.serviceclient.WorkflowServiceStubs;
import org.apache.kafka.clients.consumer.ConsumerRecord;
// Kafka se consume hone wale ek record ko represent karta hai (key + value).

import org.springframework.kafka.annotation.KafkaListener;
// Spring annotation jo Kafka topic par listener banata hai.

import org.springframework.stereotype.Service;
// Service annotation: ye class ek Spring service ke रूप में register hoti hai.

import io.temporal.client.WorkflowClient;
// Temporal client jo workflows ko start/execute karne ke liye use hota hai.

import io.temporal.client.WorkflowOptions;
// WorkflowOptions: workflow execution ke liye task queue aur configs set karta hai.

import io.temporal.worker.WorkerFactory;
// WorkerFactory: Temporal workers banane ke liye use hota hai.

@Service
// Spring service banaya jo application context mein inject ho sakta hai.
public class GlobalKafkaConsumer {

    private final WorkflowClient workflowClient;
    // Temporal WorkflowClient object jo workflows ko start karne ke liye use hoga.

    public GlobalKafkaConsumer() {
        // TemporalServiceFactory se stubs create kiye (local/cloud config ke basis par).
        WorkflowServiceStubs service = TemporalServiceFactory.createServiceStubs();

        // WorkflowClient banaya jo namespace ke saath bind hota hai.
        this.workflowClient = TemporalServiceFactory.createClient(service);

        // WorkerFactory banaya jo workflows ko register karega.
        WorkerFactory factory = TemporalServiceFactory.createWorkerFactory(workflowClient);

        // Pharmacy workflow ke liye ek worker register kiya (task queue bind).
        TemporalServiceFactory.createWorker(factory, "PHARMACY_TASK_QUEUE", PharmacyWorkflowImpl.class);

       /* // Example: Inventory workflow ke liye ek worker register kiya.
        TemporalServiceFactory.createWorker(factory, "INVENTORY_TASK_QUEUE", InventoryWorkflowImpl.class);

        // Example: Billing workflow ke liye ek worker register kiya.
        TemporalServiceFactory.createWorker(factory, "BILLING_TASK_QUEUE", BillingWorkflowImpl.class);*/

        // Factory start kiya taaki workers workflows execute kar saken.
        factory.start();
    }

    @KafkaListener(topics = {"pharmacy-orders", "inventory-updates", "billing-events"}, groupId = "global-consumer")
    // Kafka listener jo multiple topics listen karega (pharmacy, inventory, billing).
    public void consume(ConsumerRecord<String, String> record) {
        // Kafka record ka topic aur value read kiya.
        String topic = record.topic();
        String message = record.value();

        // Switch-case ke basis par alag workflows trigger karenge.
        switch (topic) {
            case "pharmacy-orders":
                // Pharmacy workflow trigger karna
                WorkflowOptions pharmacyOptions = WorkflowOptions.newBuilder()
                        .setTaskQueue("PHARMACY_TASK_QUEUE")
                        .build();

                PharmacyWorkflow pharmacyWorkflow =
                        workflowClient.newWorkflowStub(PharmacyWorkflow.class, pharmacyOptions);

                WorkflowClient.start(pharmacyWorkflow::processOrder, message);
                break;

            case "inventory-updates":
                // Inventory workflow trigger karna
                WorkflowOptions inventoryOptions = WorkflowOptions.newBuilder()
                        .setTaskQueue("INVENTORY_TASK_QUEUE")
                        .build();

                /*InventoryWorkflow inventoryWorkflow =
                        workflowClient.newWorkflowStub(InventoryWorkflow.class, inventoryOptions);

                WorkflowClient.start(inventoryWorkflow::updateStock, message); */
                break;

            case "billing-events":
                // Billing workflow trigger karna
                WorkflowOptions billingOptions = WorkflowOptions.newBuilder()
                        .setTaskQueue("BILLING_TASK_QUEUE")
                        .build();

                /*BillingWorkflow billingWorkflow =
                        workflowClient.newWorkflowStub(BillingWorkflow.class, billingOptions);

                WorkflowClient.start(billingWorkflow::processPayment, message);*/
                break;

            default:
                // Agar koi unknown topic aaya to log kar dena.
                System.out.println("Unknown topic received: " + topic);
        }
    }
}
