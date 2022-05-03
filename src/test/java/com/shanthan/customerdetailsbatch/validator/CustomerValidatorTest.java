package com.shanthan.customerdetailsbatch.validator;

import com.shanthan.customerdetailsbatch.domain.Address;
import com.shanthan.customerdetailsbatch.domain.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static com.shanthan.customerdetailsbatch.util.CustomerTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class CustomerValidatorTest {

    public CustomerValidator subject;

    private Customer customer;

    @BeforeEach
    void setUp() {
        subject = new CustomerValidator();
        customer = Customer.builder()
                .recordId(RECORD_ID)
                .customerId(CUSTOMER_ID)
                .firstName(FIRST_NAME)
                .middleName(MIDDLE_NAME)
                .lastName(LAST_NAME)
                .address(Address.builder()
                        .address1(ADDRESS_1)
                        .address2(ADDRESS_2)
                        .city(CITY)
                        .state(STATE)
                        .zipCode(ZIP_CODE)
                        .build())
                .emailAddress(EMAIL_ADDRESS)
                .cellPhone(CELL_PHONE)
                .workPhone(WORK_PHONE)
                .homePhone(HOME_PHONE)
                .notificationPreference(NOTIFICATION_PREFERENCE)
                .build();
    }

    @Test
    void validate() {
        subject.validate(customer);
        assertNotNull(subject.getCustomerSet());
        assertEquals(1, subject.getCustomerSet().size());
    }
}