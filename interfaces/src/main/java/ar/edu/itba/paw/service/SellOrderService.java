package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.SellOrder;

import java.util.Optional;

public interface SellOrderService {

    Optional<SellOrder> getOrderById(long id);

    SellOrder create(String name, double price, String description, byte[] image, String email);

}
