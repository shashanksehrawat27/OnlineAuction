package org.bidding.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorDTO {
    private Long id;
    private String name;
    private String emailId; // Added field

    // Constructors
    public VendorDTO() {}

    public VendorDTO(Long id, String name, String emailId) {
        this.id = id;
        this.name = name;
        this.emailId = emailId;
    }

}
