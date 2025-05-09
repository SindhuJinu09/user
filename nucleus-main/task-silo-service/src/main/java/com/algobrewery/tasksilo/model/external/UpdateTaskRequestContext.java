package com.algobrewery.tasksilo.model.external;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTaskRequestContext {
    @NotBlank
    private String taskUuid;
}
