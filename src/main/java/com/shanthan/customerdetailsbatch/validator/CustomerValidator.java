package com.shanthan.customerdetailsbatch.validator;

import com.shanthan.customerdetailsbatch.domain.Customer;
import com.shanthan.customerdetailsbatch.domain.NotificationPreference;
import com.shanthan.customerdetailsbatch.domain.State;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamSupport;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;

import java.util.HashSet;
import java.util.Set;

import static com.shanthan.customerdetailsbatch.util.CustomerConstants.BEGIN_CUST_ID;
import static java.util.Arrays.stream;

@Slf4j
@Getter
public class CustomerValidator extends ItemStreamSupport implements Validator<Customer> {

    private Set<Customer> customerSet;

    @Override
    public void validate(Customer customer) throws ValidationException {
        log.info("Validating Customer object ");
        customerSet = new HashSet<>();

        char beginChar = customer.getCustomerId().charAt(0);
        if (beginChar != BEGIN_CUST_ID) {
            log.error("CustomerId begins with -> {} ", beginChar);
            throw new ValidationException("CustomerId must begins with invalid character! ");
        }

        boolean isNotValidState = stream(State.values())
                .noneMatch(state -> state.getStateAbbreviation().equals(customer.getAddress().getState()));

        if (isNotValidState) {
            log.error("Invalid state entered -> {}", customer.getAddress().getState());
            throw new ValidationException("Must be a valid US state!");
        }

        boolean isNotValidPreference = stream(NotificationPreference.values())
                .noneMatch(preference -> preference.getPreference().equals(customer.getNotificationPreference()));

                    if (isNotValidPreference) {
                        log.error("Notification preference value -> {} ", customer.getNotificationPreference());
                        throw new ValidationException("Invalid Notification preference value!");
                    }

        customerSet.add(customer);
    }
}
