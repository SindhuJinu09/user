package com.algobrewery.tasksilo.converter;

import com.algobrewery.tasksilo.model.external.ListTasksResponse;
import com.algobrewery.tasksilo.model.internal.ListTasksInternalResponse;
import com.algobrewery.tasksilo.model.internal.TaskDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ListTasksResponseConverter")
public class ListTasksResponseConverter extends InternalToExternalResponseConverter<ListTasksInternalResponse, ListTasksResponse> {

    @Override
    protected HttpStatus getSuccessHttpStatusCode() {
        return HttpStatus.OK;
    }

    @Override
    protected ListTasksResponse buildResponsePayload(ListTasksInternalResponse internal) {
        List<TaskDTO> tasks = internal.getTasks();

        // The tasks should already be filtered at this point by the TaskDTO.filter method
        // We're just passing them through
        return ListTasksResponse.builder()
                .tasks(tasks)
                .build();
    }
}