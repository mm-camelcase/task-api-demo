package com.camelcase.taskapi.model.entity;

public enum TaskStatusEnum {
    PENDING,
    IN_PROGRESS,
    COMPLETED;

    public static TaskStatusEnum fromString(String status) {
        if (status == null || status.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        try {
            return TaskStatusEnum.valueOf(status.toUpperCase()); // Convert string to enum (case-insensitive)
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }
}