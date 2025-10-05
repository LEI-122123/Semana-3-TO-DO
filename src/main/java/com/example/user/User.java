package com.example.user;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

@Entity
public class User
{
    @Id
    @GeneratedValue
    public static final int USERNAME_MAX_LENGTH = 32;

    @Column(name = "user_id")
    private Long id;

    @Column(name = "name", nullable = false, length = USERNAME_MAX_LENGTH)
    private String name = "";

    @Column(name = "name", nullable = false, length = USERNAME_MAX_LENGTH)
    private String email = "";

    protected User() { // To keep Hibernate happy
    }

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public @Nullable Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int hashCode() {
        // Hashcode should never change during the lifetime of an object. Because of
        // this we can't use getId() to calculate the hashcode. Unless you have sets
        // with lots of entities in them, returning the same hashcode should not be a
        // problem.
        return getClass().hashCode();
    }
}
