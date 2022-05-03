package com.shanthan.customerdetailsbatch.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerDetailBatchException extends RuntimeException {

    public CustomerDetailBatchException(String message) {
        super(message);
    }

    public CustomerDetailBatchException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
