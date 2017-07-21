package com.shareDiscount.service.impl;

import com.shareDiscount.domains.UserParam;
import com.shareDiscount.security.model.LoginCredentials;
import com.shareDiscount.service.ShareDiscService;
import com.shareDiscount.service.model.User;
import com.shareDiscount.service.persistence.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;



@Service
public class UserService implements ShareDiscService<UserParam>{

    @Autowired
    private UserRepo userRepo;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public List<UserParam> getAll() {
        return userRepo.findAll()
                .stream()
                .map(UserParam::new)
                .collect(Collectors.toList());
    }

    public Optional<UserParam> findById(long id) {
        return Optional.ofNullable(userRepo.findOne(id))
                .map(UserParam::new);
    }

    public Optional<UserParam> findByName(String name) {
        return userRepo.findByUserName(name)
                .map(UserParam::new);
    }

    public UserParam create(UserParam userParam) {
        User u = new User(userParam.getUserName(), passwordEncoder().encode(userParam.getPassword()), userParam.getRole(), userParam.getLots());
        User user = userRepo.save(u);
        return new UserParam(user);
    }

    public UserParam update(UserParam userParam, Long id) {
        User u = userRepo.findOne(id);
        u.setUserName(userParam.getUserName());
        User user = userRepo.save(u);
        return new UserParam(user);
    }

    public Optional<UserParam> loadUser(LoginCredentials lc) {
        Optional<UserParam> userOptional = findByName(lc.getUserName());
        return userOptional.map(user -> passwordEncoder().matches(lc.getPassword(), user.getPassword()) ? user : null);
    }

    public void deleteById(long id) {
        userRepo.delete(userRepo.findOne(id));
    }

}
