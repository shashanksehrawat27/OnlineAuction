package org.bidding.service;

import org.bidding.model.Bid;

import java.util.List;

public interface BidService {
    List<Bid> findAll();
    Bid findById(Long id);
    Bid save(Bid bid);
    Bid update(Long id, Bid bid);
    boolean delete(Long id);
}