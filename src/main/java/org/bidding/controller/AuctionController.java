package org.bidding.controller;

import org.bidding.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    @Autowired
    private BidService bidService;

    @PostMapping("/end/{productId}")
    public ResponseEntity<String> endAuction(@PathVariable Long productId) {
        try {
            bidService.endAuction(productId);
            return new ResponseEntity<>("Auction ended successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}