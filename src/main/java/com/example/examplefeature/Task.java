package com.example.examplefeature;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Represents a task entity stored in the database.
 * <p>
 * Each {@code Task} has a unique identifier, a textual description,
 * a creation date, and an optional due date.
 * </p>
 * <p>
 * This class is annotated with JPA annotations for ORM mapping
 * and includes validation logic to ensure the description length
 * does not exceed {@link #DESCRIPTION_MAX_LENGTH}.
 * </p>
 */
@Entity
@Table(name = "task")
public class Task {

    /**
     * The maximum allowed length of the task description.
     */
    public static final int DESCRIPTION_MAX_LENGTH = 300;

    /**
     * The unique identifier of the task.
     * <p>
     * Automatically generated using a database sequence.
     * </p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "task_id")
    private Long id;

    /**
     * The textual description of the task.
     * <p>
     * Cannot be {@code null} and must not exceed {@link #DESCRIPTION_MAX_LENGTH} characters.
     * </p>
     */
    @Column(name = "description", nullable = false, length = DESCRIPTION_MAX_LENGTH)
    private String description = "";

    /**
     * The timestamp of when the task was created.
     * <p>
     * Cannot be {@code null}.
     * </p>
     */
    @Column(name = "creation_date", nullable = false)
    private Instant creationDate;

    /**
     * The optional due date of the task.
     * <p>
     * Can be {@code null} if no due date is assigned.
     * </p>
     */
    @Column(name = "due_date")
    @Nullable
    private LocalDate dueDate;

    /**
     * Default constructor required by JPA.
     * <p>
     * Protected to prevent direct instantiation outside JPA.
     * </p>
     */
    protected Task() {
        // Required by Hibernate
    }

    /**
     * Creates a new {@code Task} with the specified description and creation date.
     *
     * @param description  the description of the task; must not exceed {@link #DESCRIPTION_MAX_LENGTH} characters
     * @param creationDate the timestamp when the task was created; must not be {@code null}
     * @throws IllegalArgumentException if the description length exceeds {@link #DESCRIPTION_MAX_LENGTH}
     */
    public Task(String description, Instant creationDate) {
        setDescription(description);
        this.creationDate = creationDate;
    }

    /**
     * Returns the unique identifier of this task.
     *
     * @return the task ID, or {@code null} if not yet persisted
     */
    public @Nullable Long getId() {
        return id;
    }

    /**
     * Returns the textual description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description of this task.
     *
     * @param description the new description; must not exceed {@link #DESCRIPTION_MAX_LENGTH} characters
     * @throws IllegalArgumentException if the description length exceeds {@link #DESCRIPTION_MAX_LENGTH}
     */
    public void setDescription(String description) {
        if (description.length() > DESCRIPTION_MAX_LENGTH) {
            throw new IllegalArgumentException("Description length exceeds " + DESCRIPTION_MAX_LENGTH);
        }
        this.description = description;
    }

    /**
     * Returns the creation timestamp of this task.
     *
     * @return the creation date of the task
     */
    public Instant getCreationDate() {
        return creationDate;
    }

    /**
     * Returns the due date of this task, if any.
     *
     * @return the due date, or {@code null} if not set
     */
    public @Nullable LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Sets or updates the due date for this task.
     *
     * @param dueDate the new due date; can be {@code null} to remove the due date
     */
    public void setDueDate(@Nullable LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Compares this task to another object for equality.
     * <p>
     * Two tasks are considered equal if they have the same non-null identifier.
     * </p>
     *
     * @param obj the object to compare with
     * @return {@code true} if the other object is a {@code Task} with the same ID; {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !getClass().isAssignableFrom(obj.getClass())) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        Task other = (Task) obj;
        return getId() != null && getId().equals(other.getId());
    }

    /**
     * Returns the hash code for this task.
     * <p>
     * The hash code is based only on the class type, not the task ID,
     * because IDs are assigned after persistence and should not affect
     * the hash code of transient objects.
     * </p>
     *
     * @return a constant hash code based on the class
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
