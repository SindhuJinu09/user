package com.algobrewery.tasksilo.model.internal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    @NotBlank
    private String taskUuid;

    private String organizationUuid;
    private String title;
    private String description;
    private String authorUuid;
    private String assigneeUuid;
    private TaskAssigneeIDType assigneeUuidType;
    private LocalDateTime createdAt;
    private LocalDateTime dueAt;
    private LocalDateTime updatedAt;
    private TaskUpdateActorType updateActorType;
    private TaskStatus status;
    private String parentTaskUuid;
    private List<String> childTaskUuids;
    private Map<String, Object> extensionsData;

    /**
     * Filters the TaskDTO based on the selected base attributes and extensions.
     *
     * @param baseAttributes List of base attributes to include
     * @param extensions List of extension attributes to include
     * @return A new TaskDTO containing only the selected attributes
     */
    public TaskDTO filter(List<String> baseAttributes, List<String> extensions) {
        TaskDTOBuilder builder = TaskDTO.builder()
                .taskUuid(this.taskUuid); // Always include taskUuid

        // If both selectors are null or empty, return the full object
        if ((baseAttributes == null || baseAttributes.isEmpty()) &&
                (extensions == null || extensions.isEmpty())) {
            return this;
        }

        // Process base attributes if provided
        if (baseAttributes != null && !baseAttributes.isEmpty()) {
            for (String attribute : baseAttributes) {
                switch (attribute.toLowerCase()) {
                    case "title":
                        builder.title(this.title);
                        break;
                    case "description":
                        builder.description(this.description);
                        break;
                    case "author":
                    case "authoruuid":
                    case "author_uuid":
                        builder.authorUuid(this.authorUuid);
                        break;
                    case "assignee":
                    case "assigneeuuid":
                    case "assignee_uuid":
                        builder.assigneeUuid(this.assigneeUuid)
                                .assigneeUuidType(this.assigneeUuidType);
                        break;
                    case "status":
                        builder.status(this.status);
                        break;
                    case "duedate":
                    case "due_at":
                    case "dueat":
                        builder.dueAt(this.dueAt);
                        break;
                    case "organization":
                    case "organization_uuid":
                    case "organizationuuid":
                        builder.organizationUuid(this.organizationUuid);
                        break;
                    case "createdat":
                    case "created_at":
                        builder.createdAt(this.createdAt);
                        break;
                    case "updatedat":
                    case "updated_at":
                        builder.updatedAt(this.updatedAt);
                        break;
                    case "updateactortype":
                    case "update_actor_type":
                        builder.updateActorType(this.updateActorType);
                        break;
                    case "parenttask":
                    case "parent_task_uuid":
                    case "parenttaskuuid":
                        builder.parentTaskUuid(this.parentTaskUuid);
                        break;
                    case "childtasks":
                    case "child_task_uuids":
                    case "childtaskuuids":
                        builder.childTaskUuids(this.childTaskUuids);
                        break;
                }
            }
        } else {
            // If no base attributes specified but extensions are, include all base attributes
            builder.title(this.title)
                    .description(this.description)
                    .authorUuid(this.authorUuid)
                    .assigneeUuid(this.assigneeUuid)
                    .assigneeUuidType(this.assigneeUuidType)
                    .status(this.status)
                    .dueAt(this.dueAt)
                    .organizationUuid(this.organizationUuid)
                    .createdAt(this.createdAt)
                    .updatedAt(this.updatedAt)
                    .updateActorType(this.updateActorType)
                    .parentTaskUuid(this.parentTaskUuid)
                    .childTaskUuids(this.childTaskUuids);
        }

        // Handle extensions
        if (extensions != null && !extensions.isEmpty() && this.extensionsData != null) {
            Map<String, Object> filteredExtensions = this.extensionsData.entrySet().stream()
                    .filter(entry -> extensions.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (!filteredExtensions.isEmpty()) {
                builder.extensionsData(filteredExtensions);
            }
        }

        return builder.build();
    }
}