package com.shareDiscount.service.model;

import javax.persistence.*;

@Entity
@Table(name = "lot")
public class Lot {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 32, unique = true, nullable = false)
    private String name;

    @Column(name = "life_time")
    private long lifeTime;

    @Column(name = "user_id")
    private long userId;

    Lot() {

    }

    public Lot(long userId, String name, long lifeTime) {
        this.userId = userId;
        this.name = name;
        this.lifeTime = lifeTime;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getLifeTime() {
        return lifeTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLifeTime(long lifeTime) {
        this.lifeTime = lifeTime;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
