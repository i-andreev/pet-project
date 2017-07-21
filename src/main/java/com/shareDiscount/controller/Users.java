package com.shareDiscount.controller;


import com.shareDiscount.controller.swagger.UsersEndpoint;
import com.shareDiscount.controller.exception.ResourceAlreadyExistException;
import com.shareDiscount.domains.UserParam;
import com.shareDiscount.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class Users extends Endpoint<UserParam> implements UsersEndpoint{

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> listAllUsers() {
        List<UserParam> upl = userService.getAll();
        return upl.isEmpty() ? ResponseEntity.noContent().build():
                ResponseEntity.ok(upl);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserParam> getUser(@PathVariable("userId") long userId) {
        System.out.println("Fetching User with id " + userId);

        return ResponseEntity.ok(this.findOrElseThrowException(userId, userService));
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserParam user) {

        System.out.println("Creating User " + user.getUserName());

        this.isNameExist(user.getUserName());

        UserParam up = userService.create(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(up.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<UserParam> updateUser(@PathVariable("userId") long userId, @RequestBody UserParam user) {
        System.out.println("Updating User with  id: " + userId);

        this.findOrElseThrowException(userId, userService);

        return ResponseEntity.ok(
                userService.update(user, userId));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("userId") long userId) {
        System.out.println("Fetching & Deleting User with id:" + userId);

        this.findOrElseThrowException(userId, userService);

        userService.deleteById(userId);

       return ResponseEntity.noContent().build();
    }

    private void isNameExist(String userName) {
        this.userService.findByName(userName)
                .ifPresent(s -> {
                    throw new ResourceAlreadyExistException(userName);
                });
    }
}