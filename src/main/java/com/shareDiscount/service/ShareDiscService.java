package com.shareDiscount.service;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface ShareDiscService<T> {

    Optional<T> findById(long id);

    Optional<T> findByName(String name);

    T create(T param);

    T update(T param, Long id);

    void deleteById(long id);
}
