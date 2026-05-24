package com.workflow.orchestrator.temporal.lab3.aksbasedconfig;

import com.workflow.orchestrator.temporal.lab3.SslUtil;
import io.grpc.netty.shaded.io.netty.internal.tcnative.SSLContext;
import io.netty.handler.ssl.SslContext;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class TemporalConfig {

    @Value("${temporal.target}")
    private String target;

    @Value("${temporal.namespace}")
    private String namespace;

    @Value("${temporal.useTls}")
    private boolean useTls;

    @Value("${temporal.clientCert}")
    private String clientCert;

    @Value("${temporal.caCert}")
    private String caCert;

    @Value("${temporal.certPassword}")
    private String certPassword;

    @Bean
    public WorkflowServiceStubs workflowServiceStubs() {
        WorkflowServiceStubsOptions.Builder builder =
                WorkflowServiceStubsOptions.newBuilder().setTarget(target);

        if (useTls) {
            SslContext sslContext = SslUtil.createSslContext(clientCert, caCert, certPassword);
            builder.setSslContext(sslContext); // Authentication via mTLS
        }

        return WorkflowServiceStubs.newServiceStubs(builder.build());
    }

    @Bean
    public WorkflowClient workflowClient(WorkflowServiceStubs service) {
        return WorkflowClient.newInstance(service,
                WorkflowClientOptions.newBuilder().setNamespace(namespace).build());
    }
}
