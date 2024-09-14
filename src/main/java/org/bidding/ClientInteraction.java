//package org.bidding;
//
//import org.bidding.dto.*;
//import org.bidding.model.*;
//import org.bidding.service.*;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Scanner;
//
//public class ClientInteraction {
//
//    private final VendorService vendorService;
//    private final UserService userService;
//    private final ProductService productService;
//    private final BidService bidService;
//    private final Scanner scanner = new Scanner(System.in);
//
//    public ClientInteraction(VendorService vendorService, UserService userService, ProductService productService, BidService bidService) {
//        this.vendorService = vendorService;
//        this.userService = userService;
//        this.productService = productService;
//        this.bidService = bidService;
//    }
//
//    public void start() {
//        while (true) {
//            System.out.println("Welcome to the Online Auction System");
//            System.out.println("1. Register as Vendor");
//            System.out.println("2. Register as User");
//            System.out.println("3. View Products");
//            System.out.println("4. Place a Bid");
//            System.out.println("5. Exit");
//
//            int choice = scanner.nextInt();
//            scanner.nextLine(); // consume newline
//
//            switch (choice) {
//                case 1:
//                    registerVendor();
//                    break;
//                case 2:
//                    registerUser();
//                    break;
//                case 3:
//                    viewProducts();
//                    break;
//                case 4:
//                    placeBid();
//                    break;
//                case 5:
//                    System.out.println("Exiting...");
//                    return;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//    }
//
//    private void registerVendor() {
//        System.out.println("Enter Vendor Name:");
//        String name = scanner.nextLine();
//        VendorDTO vendorDTO = new VendorDTO();
//        vendorDTO.setName(name);
//
//        if (vendorService.findAll().stream().anyMatch(v -> v.getName().equals(name))) {
//            System.out.println("Vendor already registered.");
//        } else {
//            vendorService.save(convertToVendor(vendorDTO));
//            System.out.println("Vendor registered successfully.");
//        }
//    }
//
//    private void registerUser() {
//        System.out.println("Enter Username:");
//        String username = scanner.nextLine();
//        System.out.println("Enter Email:");
//        String email = scanner.nextLine();
//
//        UserDTO userDTO = new UserDTO();
//        userDTO.setUsername(username);
//        userDTO.setEmail(email);
//
//        if (userService.findAll().stream().anyMatch(user -> user.getUsername().equals(username))) {
//            System.out.println("User already registered.");
//        } else {
//            userService.save(convertToUser(userDTO));
//            System.out.println("User registered successfully.");
//        }
//    }
//
//    private void viewProducts() {
//        System.out.println("Products available:");
//        List<Product> products = productService.findAll();
//        for (Product product : products) {
//            System.out.println("Product ID: " + product.getId() + ", Name: " + product.getName() + ", Price: " + product.getBasePrice());
//        }
//    }
//
//    private void placeBid() {
//        System.out.println("Enter Product ID:");
//        Long productId = scanner.nextLong();
//        System.out.println("Enter Bid Amount:");
//        double amount = scanner.nextDouble();
//        scanner.nextLine(); // consume newline
//
//        System.out.println("Enter Username:");
//        String username = scanner.nextLine();
//
//        User user = userService.findAll().stream()
//                .filter(u -> u.getUsername().equals(username))
//                .findFirst()
//                .orElse(null);
//
//        if (user == null) {
//            System.out.println("User not registered.");
//            return;
//        }
//
//        BidDTO bidDTO = new BidDTO();
//        bidDTO.setProductId(productId);
//        bidDTO.setUserId(user.getId());
//        bidDTO.setAmount(amount);
//        bidDTO.setBidTime(LocalDateTime.now());
//
//        bidService.save(convertToBid(bidDTO));
//        System.out.println("Bid placed successfully.");
//    }
//
//    private Vendor convertToVendor(VendorDTO vendorDTO) {
//        Vendor vendor = new Vendor();
//        vendor.setId(vendorDTO.getId());
//        vendor.setName(vendorDTO.getName());
//        return vendor;
//    }
//
//    private User convertToUser(UserDTO userDTO) {
//        User user = new User();
//        user.setId(userDTO.getId());
//        user.setUsername(userDTO.getUsername());
//        user.setEmail(userDTO.getEmail());
//        return user;
//    }
//
//    private Bid convertToBid(BidDTO bidDTO) {
//        Bid bid = new Bid();
//        bid.setId(bidDTO.getId());
//        bid.setProduct(productService.findById(bidDTO.getProductId()));
//        bid.setUser(userService.findById(bidDTO.getUserId()));
//        bid.setAmount(bidDTO.getAmount());
//        bid.setBidTime(bidDTO.getBidTime());
//        return bid;
//    }
//}
