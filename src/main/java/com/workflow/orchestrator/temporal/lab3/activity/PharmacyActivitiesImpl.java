package com.workflow.orchestrator.temporal.lab3.activity;

public class PharmacyActivitiesImpl implements com.workflow.orchestrator.temporal.pharmacy.PharmacyActivities {

    @Override
    public String validatePrescription(String prescriptionData) {
        try {
            // Prescription validation logic
            if (prescriptionData == null || prescriptionData.isEmpty()) {
                throw new IllegalArgumentException("Prescription data missing");
                // Exception handling agar data invalid hai
            }
            System.out.println("Prescription validated for: " + prescriptionData);
            return "Prescription Validated for " + prescriptionData;
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate prescription", e);
            // Wrap exception in RuntimeException
        }
    }

    @Override
    public String dispatchMedicine(String orderId) {
        try {
            // Medicine dispatch logic
            System.out.println("Medicine dispatched for order: " + orderId);
            return "Medicine Dispatched for Order " + orderId;
        } catch (Exception e) {
            throw new RuntimeException("Failed to dispatch medicine", e);
        }
    }
}
