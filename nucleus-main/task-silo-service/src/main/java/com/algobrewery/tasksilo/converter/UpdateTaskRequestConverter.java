package com.algobrewery.tasksilo.converter;

import com.algobrewery.tasksilo.model.external.UpdateTaskRequest;
import com.algobrewery.tasksilo.model.internal.TaskAssigneeIDType;
import com.algobrewery.tasksilo.model.internal.TaskStatus;
import com.algobrewery.tasksilo.model.internal.TaskUpdateActorType;
import com.algobrewery.tasksilo.model.internal.UpdateTaskInternalRequest;
import org.springframework.stereotype.Component;

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
        return UpdateTaskInternalRequest.builder()
                .taskUuid(external.getRequestContext().getTaskUuid())
                .title(external.getTitle())
                .description(external.getDescription())
                .assigneeUuid(external.getAssigneeInfo() != null ? external.getAssigneeInfo().getUuid() : null)
                .assigneeUuidType(external.getAssigneeInfo() != null ?
                        TaskAssigneeIDType.valueOf(external.getAssigneeInfo().getIdType().name()) : null)
                .dueAt(external.getDueAt())
                .updateActorType(external.getUpdateActorType() != null ?
                        TaskUpdateActorType.valueOf(external.getUpdateActorType()) : null)
                .status(TaskStatus.valueOf(external.getStatus()))
                .parentTaskUuid(external.getParentTaskUuid())
                .childTaskUuid(external.getChildTaskUuid())
                .extensionsData(external.getExtensionsData())
                .build();
    }
}