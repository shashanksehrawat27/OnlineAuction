package org.bidding.controller;

import org.bidding.dto.BidDTO;
import org.bidding.model.Bid;
import org.bidding.service.BidService;
import org.bidding.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bid not found");
        }
        BidDTO bidDTO = convertToDTO(bid);
        return new ResponseEntity<>(bidDTO, HttpStatus.OK);
    }

    // Place a bid
    @PostMapping("/place")
    public ResponseEntity<Object> placeBid(@RequestParam Long productId,
                                           @RequestParam Long userId,
                                           @RequestParam BigDecimal bidAmount) {
        try {
            Bid bid = bidService.placeBid(productId, userId, bidAmount);
            BidDTO bidDTO = convertToDTO(bid);
            return new ResponseEntity<>(bidDTO, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create a new bid
    @PostMapping
    public ResponseEntity<BidDTO> createBid(@RequestBody BidDTO bidDTO) {
        try {
            Bid bid = convertToEntity(bidDTO);
            Bid savedBid = bidService.save(bid);
            BidDTO savedBidDTO = convertToDTO(savedBid);
            return new ResponseEntity<>(savedBidDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid bid data");
        }
    }

    // Helper method to convert Bid entity to BidDTO
    private BidDTO convertToDTO(Bid bid) {
        return new BidDTO(
                bid.getId(),
                bid.getProduct().getId(), // Assuming Product is a related entity
                bid.getUser().getId(), // Assuming User is a related entity
                bid.getAmount(),
                bid.getBidTime()
        );
    }

    // Helper method to convert BidDTO to Bid entity
    private Bid convertToEntity(BidDTO bidDTO) {
        Bid bid = new Bid();
        bid.setAmount(bidDTO.getAmount());
        bid.setBidTime(bidDTO.getBidTime());
        // Set product and user if necessary
        return bid;
    }

    // ErrorResponse class for structured error responses
    public static class ErrorResponse {
        private int status;
        private String message;

        public ErrorResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        // Getters and Setters
        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}