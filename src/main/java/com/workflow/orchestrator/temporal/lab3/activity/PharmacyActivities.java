package com.workflow.orchestrator.temporal.pharmacy;
// Package define karta hai ki ye class pharmacy module ke andar hai.

import io.temporal.activity.ActivityInterface;
// Temporal annotation jo batata hai ki ye ek Activity interface hai.

import io.temporal.activity.ActivityMethod;
// Temporal annotation jo methods ko Activity ke रूप में mark karta hai.

import io.temporal.workflow.Promise;
// Promise: Temporal ka async abstraction jo non-blocking result return karta hai.

@ActivityInterface
// Batata hai ki ye interface Temporal Activities ke liye use hoga.
public interface PharmacyActivities {

    @ActivityMethod
        // Ye method ek Activity hai jo prescription validate karega.
    Promise<String> validatePrescription(String prescriptionData);

    @ActivityMethod
        // Ye method ek Activity hai jo medicine dispatch karega.
    Promise<String> dispatchMedicine(String orderId);
}
