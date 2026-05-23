package com.workflow.orchestrator.temporal.lab1;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class GreetingWorker {
    public static void main(String[] args) {
        /**
         1)  WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
                     Ye Line ek Workflow service stub banati hai jo Temporal server ke saath connect hota hai.
                     newLocalServiceStubs() default 127.0.0.1:7233 pe connect karta hai.
                     Matlab: “Mujhe local Temporal server se baat karni hai.”
         2)  WorkflowClient client = WorkflowClient.newInstance(service);
                    Ye ek client object banata hai jo workflows ko start karne aur unse interact karne ke liye use hota hai.
                    Is client ke through aap workflow stub create karte ho.
         3)  WorkerFactory factory = WorkerFactory.newInstance(client);
                    Ye ek Worker factory object banata hai jo workers create karne ke liye use hota hai.
                    Is factory ke through aap workers ko start karte ho.
                    Ye ek worker factory banata hai jo multiple workers manage kar sakta hai.
                    Factory ke through aap ek ya zyada workers bana sakte ho.
         4) Worker worker = factory.newWorker("greeting-tasks");
                    Queue banaey k liye.
                    Ye ek worker banata hai jo "greeting-tasks"(queue) task queue ko poll karega.
                    Worker continuously poll karta hai aur tasks execute karta hai.
                    Matlab: “Mujhe un tasks ko handle karna hai jo 'greeting-tasks' queue mein hain.”
         5)  worker.registerWorkflowImplementationTypes(GreetingImpl.class);
                    Ye line batati hai ki worker kaunse workflow implementation handle karega.
                    Yahan aapne GreetingImpl register kiya hai jo Greeting workflow interface ko implement karta hai.
                    Matlab: “Agar koi workflow Greeting type ka task bheje, to is implementation se execute karo.”
         6)  factory.start();
                    Ye line worker factory ko start kar deti hai.
                    Iske baad worker background mein continuously task queue poll karta hai.
                    Jab koi workflow execution aata hai, worker usko run karta hai.

         Overall Flow :
         - Service Stub → Temporal server se connection banata hai.
         - Client → workflows ko start/communicate karne ke liye.
         - Worker Factory → workers banane ke liye.
         - Worker → ek specific task queue poll karta hai.
         - Register Workflow → batata hai ki worker kaunse workflow implementation run karega.
         - Start Factory → worker ko background mein run kar deta hai.
         **/
    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    WorkflowClient client = WorkflowClient.newInstance(service);
    WorkerFactory factory = WorkerFactory.newInstance(client);

    // Specify the name of the Task Queue that this Worker should poll
    Worker worker = factory.newWorker("greeting-tasks");

    // Specify which Workflow implementations this Worker will support
    worker.registerWorkflowImplementationTypes(GreetingImpl.class);

    // Begin running the Worker
    factory.start();
}
}