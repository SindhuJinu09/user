// UpdateTaskRequest.java
package com.algobrewery.tasksilo.model.external;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class UpdateTaskRequest {
    @NotNull
    private UpdateTaskRequestContext requestContext;

    @NotBlank
    private String title;

    private String description;

    private AssigneeInfo assigneeInfo;

    @NotNull
    private LocalDateTime dueAt;

    private String updateActorType;

    @NotNull
    private String status;

    // Made optional by removing @NotNull
    private String parentTaskUuid;

    // Made optional by removing @NotNull
    private String childTaskUuid;

    @NotNull
    private Map<String, Object> extensionsData;
}