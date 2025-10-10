package com.example.examplefeature;

import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * Service class that handles business logic related to {@link Task} entities.
 * <p>
 * This service provides methods for creating and retrieving tasks from the
 * persistence layer through the {@link TaskRepository}.
 * </p>
 * <p>
 * All public methods are transactional, ensuring proper database consistency.
 * </p>
 */
@Service
public class TaskService {

    /**
     * The repository used to access and persist {@link Task} entities.
     */
    private final TaskRepository taskRepository;

    /**
     * Constructs a new {@code TaskService} with the specified {@link TaskRepository}.
     *
     * @param taskRepository the repository used to manage {@link Task} entities; must not be {@code null}
     */
    TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Creates and persists a new {@link Task}.
     * <p>
     * The creation date is automatically set to the current system time.
     * The due date may be {@code null} if no deadline is specified.
     * </p>
     * <p>
     * This method runs within a transactional context and immediately flushes the entity
     * to the database after persisting.
     * </p>
     *
     * @param description the description of the task; must not be {@code null} or empty
     * @param dueDate     the optional due date of the task; may be {@code null}
     * @throws RuntimeException if the description equals {@code "fail"} (used for testing error handling)
     */
    @Transactional
    public void createTask(String description, @Nullable LocalDate dueDate) {
        if ("fail".equals(description)) {
            throw new RuntimeException("This is for testing the error handler");
        }
        var task = new Task(description, Instant.now());
        task.setDueDate(dueDate);
        taskRepository.saveAndFlush(task);
    }

    /**
     * Retrieves a paginated list of {@link Task} entities.
     * <p>
     * This method executes in a read-only transactional context to ensure
     * that no data modifications occur during retrieval.
     * </p>
     *
     * @param pageable the pagination and sorting information; must not be {@code null}
     * @return a list of tasks corresponding to the given page request
     */
    @Transactional(readOnly = true)
    public List<Task> list(Pageable pageable) {
        return taskRepository.findAllBy(pageable).toList();
    }

}
