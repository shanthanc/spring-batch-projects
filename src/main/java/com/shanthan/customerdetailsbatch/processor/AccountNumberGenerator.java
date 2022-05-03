package com.shanthan.customerdetailsbatch.processor;

import com.shanthan.customerdetailsbatch.exception.CustomerDetailBatchException;
import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Slf4j
public class AccountNumberGenerator {

    public static String generateAccountNumber(Integer notificationPreference)
            throws CustomerDetailBatchException {
        switch (notificationPreference) {
            case 1 -> {
                return "43".concat(randomNumeric(14));
            }
            case 2 -> {
                return "53".concat(randomNumeric(14));
            }
            case 3 -> {
                return "63".concat(randomNumeric(14));
            }
            case 4 -> {
                return "73".concat(randomNumeric(14));
            }
            case 5 -> {
                return "83".concat(randomNumeric(14));
            }
            default -> {
                throw new CustomerDetailBatchException("Invalid Notification Preference! ");
            }
        }
    }

}
