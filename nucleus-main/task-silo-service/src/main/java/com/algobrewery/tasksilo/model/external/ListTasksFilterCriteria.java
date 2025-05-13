package com.algobrewery.tasksilo.model.external;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ListTasksFilterCriteria {
    @NotEmpty
    @Valid
    private List<ListTasksFilterCriteriaAttribute> attributes;
}
