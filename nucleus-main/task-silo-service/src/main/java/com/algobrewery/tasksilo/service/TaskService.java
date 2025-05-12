package com.algobrewery.tasksilo.service;

import com.algobrewery.tasksilo.model.internal.CreateTaskInternalRequest;
import com.algobrewery.tasksilo.model.internal.CreateTaskInternalResponse;
import com.algobrewery.tasksilo.model.internal.GetTaskInternalRequest;
import com.algobrewery.tasksilo.model.internal.GetTaskInternalResponse;
import com.algobrewery.tasksilo.model.internal.UpdateTaskInternalRequest;
import com.algobrewery.tasksilo.model.internal.UpdateTaskInternalResponse;

import java.util.concurrent.CompletableFuture;

public interface TaskService {
    CompletableFuture<CreateTaskInternalResponse> createTask(CreateTaskInternalRequest request);
    CompletableFuture<GetTaskInternalResponse> getTask(GetTaskInternalRequest request);
    CompletableFuture<UpdateTaskInternalResponse> updateTask(UpdateTaskInternalRequest request);
}