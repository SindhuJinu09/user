package com.algobrewery.tasksilo.converter;

import com.algobrewery.tasksilo.model.entity.Task;
import com.algobrewery.tasksilo.model.internal.TaskAssigneeIDType;
import com.algobrewery.tasksilo.model.internal.TaskDTO;
import com.algobrewery.tasksilo.model.internal.TaskStatus;
import com.algobrewery.tasksilo.model.internal.TaskUpdateActorType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskConverter {

    public TaskDTO doBackward(Task task) {
        if (task == null) {
            return null;
        }

        return TaskDTO.builder()
                .taskUuid(task.getTaskUuid())
                .organizationUuid(task.getOrganizationUuid())
                .title(task.getTitle())
                .description(task.getDescription())
                .authorUuid(task.getAuthorUuid())
                .assigneeUuid(task.getAssigneeUuid())
                .assigneeUuidType(TaskAssigneeIDType.valueOf(task.getAssigneeUuidType()))
                .createdAt(task.getCreatedAt())
                .dueAt(task.getDueAt())
                .updatedAt(task.getUpdatedAt())
                .updateActorType(TaskUpdateActorType.valueOf(task.getUpdateActorType()))
                .status(TaskStatus.valueOf(task.getStatus()))
                .parentTaskUuid(task.getParentTaskUuid())
                .childTaskUuids(List.of(task.getChildTaskUuids()))
                .extensionsData(task.getExtensionsData())
                .build();
    }

    public Task doForward(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }

        return Task.builder()
                .taskUuid(taskDTO.getTaskUuid())
                .organizationUuid(taskDTO.getOrganizationUuid())
                .title(taskDTO.getTitle())
                .description(taskDTO.getDescription())
                .authorUuid(taskDTO.getAuthorUuid())
                .assigneeUuid(taskDTO.getAssigneeUuid())
                .assigneeUuidType(taskDTO.getAssigneeUuidType().name())
                .createdAt(taskDTO.getCreatedAt())
                .dueAt(taskDTO.getDueAt())
                .updatedAt(taskDTO.getUpdatedAt())
                .updateActorType(taskDTO.getUpdateActorType().name())
                .status(taskDTO.getStatus().name())
                .parentTaskUuid(taskDTO.getParentTaskUuid())
                .childTaskUuids(taskDTO.getChildTaskUuids().toArray(new String[0]))
                .extensionsData(taskDTO.getExtensionsData())
                .build();
    }
}