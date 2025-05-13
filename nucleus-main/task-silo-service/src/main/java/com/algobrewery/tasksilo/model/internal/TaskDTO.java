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
    // TaskDTO.java - Modify the filter method to only include requested fields
    public TaskDTO filter(List<String> baseAttributes, List<String> extensions) {
        // If both selectors are null or empty, return the full object
        if ((baseAttributes == null || baseAttributes.isEmpty()) &&
                (extensions == null || extensions.isEmpty())) {
            return this;
        }

        TaskDTO filteredTask = new TaskDTO();
        // Always include taskUuid
        filteredTask.setTaskUuid(this.taskUuid);

        // Process base attributes if provided
        if (baseAttributes != null && !baseAttributes.isEmpty()) {
            for (String attribute : baseAttributes) {
                switch (attribute.toLowerCase()) {
                    case "title":
                        filteredTask.setTitle(this.title);
                        break;
                    case "description":
                        filteredTask.setDescription(this.description);
                        break;
                    case "author":
                    case "authoruuid":
                    case "author_uuid":
                        filteredTask.setAuthorUuid(this.authorUuid);
                        break;
                    case "assignee":
                    case "assigneeuuid":
                    case "assignee_uuid":
                        filteredTask.setAssigneeUuid(this.assigneeUuid);
                        filteredTask.setAssigneeUuidType(this.assigneeUuidType);
                        break;
                    case "status":
                        filteredTask.setStatus(this.status);
                        break;
                    case "duedate":
                    case "due_at":
                    case "dueat":
                        filteredTask.setDueAt(this.dueAt);
                        break;
                    case "organization":
                    case "organization_uuid":
                    case "organizationuuid":
                        filteredTask.setOrganizationUuid(this.organizationUuid);
                        break;
                    case "createdat":
                    case "created_at":
                        filteredTask.setCreatedAt(this.createdAt);
                        break;
                    case "updatedat":
                    case "updated_at":
                        filteredTask.setUpdatedAt(this.updatedAt);
                        break;
                    case "updateactortype":
                    case "update_actor_type":
                        filteredTask.setUpdateActorType(this.updateActorType);
                        break;
                    case "parenttask":
                    case "parent_task_uuid":
                    case "parenttaskuuid":
                        filteredTask.setParentTaskUuid(this.parentTaskUuid);
                        break;
                    case "childtasks":
                    case "child_task_uuids":
                    case "childtaskuuids":
                        filteredTask.setChildTaskUuids(this.childTaskUuids);
                        break;
                }
            }
        } else {
            // If no base attributes specified but extensions are, include all base attributes
            filteredTask.setTitle(this.title);
            filteredTask.setDescription(this.description);
            filteredTask.setAuthorUuid(this.authorUuid);
            filteredTask.setAssigneeUuid(this.assigneeUuid);
            filteredTask.setAssigneeUuidType(this.assigneeUuidType);
            filteredTask.setStatus(this.status);
            filteredTask.setDueAt(this.dueAt);
            filteredTask.setOrganizationUuid(this.organizationUuid);
            filteredTask.setCreatedAt(this.createdAt);
            filteredTask.setUpdatedAt(this.updatedAt);
            filteredTask.setUpdateActorType(this.updateActorType);
            filteredTask.setParentTaskUuid(this.parentTaskUuid);
            filteredTask.setChildTaskUuids(this.childTaskUuids);
        }

        // Handle extensions
        if (extensions != null && !extensions.isEmpty() && this.extensionsData != null) {
            Map<String, Object> filteredExtensions = new HashMap<>();
            for (String extension : extensions) {
                if (this.extensionsData.containsKey(extension)) {
                    filteredExtensions.put(extension, this.extensionsData.get(extension));
                }
            }

            if (!filteredExtensions.isEmpty()) {
                filteredTask.setExtensionsData(filteredExtensions);
            }
        }

        return filteredTask;
    }}