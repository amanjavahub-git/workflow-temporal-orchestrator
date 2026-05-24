package com.workflow.orchestrator.temporal.lab3.globalconfig;


// Package: config classes ko alag folder mein rakhna best practice hai

import com.workflow.orchestrator.temporal.lab3.SslUtil;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

@Configuration
// Spring Boot configuration class jo beans provide karegi
public class TemporalConfig {

    @Value("${temporal.target}")
    private String target;
    // Cluster address (local mein 127.0.0.1:7233, cloud mein ingress URL)

    @Value("${temporal.namespace}")
    private String namespace;
    // Namespace (default/dev/prod separation ke liye)

    @Value("${temporal.useTls}")
    private boolean useTls;
    // TLS flag (local mein false, cloud mein true)

    @Value("${temporal.clientCert:}")
    private String clientCert;
    // Client certificate path (cloud mein mounted volume, local mein empty)

    @Value("${temporal.caCert:}")
    private String caCert;
    // CA truststore path (cloud mein mounted volume, local mein empty)

    @Value("${temporal.certPassword:}")
    private String certPassword;
    // Keystore/truststore password (ENV variable se inject karna recommended)

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        // Builder banaya jo Temporal cluster ke target set karega
        WorkflowServiceStubsOptions.Builder builder =
                WorkflowServiceStubsOptions.newBuilder().setTarget(target);

        // Agar TLS enabled hai to SSLContext attach karo
        if (useTls) {

            SSLContext sslContext = SslUtil.createSslContext(clientCert, caCert, certPassword);
            builder.setSslContext(sslContext);
        }

        // Final stubs object return karo jo Temporal cluster ke saath connect karega
        return WorkflowServiceStubs.newServiceStubs(builder.build());
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs service) {
        // WorkflowClient banaya jo namespace ke saath bind hota hai
        return WorkflowClient.newInstance(service,
                WorkflowClientOptions.newBuilder().setNamespace(namespace).build());
    }
}
