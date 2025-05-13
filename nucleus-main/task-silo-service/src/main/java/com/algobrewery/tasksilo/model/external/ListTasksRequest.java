package com.algobrewery.tasksilo.model.external;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ListTasksRequest {
    @NotNull
    @Valid
    private ListTasksRequestContext requestContext;

    @NotNull
    @Valid
    private ListTasksFilterCriteria filterCriteria;

    private ListTasksSelector selector;
}
