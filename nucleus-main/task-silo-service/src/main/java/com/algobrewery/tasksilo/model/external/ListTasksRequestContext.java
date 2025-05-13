package com.algobrewery.tasksilo.model.external;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ListTasksRequestContext {
    @NotBlank
    private String organization_uuid;

    @NotBlank
    private String user_uuid;
}