package com.shanthan.customerdetailsbatch.processor;

import com.shanthan.customerdetailsbatch.domain.Customer;
import com.shanthan.customerdetailsbatch.domain.CustomerAccount;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;

import static com.shanthan.customerdetailsbatch.domain.NotificationPreference.getNotificationPreference;
import static com.shanthan.customerdetailsbatch.processor.AccountNumberGenerator.generateAccountNumber;

@Slf4j
@Getter
public class CustomerProcessor implements ItemProcessor<Customer, CustomerAccount> {

    @Override
    public CustomerAccount process(Customer item) {
        String customerId = null, accountNumber;
        CustomerAccount customerAccount = null;
        try {
            customerId = item.getCustomerId();
            log.info("Generating account number for customer with customerId -> {} ", customerId);
            accountNumber = generateAccountNumber(getNotificationPreference(item.getNotificationPreference()));
            customerAccount = CustomerAccount.builder()
                            .customer(item)
                            .accountNumber(accountNumber)
                    .build();
        } catch (Exception ex) {
            log.error("Error generating account Number for customer with customerId -> {} : {} ",
                    customerId, ex.getMessage());
           

        }
        return customerAccount;
    }
}
