package com.algobrewery.tasksilo.model.internal;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
public class ListTasksInternalRequest extends BaseInternalRequest {
    private List<Map.Entry<String, List<String>>> filterAttributes;
    private List<String> baseAttributesToSelect;
    private List<String> extensionsToSelect;
}