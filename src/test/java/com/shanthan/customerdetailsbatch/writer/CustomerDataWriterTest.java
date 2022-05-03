package com.shanthan.customerdetailsbatch.writer;

import com.shanthan.customerdetailsbatch.domain.Address;
import com.shanthan.customerdetailsbatch.domain.Customer;
import com.shanthan.customerdetailsbatch.domain.CustomerAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;

import java.util.ArrayList;
import java.util.List;

import static com.shanthan.customerdetailsbatch.util.CustomerTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

class CustomerDataWriterTest {

    @InjectMocks
    private CustomerDataWriter subject;

    @Mock
    private FlatFileItemWriter<CustomerData> customerAccountDataWriter;

    @Mock
    private ExecutionContext executionContext;

    private List<CustomerData> customerDataList;

    private List<CustomerAccount> customerAccounts;
    private List<List<CustomerAccount>> listOfCustomerAccounts;

    @BeforeEach
    void setUp() {
        openMocks(this);
        Customer customer = Customer.builder()
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
        CustomerAccount customerAccount = CustomerAccount.builder()
                .accountNumber(ACCOUNT_NUMBER)
                .customer(customer)
                .build();
       List<CustomerAccount> customerAccounts = new ArrayList<>();
        customerAccounts.add(customerAccount);
        listOfCustomerAccounts = new ArrayList<>();
        listOfCustomerAccounts.add(customerAccounts);
        CustomerData customerData = CustomerData.builder()
                .customerId(CUSTOMER_ID)
                .accountNumber(ACCOUNT_NUMBER)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .phoneNumber(CELL_PHONE)
                .emailAddress(EMAIL_ADDRESS)
                .notificationPreference(NOTIFICATION_PREFERENCE)
                .build();
        customerDataList = new ArrayList<>();
        customerDataList.add(customerData);
    }

    @Test
    void testOpenWriter() {
        subject.open(executionContext);
        verify(customerAccountDataWriter).open(executionContext);
    }

    @Test
    void testUpdateWriter() {
        subject.update(executionContext);
        verify(customerAccountDataWriter).update(executionContext);
    }

    @Test
    void close() {
        subject.close();
        verify(customerAccountDataWriter).close();
    }

    @Test
    void givenValidCustomerAccountsList_whenAskedToWrite_thenWriteCustomerData() throws Exception {
        subject.write(customerAccounts);
        verify(customerAccountDataWriter).write(ArgumentMatchers.eq(customerDataList));
    }
}