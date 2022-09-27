package com.dadapp.seniorproject.user;

import com.dadapp.seniorproject.role.RoleRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Component
public class UserService implements IUserService {

    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");


    @Override
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setRoles(new HashSet<>(roleRepo.findAll()));
        user.setCreatedOn(sdf1.format(timestamp));
        userRepo.save(user);
    }

    @Override
    public User getId(Long id) {
        Optional<User> optional = userRepo.findUserById(id);
        User user;
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new RuntimeException("User not found for id :: " + id);
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }


    @Override
    public void deleteUser(Long id) {
        boolean exists = userRepo.existsById(id);
        if (!exists) {
            throw new IllegalStateException("user with id " + id + " does not exist");
        }
        userRepo.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(Long id, String username, String email) {
        User user = userRepo.findUserById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " + id + " does not exist"));

        if(username != null &&
                username.length() > 0 &&
                !Objects.equals(user.getFullName(), username)) {
            user.setFullName(username);
        }

        if(email != null &&
                email.length() > 0 &&
                !Objects.equals(user.getEmail(), email)) {
            Optional<User> userOptional = userRepo
                    .findUserByEmail(email);
            if (userOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            user.setEmail(email);
        }
    }
}


