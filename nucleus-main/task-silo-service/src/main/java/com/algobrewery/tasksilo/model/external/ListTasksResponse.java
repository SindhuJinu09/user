package com.algobrewery.tasksilo.model.external;

import com.algobrewery.tasksilo.model.internal.TaskDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class ListTasksResponse extends BaseResponse {
    private List<TaskDTO> tasks;
}