package com.algobrewery.tasksilo.service.impl;

import com.algobrewery.tasksilo.converter.TaskConverter;
import com.algobrewery.tasksilo.exceptions.GatewayTimeoutException;
import com.algobrewery.tasksilo.exceptions.InvalidRequestError;
import com.algobrewery.tasksilo.exceptions.NotFoundException;
import com.algobrewery.tasksilo.exceptions.ServiceException;
import com.algobrewery.tasksilo.gateway.UserServiceGateway;
import com.algobrewery.tasksilo.model.entity.Task;
import com.algobrewery.tasksilo.model.internal.*;
import com.algobrewery.tasksilo.repository.task.TaskRepository;
import com.algobrewery.tasksilo.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static com.algobrewery.tasksilo.repository.task.TaskSpecifications.withOrganizationUuid;
import static com.algobrewery.tasksilo.repository.task.TaskSpecifications.withTaskUuid;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final UserServiceGateway userServiceGateway;
    private final TaskConverter taskConverter;
    private final TaskRepository taskRepository;

    @Override
    public CompletableFuture<CreateTaskInternalResponse> createTask(CreateTaskInternalRequest request) {
        return userServiceGateway.getUser(request.getRequestContext(), request.getTaskDTO().getAssigneeUuid())
                .thenCompose(userId -> saveTask(request.getTaskDTO()))
                .thenCompose(this::buildCreateTaskInternalResponse)
                .exceptionally(this::handleCreateTaskException);
    }

    private CompletableFuture<Task> saveTask(TaskDTO taskDTO) {
        try {
            log.info("saveTask taskDTO:{}", taskDTO);
            return CompletableFuture.completedFuture(taskRepository.save(taskConverter.doForward(taskDTO)));
        } catch (Exception e) {
            log.error("exception in saveTask taskDTO:{}", taskDTO, e);
            return CompletableFuture.failedFuture(new ServiceException(e.getMessage(), e.getCause()));
        }
    }

    private CompletableFuture<CreateTaskInternalResponse> buildCreateTaskInternalResponse(Task task) {
        return CompletableFuture.completedFuture(CreateTaskInternalResponse.builder()
                .taskUuid(task.getTaskUuid())
                .responseResult(ResponseResult.SUCCESS)
                .responseReasonCode(ResponseReasonCode.SUCCESS)
                .build());
    }

    private CreateTaskInternalResponse handleCreateTaskException(Throwable ex) {
        log.error("handleCreateTaskException", ex);
        Throwable cause = ex;
        if (!Objects.isNull(ex) && ex instanceof CompletionException) {
            cause = ex.getCause();
        }
        if (cause instanceof InvalidRequestError) {
            return CreateTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.BAD_REQUEST)
                    .build();
        }
        if (cause instanceof NotFoundException) {
            return CreateTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.BAD_REQUEST)
                    .build();
        }
        if (cause instanceof GatewayTimeoutException) {
            return CreateTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.TIMEOUT)
                    .build();
        }
        if (cause instanceof ServiceException) {
            return CreateTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.INTERNAL_ERROR)
                    .build();
        }
        throw new CompletionException(new ServiceException(cause));
    }

    @Override
    public CompletableFuture<GetTaskInternalResponse> getTask(GetTaskInternalRequest request) {
        return CompletableFuture.completedFuture(
                        Specification.where(withTaskUuid(request.getTaskUuid()))
                                .and(withOrganizationUuid(request.getRequestContext().getAppOrgUuid())))
                .thenCompose(this::getTask)
                .thenCompose(this::buildGetTaskInternalResponse)
                .exceptionally(this::handleGetTaskException);
    }

    private CompletableFuture<Task> getTask(Specification<Task> spec) {
        try {
            return taskRepository.findOne(spec)
                    .map(CompletableFuture::completedFuture)
                    .orElseGet(() -> CompletableFuture.failedFuture(new NotFoundException()));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(new ServiceException(e.getMessage(), e.getCause()));
        }
    }

    private CompletableFuture<GetTaskInternalResponse> buildGetTaskInternalResponse(Task task) {
        return CompletableFuture.completedFuture(GetTaskInternalResponse.builder()
                .taskDTO(taskConverter.doBackward(task))
                .responseResult(ResponseResult.SUCCESS)
                .responseReasonCode(ResponseReasonCode.SUCCESS)
                .build());
    }

    private GetTaskInternalResponse handleGetTaskException(Throwable ex) {
        Throwable cause = ex;
        if (!Objects.isNull(ex) && ex instanceof CompletionException) {
            cause = ex.getCause();
        }
        if (cause instanceof InvalidRequestError) {
            return GetTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.BAD_REQUEST)
                    .build();
        }
        if (cause instanceof NotFoundException) {
            return GetTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.BAD_REQUEST)
                    .build();
        }
        if (cause instanceof GatewayTimeoutException) {
            return GetTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.TIMEOUT)
                    .build();
        }
        if (cause instanceof ServiceException) {
            return GetTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.INTERNAL_ERROR)
                    .build();
        }
        throw new CompletionException(new ServiceException(cause));
    }

    @Override
    public CompletableFuture<UpdateTaskInternalResponse> updateTask(UpdateTaskInternalRequest request) {
        return CompletableFuture.completedFuture(
                        Specification.where(withTaskUuid(request.getTaskUuid()))
                                .and(withOrganizationUuid(request.getRequestContext().getAppOrgUuid())))
                .thenCompose(this::getTask)
                .thenCompose(task -> updateTaskFields(task, request))
                .thenCompose(this::buildUpdateTaskInternalResponse)
                .exceptionally(this::handleUpdateTaskException);
    }

    private CompletableFuture<Task> updateTaskFields(Task task, UpdateTaskInternalRequest request) {
        try {
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            if (request.getAssigneeUuid() != null) {
                task.setAssigneeUuid(request.getAssigneeUuid());
                task.setAssigneeUuidType(request.getAssigneeUuidType().name());
            }
            task.setDueAt(request.getDueAt());
            if (request.getUpdateActorType() != null) {
                task.setUpdateActorType(request.getUpdateActorType().name());
            }
            task.setStatus(request.getStatus().name());
            if (request.getParentTaskUuid() != null) {
                task.setParentTaskUuid(request.getParentTaskUuid());
            }
            if (request.getChildTaskUuid() != null) {
                String[] childTaskUuids = task.getChildTaskUuids();
                String[] newChildTaskUuids = new String[childTaskUuids.length + 1];
                System.arraycopy(childTaskUuids, 0, newChildTaskUuids, 0, childTaskUuids.length);
                newChildTaskUuids[childTaskUuids.length] = request.getChildTaskUuid();
                task.setChildTaskUuids(newChildTaskUuids);
            }
            task.setExtensionsData(request.getExtensionsData());
            task.setUpdatedAt(LocalDateTime.now());

            return CompletableFuture.completedFuture(taskRepository.save(task));
        } catch (Exception e) {
            log.error("Error updating task: {}", task.getTaskUuid(), e);
            return CompletableFuture.failedFuture(new ServiceException(e.getMessage(), e.getCause()));
        }
    }

    private CompletableFuture<UpdateTaskInternalResponse> buildUpdateTaskInternalResponse(Task task) {
        return CompletableFuture.completedFuture(UpdateTaskInternalResponse.builder()
                .responseResult(ResponseResult.SUCCESS)
                .responseReasonCode(ResponseReasonCode.SUCCESS)
                .build());
    }

    private UpdateTaskInternalResponse handleUpdateTaskException(Throwable ex) {
        log.error("handleUpdateTaskException", ex);
        Throwable cause = ex;
        if (!Objects.isNull(ex) && ex instanceof CompletionException) {
            cause = ex.getCause();
        }
        if (cause instanceof InvalidRequestError) {
            return UpdateTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.BAD_REQUEST)
                    .build();
        }
        if (cause instanceof NotFoundException) {
            return UpdateTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.BAD_REQUEST)
                    .build();
        }
        if (cause instanceof GatewayTimeoutException) {
            return UpdateTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.TIMEOUT)
                    .build();
        }
        if (cause instanceof ServiceException) {
            return UpdateTaskInternalResponse.builder()
                    .responseResult(ResponseResult.FAILURE)
                    .responseReasonCode(ResponseReasonCode.INTERNAL_ERROR)
                    .build();
        }
        throw new CompletionException(new ServiceException(cause));
    }
} 