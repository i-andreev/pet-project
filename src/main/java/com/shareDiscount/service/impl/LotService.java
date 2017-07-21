package com.shareDiscount.service.impl;

import com.shareDiscount.domains.LotParam;
import com.shareDiscount.service.ShareDiscService;
import com.shareDiscount.service.model.Lot;
import com.shareDiscount.service.persistence.LotRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LotService implements ShareDiscService<LotParam> {

    @Autowired
    private LotRepo lotRepo;

    public List<LotParam> getAll(Long userId) {
        List<Lot> lots = null != userId ? lotRepo.findByUserId(userId) : lotRepo.findAll();
        return lots.stream()
                .map(lp -> new LotParam(lp.getId(), lp.getName(), lp.getLifeTime(), lp.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LotParam> findById(long id) {
        return Optional.ofNullable(lotRepo.findOne(id))
                .map(lp -> new LotParam(lp.getId(), lp.getName(), lp.getLifeTime(), lp.getUserId()));
    }

    @Override
    public Optional<LotParam> findByName(String name) {
        return lotRepo.findByName(name)
                .map(lp -> new LotParam(lp.getId(), lp.getName(), lp.getLifeTime(), lp.getUserId()));
    }

    public LotParam create(LotParam lotParam) {
        Lot l = new Lot(lotParam.getUserId(), lotParam.getName(), lotParam.getLifeTime());
        Lot lot = lotRepo.saveAndFlush(l);
        return new LotParam(lot.getId(), lot.getName(), lot.getLifeTime(), lot.getUserId());
    }

    @Override
    public LotParam update(LotParam param, Long id) {
        Lot l = lotRepo.findOne(id);
        l.setName(param.getName());
        l.setLifeTime(param.getLifeTime());
        l.setUserId(param.getUserId());

        Lot lot = lotRepo.save(l);
        return new LotParam(lot.getId(), lot.getName(), lot.getLifeTime(), lot.getUserId());
    }

    @Override
    public void deleteById(long id) {
        lotRepo.delete(lotRepo.findOne(id));
    }
}
