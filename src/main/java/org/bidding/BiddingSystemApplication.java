package org.bidding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BiddingSystemApplication {
    public static void main(String[] args) {
         SpringApplication.run(BiddingSystemApplication.class, args);
//        VendorService vendorService = new VendorServiceImpl(); // Assuming these are implemented
//        UserService userService = new UserServiceImpl();
//        ProductService productService = new ProductServiceImpl();
//        BidService bidService = new BidServiceImpl();
//
//        ClientInteraction clientInteraction = new ClientInteraction(vendorService, userService, productService, bidService);
//        clientInteraction.start();
    }
}