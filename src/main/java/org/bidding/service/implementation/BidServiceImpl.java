package org.bidding.service.implementation;

import org.bidding.model.Bid;
import org.bidding.repository.BidRepository;
import org.bidding.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidServiceImpl implements BidService {

    @Autowired
    private BidRepository bidRepository;

    @Override
    public List<Bid> findAll() {
        return bidRepository.findAll();
    }

    @Override
    public Bid findById(Long id) {
        return bidRepository.findById(id).orElse(null);
    }

    @Override
    public Bid save(Bid bid) {
        return bidRepository.save(bid);
    }

    @Override
    public Bid update(Long id, Bid bid) {
        if (bidRepository.existsById(id)) {
            bid.setId(id);
            return bidRepository.save(bid);
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if (bidRepository.existsById(id)) {
            bidRepository.deleteById(id);
            return true;
        }
        return false;
    }
}