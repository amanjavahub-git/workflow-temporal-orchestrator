package com.workflow.orchestrator.temporal.lab3.workflow;

// Package pharmacy module ke liye

import io.temporal.activity.ActivityOptions;
// Activity options (timeout, retry, etc.)

import io.temporal.common.RetryOptions;
// Retry configuration

import io.temporal.workflow.Promise;
// Promise abstraction for async results

import io.temporal.workflow.Workflow;
// Workflow runtime utilities

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

import java.time.Duration;


public class PharmacyWorkflowImpl implements PharmacyWorkflow {

    // Activity stub create kiya with timeout + retry
    private final com.workflow.orchestrator.temporal.pharmacy.PharmacyActivities activities = Workflow.newActivityStub(
            com.workflow.orchestrator.temporal.pharmacy.PharmacyActivities.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(10))
                    // Activity timeout
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(3)
                            // Retry max 3 times
                            .setInitialInterval(Duration.ofSeconds(2))
                            // Retry delay
                            .setBackoffCoefficient(2.0)
                            // Exponential backoff
                            .build())
                    .build()
    );

    @Override
    public String processOrder(String prescriptionData) {
        try {
            // Step 1: Call first activity
            String validationResult = String.valueOf(activities.validatePrescription(prescriptionData));

            // Step 2: Call second activity
            String dispatchResult = String.valueOf(activities.dispatchMedicine(prescriptionData));

            // Step 3: Merge results
            return validationResult + " | " + dispatchResult;
        } catch (Exception e) {
            Workflow.getLogger(PharmacyWorkflowImpl.class).error("Workflow failed", e);
            throw Workflow.wrap(e);
        }
    }

}

