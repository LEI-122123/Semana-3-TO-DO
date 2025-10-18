package com.example.user;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;

@Entity
@Table(name = "app_user")   //  cannot have table "user" in a H2 database; Table created dynamically by Hibernate (ignore IDE warning)
public class User
{
    public static final int USERNAME_MAX_LENGTH = 32;

//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator( name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @Column(name = "user_id")   // Table created dynamically by Hibernate (ignore IDE warning)
    private Long id;

    @Column(name = "name", nullable = false, length = USERNAME_MAX_LENGTH)  //  Ditto
    private String name = "";

    @Column(name = "email", nullable = false)   //  length = 255 (default value)    //  Ditto
    private String email = "";

    @Column(name = "pw", nullable = false)
    private String password = "";

    protected User() { // To keep Hibernate happy
    }

    public User(String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
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

    public String getPassword()
    {
        return password;
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
