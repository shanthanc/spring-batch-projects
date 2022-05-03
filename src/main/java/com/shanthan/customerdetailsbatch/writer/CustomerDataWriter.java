package com.shanthan.customerdetailsbatch.writer;

import com.shanthan.customerdetailsbatch.domain.Customer;
import com.shanthan.customerdetailsbatch.domain.CustomerAccount;
import com.shanthan.customerdetailsbatch.exception.CustomerDetailBatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomerDataWriter implements ItemStreamWriter<CustomerAccount> {

    private final FlatFileItemWriter<CustomerData> customerAccountDataWriter;

    public CustomerDataWriter(FlatFileItemWriter<CustomerData> customerAccountDataWriter) {
        this.customerAccountDataWriter = customerAccountDataWriter;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        log.info("Customer writer is open now ... ");
        customerAccountDataWriter.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        log.info("Customer writer in update ");
        customerAccountDataWriter.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        customerAccountDataWriter.close();
    }


    private List<CustomerData> writeData(List<CustomerAccount> accounts) {
        List<CustomerData> customerDataList = new ArrayList<>();
        accounts.forEach(acct -> {
            Customer customer = acct.getCustomer();
            customerDataList.add(CustomerData.builder()
                    .customerId(customer.getCustomerId())
                    .accountNumber(acct.getAccountNumber())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .emailAddress(customer.getEmailAddress())
                    .phoneNumber(customer.getCellPhone())
                    .notificationPreference(customer.getNotificationPreference())
                    .build());
        });
        return customerDataList;
    }

    @Override
    public void write(List<? extends CustomerAccount> items) throws Exception {
        try {
            customerAccountDataWriter.write(writeData((List<CustomerAccount>) items));
        } catch (Exception e) {
            log.error("Exception occurred while writing customer data to the file -> {} ", e.getMessage());
            throw new CustomerDetailBatchException(e.getMessage(), e);
        }
    }
}
