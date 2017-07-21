package com.shareDiscount.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shareDiscount.security.model.LoginCredentials;
import com.shareDiscount.service.model.Lot;
import com.shareDiscount.service.model.User;
import io.swagger.annotations.ApiModel;

import java.util.HashSet;
import java.util.Set;

@ApiModel
public class UserParam {

    private Long id;
    private String userName;

    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String role;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    private Set<Lot> lots = new HashSet<>();

    public UserParam() {
    }
    public UserParam(LoginCredentials lc, String role) {
        this.userName = lc.getUserName();
        this.password = lc.getPassword();
        this.role = role;
    }

    public UserParam(User u) {
        this.id = u.getId();
        this.userName = u.getUserName();
        this.password = u.getPassword();
        this.role = u.getRole();
        this.lots = u.getLots();
    }

    public String getUserName() {
        return userName;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Set<Lot> getLots() {
        return lots;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
