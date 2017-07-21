package com.shareDiscount.controller;

import com.shareDiscount.controller.exception.FailedToLoginException;
import com.shareDiscount.security.model.AuthenticatedUser;
import com.shareDiscount.security.model.LoginCredentials;
import com.shareDiscount.security.model.JwtAuthenticationToken;
import com.shareDiscount.controller.exception.ResourceAlreadyExistException;
import com.shareDiscount.domains.UserParam;
import com.shareDiscount.security.service.JwtService;
import com.shareDiscount.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@RestController
@RequestMapping("/api")
public class Authentication {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<AuthenticatedUser> login(@RequestBody LoginCredentials loginCredentials) {

        UserParam userParam = userService.loadUser(loginCredentials)
                .orElseThrow(() -> new FailedToLoginException(loginCredentials.getUserName()));

        String token = jwtService.generateToken(userParam);

        return ResponseEntity.ok(
                jwtService.getUserFromToken(token)
        );
    }

    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public ResponseEntity<AuthenticatedUser> signUp(@RequestBody LoginCredentials lc) {

        System.out.println("Creating User " + lc.getUserName());

        this.userService.findByName(lc.getUserName())
                .ifPresent(s -> {
                    throw new ResourceAlreadyExistException(lc.getUserName());
                });

        UserParam up = userService.create(new UserParam(lc, "USER"));

        String token = jwtService.generateToken(up);

        return ResponseEntity.ok(
                jwtService.getUserFromToken(token)
        );
    }
}
