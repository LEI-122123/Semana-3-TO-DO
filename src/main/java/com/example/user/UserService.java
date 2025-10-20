package com.example.user;

import com.example.IFindAll;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements IFindAll<User>
{
    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void createUser(String name, String email, String password)
    {
//        if (id < 0){
//            throw new RuntimeException("This is for testing the error handler");
//        }
        var user = new User(name, email, password);

        userRepository.saveAndFlush(user);
    }

    @Transactional(readOnly = true)
    public List<User> list(Pageable pageable) {
        return userRepository.findAllBy(pageable).toList();
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
