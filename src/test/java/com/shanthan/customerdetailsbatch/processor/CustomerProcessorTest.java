package com.shanthan.customerdetailsbatch.processor;

import com.shanthan.customerdetailsbatch.domain.Address;
import com.shanthan.customerdetailsbatch.domain.Customer;
import com.shanthan.customerdetailsbatch.domain.CustomerAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.shanthan.customerdetailsbatch.util.CustomerTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

class CustomerProcessorTest {

    @InjectMocks
    private CustomerProcessor subject;

    private Customer customer;

    @BeforeEach
    void setUp() {
        openMocks(this);
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
    void givenValidCustomerItemToProcess_whenCalledForProcess_thenCreateAccountNumberAndSaveInMap() {
        CustomerAccount result = subject.process(customer);
        assertNotNull(result);
        assertTrue(Pattern.matches("53\\d{14}",result.getAccountNumber()));
    }
}