package com.algobrewery.tasksilo.model.external;

import lombok.Data;

import java.util.List;

@Data
public class ListTasksSelector {
    private List<String> base_attributes;
    private List<String> extensions;
}