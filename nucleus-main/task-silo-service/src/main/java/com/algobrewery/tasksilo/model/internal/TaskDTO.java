package com.algobrewery.tasksilo.model.internal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    public TaskDTO filter(List<String> baseAttributes, List<String> extensions) {
        TaskDTOBuilder builder = TaskDTO.builder()
                .taskUuid(this.taskUuid); // Always include taskUuid

        if (baseAttributes != null) {
            for (String attribute : baseAttributes) {
                switch (attribute.toLowerCase()) {
                    case "title":
                        builder.title(this.title);
                        break;
                    case "description":
                        builder.description(this.description);
                        break;
                    case "author":
                        builder.authorUuid(this.authorUuid);
                        break;
                    case "assignee":
                        builder.assigneeUuid(this.assigneeUuid)
                                .assigneeUuidType(this.assigneeUuidType);
                        break;
                    case "status":
                        builder.status(this.status);
                        break;
                    case "duedate":
                        builder.dueAt(this.dueAt);
                        break;
                    case "organization":
                        builder.organizationUuid(this.organizationUuid);
                        break;
                    case "createdat":
                        builder.createdAt(this.createdAt);
                        break;
                    case "updatedat":
                        builder.updatedAt(this.updatedAt);
                        break;
                    case "updateactortype":
                        builder.updateActorType(this.updateActorType);
                        break;
                    case "parenttask":
                        builder.parentTaskUuid(this.parentTaskUuid);
                        break;
                    case "childtasks":
                        builder.childTaskUuids(this.childTaskUuids);
                        break;
                }
            }
        }

        // Handle extensions
        if (extensions != null && !extensions.isEmpty()) {
            Map<String, Object> filteredExtensions = this.extensionsData.entrySet().stream()
                    .filter(entry -> extensions.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            builder.extensionsData(filteredExtensions);
        } else {
            builder.extensionsData(Map.of());
        }

        return builder.build();
    }
}