package org.bidding.controller;

import org.bidding.dto.BidDTO;
import org.bidding.model.Bid;
import org.bidding.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidService bidService;

    // Get all bids
    @GetMapping
    public ResponseEntity<List<BidDTO>> getAllBids() {
        List<Bid> bids = bidService.findAll();
        List<BidDTO> bidDTOs = bids.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bidDTOs, HttpStatus.OK);
    }

    // Get a bid by ID
    @GetMapping("/{id}")
    public ResponseEntity<BidDTO> getBidById(@PathVariable Long id) {
        Bid bid = bidService.findById(id);
        if (bid == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BidDTO bidDTO = convertToDTO(bid);
        return new ResponseEntity<>(bidDTO, HttpStatus.OK);
    }

    // Create a new bid
    @PostMapping
    public ResponseEntity<BidDTO> createBid(@RequestBody BidDTO bidDTO) {
        Bid bid = convertToEntity(bidDTO);
        Bid savedBid = bidService.save(bid);
        BidDTO savedBidDTO = convertToDTO(savedBid);
        return new ResponseEntity<>(savedBidDTO, HttpStatus.CREATED);
    }

    // Helper method to convert Bid entity to BidDTO
    private BidDTO convertToDTO(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setId(bid.getId());
        bidDTO.setAmount(bid.getAmount());
        bidDTO.setBidTime(bid.getBidTime());
        // Add more fields as needed
        return bidDTO;
    }

    // Helper method to convert BidDTO to Bid entity
    private Bid convertToEntity(BidDTO bidDTO) {
        Bid bid = new Bid();
        bid.setAmount(bidDTO.getAmount());
        bid.setBidTime(bidDTO.getBidTime());
        // Add more fields as needed
        return bid;
    }
}