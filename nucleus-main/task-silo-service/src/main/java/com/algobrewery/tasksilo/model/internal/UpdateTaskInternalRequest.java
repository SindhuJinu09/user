package com.algobrewery.tasksilo.model.internal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class UpdateTaskInternalRequest extends BaseInternalRequest {
    @NotBlank
    private String taskUuid;

    @NotBlank
    private String title;

    private String description;

    private String assigneeUuid;

    private TaskAssigneeIDType assigneeUuidType;

    @NotNull
    private LocalDateTime dueAt;

    private TaskUpdateActorType updateActorType;

    @NotNull
    private TaskStatus status;

    private String parentTaskUuid;

    private String childTaskUuid;

    @NotNull
    private Map<String, Object> extensionsData;
}