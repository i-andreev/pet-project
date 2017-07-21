package com.shareDiscount.controller;

import com.shareDiscount.controller.swagger.LotsEndpoint;
import com.shareDiscount.domains.LotParam;
import com.shareDiscount.service.impl.LotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lot")
public class Lots extends Endpoint<LotParam> implements LotsEndpoint {

    @Autowired
    LotService lotService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createLot(@RequestBody LotParam lot) {
        System.out.println("Creating Lot " + lot.getName());

        LotParam lp = lotService.create(lot);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(lp.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(value = "/{lotId}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LotParam> getLot(@PathVariable("lotId") long lotId) {
        System.out.println("Fetching Lot with id " + lotId);

        return ResponseEntity.ok(this.findOrElseThrowException(lotId, lotService));
    }

    @RequestMapping(value = "/{lotId}", method = RequestMethod.PUT)
    public ResponseEntity<LotParam> updateLot(@PathVariable("lotId") long lotId, @RequestBody LotParam lot) {
        System.out.println("Updating Lot with  id: " + lotId);

        this.findOrElseThrowException(lotId, lotService);

        return ResponseEntity.ok(
                lotService.update(lot, lotId));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> listAllLots(@RequestParam(required = false) Long userId) {
        List<LotParam> upl = lotService.getAll(userId);
        return upl.isEmpty() ? ResponseEntity.noContent().build():
                ResponseEntity.ok(upl);    }

    @RequestMapping(value = "/{lotId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteLot(@PathVariable("lotId") long lotId) {
        System.out.println("Fetching & Deleting lot with id:" + lotId);

        this.findOrElseThrowException(lotId, lotService);

        lotService.deleteById(lotId);

        return ResponseEntity.noContent().build();
    }
}
