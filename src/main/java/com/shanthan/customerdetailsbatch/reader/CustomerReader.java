package com.shanthan.customerdetailsbatch.reader;

import com.shanthan.customerdetailsbatch.domain.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.core.io.Resource;

@Slf4j
public class CustomerReader implements ResourceAwareItemReaderItemStream<Customer> {

    private Customer curItem;
    private ResourceAwareItemReaderItemStream<Customer> delegate;

    public CustomerReader(ResourceAwareItemReaderItemStream<Customer> delegate) {
        this.delegate = delegate;
        curItem = null;
    }

    @Override
    public void setResource(Resource resource) {
        log.info("Preparing resource -> {}", resource.getFilename());
        delegate.setResource(resource);
    }

    @Override
    public Customer read() throws Exception {
        if (curItem == null) {
            curItem = delegate.read();
        }

        return curItem;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        log.info("Reading item open with execution context -> [{}]", executionContext.entrySet());
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        log.info("Update read item with execution context ->[{}]", executionContext.entrySet());
        delegate.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        log.info("closing the delegate... ");
        delegate.close();
    }
}
