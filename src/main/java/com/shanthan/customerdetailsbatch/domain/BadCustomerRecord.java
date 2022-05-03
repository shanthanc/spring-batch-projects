package com.shanthan.customerdetailsbatch.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BadCustomerRecord {

    private String customerId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private String errorReason;
}

