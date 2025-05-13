package com.algobrewery.tasksilo.converter;

import com.algobrewery.tasksilo.model.external.ListTasksResponse;
import com.algobrewery.tasksilo.model.internal.ListTasksInternalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component("ListTasksResponseConverter")
public class ListTasksResponseConverter extends InternalToExternalResponseConverter<ListTasksInternalResponse, ListTasksResponse> {

    @Override
    protected HttpStatus getSuccessHttpStatusCode() {
        return HttpStatus.OK;
    }

    @Override
    protected ListTasksResponse buildResponsePayload(ListTasksInternalResponse internal) {
        return ListTasksResponse.builder()
                .tasks(internal.getTasks())
                .build();
    }
}