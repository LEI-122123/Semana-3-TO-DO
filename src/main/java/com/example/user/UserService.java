package com.example.user;

import com.example.user.User;
import com.example.user.UserRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService
{
    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(Long id, String name, String email)
    {
        if (id < 0){
            throw new RuntimeException("This is for testing the error handler");
        }
        var user = new User(id, name, email);

        userRepository.saveAndFlush(user);
    }

    @Transactional(readOnly = true)
    public List<User> list(Pageable pageable) {
        return userRepository.findAllBy(pageable).toList();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
