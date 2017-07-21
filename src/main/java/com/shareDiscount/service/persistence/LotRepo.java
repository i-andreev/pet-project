package com.shareDiscount.service.persistence;

import com.shareDiscount.service.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LotRepo extends JpaRepository<Lot, Long> {
    Optional<Lot> findByName(String name);
    List<Lot> findByUserId(Long userId);
}
