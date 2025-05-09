package com.algobrewery.tasksilo.service;

import com.algobrewery.tasksilo.model.internal.*;

import java.util.concurrent.CompletableFuture;

public interface TaskService {
    CompletableFuture<CreateTaskInternalResponse> createTask(CreateTaskInternalRequest request);
    CompletableFuture<GetTaskInternalResponse> getTask(GetTaskInternalRequest request);
    CompletableFuture<UpdateTaskInternalResponse> updateTask(UpdateTaskInternalRequest request);

}
