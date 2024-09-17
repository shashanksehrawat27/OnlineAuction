package org.bidding.controller;

import org.bidding.database.entity.BidEntity;
import org.bidding.database.mapper.EntityMapper;
import org.bidding.dto.BidDTO;
import org.bidding.service.BidService;
import org.bidding.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.bidding.exception.ErrorResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bids")
public class BidController {

    @Autowired
    private BidService bidService;
    @Autowired
    private EntityMapper entityMapper;
    // Get all bids
    @GetMapping
    public ResponseEntity<List<BidDTO>> getAllBids() {
        List<BidDTO> bids = bidService.findAllBids();
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    // Place a bid
    @PostMapping("/place")
    public ResponseEntity<Object> placeBid(@RequestParam Long productId,
                                           @RequestParam Long userId,
                                           @RequestParam BigDecimal bidAmount) {
        try {
            BidDTO bid = bidService.placeBid(productId, userId, bidAmount);
            return new ResponseEntity<>(bid, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get current highest bid on a product
    @GetMapping("/product/{productId}/highest")
    public ResponseEntity<BidDTO> getCurrentHighestBidForProduct(@PathVariable Long productId) {
        BidDTO highestBid = bidService.getCurrentHighestBidForProduct(productId);
        if (highestBid == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(highestBid, HttpStatus.OK);
    }

}