package com.shanthan.customerdetailsbatch.reader;

import com.shanthan.customerdetailsbatch.domain.Address;
import com.shanthan.customerdetailsbatch.domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

import static com.shanthan.customerdetailsbatch.util.CustomerTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CustomerReaderTest {

    @InjectMocks
    private CustomerReader subject;

    @Mock
    private ResourceAwareItemReaderItemStream<Customer> delegate;

    @Mock
    private Resource resource;

    @Mock
    private ExecutionContext executionContext;

    private Customer customer;

    @BeforeEach
    void setUp() {
        openMocks(this);
        when(resource.getFilename()).thenReturn("someFileName");
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
    void whenSetResourceCalled_thenTestingForDelegateSet() {
        subject.setResource(resource);
        verify(delegate).setResource(resource);
    }

    @Test
    void givenCustomerInputRow_whenReadCalled_thenCustomerInputRowRead() throws Exception {
        when(delegate.read()).thenReturn(customer);
        Customer result = subject.read();
        assertNotNull(result);
        assertEquals(CUSTOMER_ID, result.getCustomerId());
    }

    @Test
    void testForDelegateOpen() {
        subject.open(executionContext);
        verify(delegate).open(executionContext);
    }

    @Test
    void testForDelegateUpdate() {
        subject.update(executionContext);
        verify(delegate).update(executionContext);
    }

    @Test
    void testForDelegateClose() {
        subject.close();
        verify(delegate).close();
    }
}