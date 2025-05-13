package com.algobrewery.tasksilo.model.internal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class ListTasksInternalResponse extends BaseInternalResponse {
    private List<TaskDTO> tasks;
}