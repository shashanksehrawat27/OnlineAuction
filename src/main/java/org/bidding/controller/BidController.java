package org.bidding.controller;

import org.bidding.database.adapter.ProductAdapter;
import org.bidding.database.adapter.UserAdapter;
import org.bidding.database.mapper.EntityMapper;
import org.bidding.domain.dto.BidDTO;
import org.bidding.domain.dto.ProductDTO;
import org.bidding.domain.dto.UserDTO;
import org.bidding.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/bids")
public class BidController {
    @Autowired
    private ProductAdapter productAdapter;
    @Autowired
    private UserAdapter userAdapter;
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
        ProductDTO product = productAdapter.findById(productId);
        UserDTO user = userAdapter.findById(userId);
        BidDTO bid = bidService.placeBid(product, user, bidAmount);
        return new ResponseEntity<>(bid, HttpStatus.CREATED);

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