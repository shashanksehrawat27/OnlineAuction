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
        List<BidEntity> bids = bidService.findAllBids();
        List<BidDTO> bidDTOs = bids.stream()
                .map(entityMapper::toBidDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(bidDTOs, HttpStatus.OK);
    }

    // Place a bid
    @PostMapping("/place")
    public ResponseEntity<Object> placeBid(@RequestParam Long productId,
                                           @RequestParam Long userId,
                                           @RequestParam BigDecimal bidAmount) {
        try {
            BidEntity bid = bidService.placeBid(productId, userId, bidAmount);
            BidDTO bidDTO = entityMapper.toBidDTO(bid);
            return new ResponseEntity<>(bidDTO, HttpStatus.CREATED);
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
        BidEntity highestBid = bidService.getCurrentHighestBidForProduct(productId);
        if (highestBid == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BidDTO bidDTO = entityMapper.toBidDTO(highestBid);
        return new ResponseEntity<>(bidDTO, HttpStatus.OK);
    }

}