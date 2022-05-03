package com.shanthan.customerdetailsbatch.listener;

import com.shanthan.customerdetailsbatch.domain.BadCustomerRecord;
import com.shanthan.customerdetailsbatch.domain.Customer;
import com.shanthan.customerdetailsbatch.domain.CustomerAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileItemWriter;

import java.util.Collections;

@Slf4j
public class CustomerSkipListener implements SkipListener<Customer, CustomerAccount> {

    private final FlatFileItemWriter<BadCustomerRecord> customerBadRecordWriter;

    public CustomerSkipListener(FlatFileItemWriter<BadCustomerRecord> customerBadRecordWriter) {
        this.customerBadRecordWriter = customerBadRecordWriter;
    }

    @Override
    public void onSkipInRead(Throwable t) {
        log.error("Skipped reading record because -> {}, [{}]", t.getMessage(), t);
    }

    @Override
    public void onSkipInWrite(CustomerAccount item, Throwable t) {
        try {
            Customer badCustomer = item.getCustomer();
            BadCustomerRecord badRecord = BadCustomerRecord.builder()
                    .customerId(badCustomer.getCustomerId())
                    .firstName(badCustomer.getFirstName())
                    .lastName(badCustomer.getLastName())
                    .emailAddress(badCustomer.getEmailAddress())
                    .phoneNumber(badCustomer.getCellPhone())
                    .errorReason(t.getMessage())
                    .build();
            customerBadRecordWriter.write(Collections.singletonList(badRecord));
        } catch (Exception ex) {
            log.error("Error while writing bad record -> {}", ex.getMessage());
        }

    }

    @Override
    public void onSkipInProcess(Customer item, Throwable t) {
        try {
            BadCustomerRecord badRecord = BadCustomerRecord.builder()
                    .customerId(item.getCustomerId())
                    .firstName(item.getFirstName())
                    .lastName(item.getLastName())
                    .emailAddress(item.getEmailAddress())
                    .phoneNumber(item.getCellPhone())
                    .errorReason(t.getMessage())
                    .build();
            customerBadRecordWriter.write(Collections.singletonList(badRecord));
        } catch (Exception ex) {
            log.error("Error while writing bad record on skip process -> {}", ex.getMessage());
        }
    }

}
