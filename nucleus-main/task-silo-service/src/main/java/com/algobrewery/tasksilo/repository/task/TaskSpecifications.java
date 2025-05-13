package com.algobrewery.tasksilo.repository.task;

import com.algobrewery.tasksilo.model.entity.Task;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class TaskSpecifications {

    public static Specification<Task> withOrganizationUuid(String organizationUuid) {
        return (root, query, cb) -> cb.equal(root.get("organizationUuid"), organizationUuid);
    }

    public static Specification<Task> withTaskUuid(String taskUuid) {
        return (root, query, cb) -> cb.equal(root.get("taskUuid"), taskUuid);
    }

    public static Specification<Task> withAuthorUuid(String authorUuid) {
        return (root, query, cb) -> cb.equal(root.get("authorUuid"), authorUuid);
    }

    public static Specification<Task> withDueAtBefore(LocalDateTime dueAt) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dueAt"), dueAt);
    }

    public static Specification<Task> withDueAtAfter(LocalDateTime dueAt) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dueAt"), dueAt);
    }

    /**
     * Filter tasks by status
     */
    public static Specification<Task> byStatusIn(List<String> statuses) {
        return (root, query, cb) -> {
            if (statuses == null || statuses.isEmpty()) {
                return null;
            }
            return root.get("status").in(statuses);
        };
    }

    /**
     * Filter tasks by parent task UUID
     */
    public static Specification<Task> byParentTaskUuidIn(List<String> parentTaskUuids) {
        return (root, query, cb) -> {
            if (parentTaskUuids == null || parentTaskUuids.isEmpty()) {
                return null;
            }
            return root.get("parentTaskUuid").in(parentTaskUuids);
        };
    }

    /**
     * Filter tasks by assignee UUID
     */
    public static Specification<Task> byAssigneeUuidIn(List<String> assigneeUuids) {
        return (root, query, cb) -> {
            if (assigneeUuids == null || assigneeUuids.isEmpty()) {
                return null;
            }
            return root.get("assigneeUuid").in(assigneeUuids);
        };
    }

    /**
     * Filter tasks by author UUID
     */
    public static Specification<Task> byAuthorUuidIn(List<String> authorUuids) {
        return (root, query, cb) -> {
            if (authorUuids == null || authorUuids.isEmpty()) {
                return null;
            }
            return root.get("authorUuid").in(authorUuids);
        };
    }

    /**
     * Filter tasks by due date range
     */
    public static Specification<Task> byDueDateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) -> {
            if (start == null && end == null) {
                return null;
            } else if (start == null) {
                return cb.lessThanOrEqualTo(root.get("dueAt"), end);
            } else if (end == null) {
                return cb.greaterThanOrEqualTo(root.get("dueAt"), start);
            } else {
                return cb.between(root.get("dueAt"), start, end);
            }
        };
    }

    /**
     * Filter tasks created after a specific date
     */
    public static Specification<Task> byCreatedAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("createdAt"), date);
        };
    }

    /**
     * Filter tasks updated after a specific date
     */
    public static Specification<Task> byUpdatedAfter(LocalDateTime date) {
        return (root, query, cb) -> {
            if (date == null) {
                return null;
            }
            return cb.greaterThanOrEqualTo(root.get("updatedAt"), date);
        };
    }
}