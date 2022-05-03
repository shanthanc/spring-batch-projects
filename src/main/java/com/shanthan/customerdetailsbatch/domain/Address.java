package com.shanthan.customerdetailsbatch.domain;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
public class Address {

    @NotNull(message = "Address line 1 cannot be null or empty")
    @Pattern(regexp = "[0-9a-zA-z\\.\\-\\/]+", message = "Invalid address line 1")
    private String address1;

    @Pattern(regexp = "[0-9a-zA-z\\.\\-\\/]+", message = "Invalid address line 2")
    private String address2;

    @NotNull(message = "City cannot be null or empty!")
    @Pattern(regexp = "\\p{Alpha}+", message = "Invalid city!")
    private String city;

    @NotNull(message = "State cannot be null")
    @Size(min = 2, max = 2)
    @Pattern(regexp = "\\p{Upper}", message = "Invalid State!")
    private String state;

    @NotNull(message = "Zipcode cannot be null")
    @Size(min = 5, max = 5)
    @Pattern(regexp = "\\d{5}", message = "Invalid Zipcode!")
    private String zipCode;
}

