package com.algobrewery.tasksilo.converter;

import com.algobrewery.tasksilo.model.external.ListTasksFilterCriteria;
import com.algobrewery.tasksilo.model.external.ListTasksRequest;
import com.algobrewery.tasksilo.model.internal.ListTasksInternalRequest;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("ListTasksRequestConverter")
public class ListTasksRequestConverter extends ExternalToInternalRequestConverter<ListTasksRequest, ListTasksInternalRequest> {

    @Override
    protected ListTasksInternalRequest buildRequestPayload(
            String orgUUID,
            String userUUID,
            String clientUserSessionUUID,
            String traceID,
            String regionID,
            ListTasksRequest external) {

        // Extract filter attributes
        List<Map.Entry<String, List<String>>> filterAttributes = external.getFilterCriteria().getAttributes().stream()
                .map(attr -> new AbstractMap.SimpleEntry<>(attr.getName(), attr.getValues()))
                .collect(Collectors.toList());

        // Extract selector attributes - handle null cases
        List<String> baseAttributesToSelect = null;
        List<String> extensionsToSelect = null;

        if (external.getSelector() != null) {
            baseAttributesToSelect = external.getSelector().getBase_attributes();
            extensionsToSelect = external.getSelector().getExtensions();
        }

        // If null, initialize as empty lists to avoid null pointer exceptions
        if (baseAttributesToSelect == null) {
            baseAttributesToSelect = new ArrayList<>();
        }

        if (extensionsToSelect == null) {
            extensionsToSelect = new ArrayList<>();
        }

        return ListTasksInternalRequest.builder()
                .filterAttributes(filterAttributes)
                .baseAttributesToSelect(baseAttributesToSelect)
                .extensionsToSelect(extensionsToSelect)
                .build();
    }
}