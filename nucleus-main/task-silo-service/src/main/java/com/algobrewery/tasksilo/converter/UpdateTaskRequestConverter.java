// UpdateTaskRequestConverter.java
package com.algobrewery.tasksilo.converter;

import com.algobrewery.tasksilo.model.external.UpdateTaskRequest;
import com.algobrewery.tasksilo.model.internal.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component("UpdateTaskRequestConverter")
public class UpdateTaskRequestConverter extends ExternalToInternalRequestConverter<UpdateTaskRequest, UpdateTaskInternalRequest> {

    @Override
    public UpdateTaskInternalRequest buildRequestPayload(
            String orgUUID,
            String userUUID,
            String clientUserSessionUUID,
            String traceID,
            String regionID,
            UpdateTaskRequest external) {
        LocalDateTime current = LocalDateTime.now();
        return UpdateTaskInternalRequest.builder()
                .taskDTO(TaskDTO.builder()
                        .taskUuid(external.getRequestContext().getTaskUuid())
                        .organizationUuid(orgUUID)
                        .title(external.getTitle())
                        .description(external.getDescription())
                        .authorUuid(userUUID)
                        .assigneeUuid(external.getAssigneeInfo() != null ? external.getAssigneeInfo().getUuid() : null)
                        .assigneeUuidType(external.getAssigneeInfo() != null ?
                                TaskAssigneeIDType.valueOf(external.getAssigneeInfo().getIdType().name()) : null)
                        .dueAt(external.getDueAt())
                        .updatedAt(current)
                        .updateActorType(TaskUpdateActorType.valueOf(external.getUpdateActorType()))
                        .status(TaskStatus.valueOf(external.getStatus()))
                        .parentTaskUuid(external.getParentTaskUuid()) // Can be null
                        .childTaskUuids(new ArrayList<>()) // Initialize empty list
                        .extensionsData(external.getExtensionsData())
                        .build())
                .build();
    }
}