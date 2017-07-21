package com.shareDiscount.service.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 32, unique = true, nullable = false)
    private String userName;
    private String password;

    private String role;

    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.REMOVE, mappedBy = "userId")
    private Set<Lot> lots;

    User(){

    }
    public User(String userName, String password, String role, Set<Lot> lots) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.lots = lots;
    }

    public Long getId() {
        return id;
    }

    public Set<Lot> getLots() {
        return lots;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public void erasePassword() {
        this.password = null;
    }

}
