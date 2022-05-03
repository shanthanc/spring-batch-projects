package com.shanthan.customerdetailsbatch.writer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerData {
    private String customerId;
    private String accountNumber;
    private String emailAddress;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private Integer notificationPreference;
}

