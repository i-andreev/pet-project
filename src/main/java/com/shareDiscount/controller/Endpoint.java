package com.shareDiscount.controller;

import com.shareDiscount.controller.exception.ResourceNotFoundException;
import com.shareDiscount.service.ShareDiscService;
import org.springframework.stereotype.Component;

@Component
class Endpoint<T> {

    T findOrElseThrowException(long id, ShareDiscService<T> service) {
        return service.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Resource with id %s doesn't exist", id)));
    }
}
