package com.algobrewery.tasksilo.converter;

import com.algobrewery.tasksilo.model.external.UpdateTaskResponse;
import com.algobrewery.tasksilo.model.internal.UpdateTaskInternalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component("UpdateTaskResponseConverter")
public class UpdateTaskResponseConverter extends InternalToExternalResponseConverter<UpdateTaskInternalResponse, UpdateTaskResponse> {

    @Override
    protected HttpStatus getSuccessHttpStatusCode() {
        return HttpStatus.OK;
    }

    @Override
    protected UpdateTaskResponse buildResponsePayload(UpdateTaskInternalResponse internal) {
        return UpdateTaskResponse.builder().build();
    }
}