package com.dadapp.seniorproject.user;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IUserService {

    List<User> findAllUsers();
    User findByUsername(String username);
    void deleteUser(Long id);
    void updateUser(Long id, String username, String email);
    void save(User user) throws Exception;
    User getId(Long id);
}
