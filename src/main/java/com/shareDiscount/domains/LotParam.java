package com.shareDiscount.domains;

public class LotParam {

    private Long id;
    private String name;
    private long lifeTime;
    private long userId;

    public LotParam() {
    }

    public LotParam(Long id, String name, long lifeTime, long userId) {
        this.id = id;
        this.name = name;
        this.lifeTime = lifeTime;
        this.userId = userId;
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
}
