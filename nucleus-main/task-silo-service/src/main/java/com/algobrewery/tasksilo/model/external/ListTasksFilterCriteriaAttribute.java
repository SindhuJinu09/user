package com.algobrewery.tasksilo.model.external;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ListTasksFilterCriteriaAttribute {
    @NotBlank
    private String name;

    @NotEmpty
    private List<String> values;
}
