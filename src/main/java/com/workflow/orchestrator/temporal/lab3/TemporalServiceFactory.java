package com.workflow.orchestrator.temporal.lab3;

import io.temporal.serviceclient.WorkflowServiceStubs;          // Temporal SDK ka entry point for gRPC stubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions;   // Options for configuring stubs (target, TLS, etc.)
import io.temporal.client.WorkflowClient;                       // Client for starting/querying workflows
import io.temporal.client.WorkflowClientOptions;                // Options for WorkflowClient (namespace, etc.)
import io.temporal.worker.WorkerFactory;                        // Factory to create workers
import io.temporal.worker.Worker;                               // Worker that executes workflows

import javax.net.ssl.SSLContext;                                // For secure TLS connections
import java.util.Optional;                                      // Utility for handling null values safely

import static com.workflow.orchestrator.temporal.lab3.SslUtil.createSslContext;

public class TemporalServiceFactory {

    // Default values agar ENV variables set na ho
    private static final String DEFAULT_TARGET = "127.0.0.1:7233";   // Local Temporal server address
    private static final String DEFAULT_NAMESPACE = "default";       // Default namespace

    // Method to create WorkflowServiceStubs (gateway to Temporal cluster)
    public static WorkflowServiceStubs createServiceStubs() {
        // Cluster address ENV se read karo, agar missing ho to local default use karo
        String target = getEnv("TEMPORAL_TARGET", DEFAULT_TARGET);

        // TLS flag ENV se read karo, agar missing ho to false (local dev)
        boolean useTls = Boolean.parseBoolean(getEnv("TEMPORAL_USE_TLS", "false"));

        // Builder banaya jo Temporal cluster ke target address set karega
        WorkflowServiceStubsOptions.Builder builder =
                WorkflowServiceStubsOptions.newBuilder().setTarget(target);

        // Agar TLS required hai (cloud mein usually hota hai)
        if (useTls) {
            // Custom SSLContext banaya using client certs + CA cert
            SSLContext sslContext = SslUtil.createSslContext(
                    getEnv("TEMPORAL_CLIENT_CERT", "certs/client.p12"), // Client certificate path
                    getEnv("TEMPORAL_CLIENT_KEY", "certs/client.key"), // Client key path
                    getEnv("TEMPORAL_CA_CERT", "certs/ca.jks")         // CA truststore path
            );
            builder.setSslContext(sslContext); // Builder mein SSL context attach karo
        }

        // Final stubs object return karo jo Temporal cluster ke saath connect karega
        return WorkflowServiceStubs.newServiceStubs(builder.build());
    }

    // Method to create WorkflowClient (used to start/query workflows)
    public static WorkflowClient createClient(WorkflowServiceStubs service) {
        // Namespace ENV se read karo, agar missing ho to default use karo
        String namespace = getEnv("TEMPORAL_NAMESPACE", DEFAULT_NAMESPACE);

        // WorkflowClient banaya jo workflows ko execute karne ke liye use hoga
        return WorkflowClient.newInstance(service,
                WorkflowClientOptions.newBuilder().setNamespace(namespace).build());
    }

    // Method to create WorkerFactory (used to spawn workers)
    public static WorkerFactory createWorkerFactory(WorkflowClient client) {
        return WorkerFactory.newInstance(client); // WorkerFactory return karo
    }

    // Helper method to create Worker and register workflow implementations
    public static Worker createWorker(WorkerFactory factory, String taskQueue,
                                      Class<?>... workflowImpls) {
        Worker worker = factory.newWorker(taskQueue); // Worker bind to specific task queue
        worker.registerWorkflowImplementationTypes(workflowImpls); // Register workflow classes
        return worker; // Worker return karo
    }

    // Utility method to safely read ENV variables with default fallback
    private static String getEnv(String key, String defaultValue) {
        return Optional.ofNullable(System.getenv(key)).orElse(defaultValue);
    }
}
