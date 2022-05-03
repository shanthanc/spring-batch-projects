package com.shanthan.customerdetailsbatch.config;

import com.shanthan.customerdetailsbatch.domain.BadCustomerRecord;
import com.shanthan.customerdetailsbatch.domain.Customer;
import com.shanthan.customerdetailsbatch.domain.CustomerAccount;
import com.shanthan.customerdetailsbatch.listener.CustomerSkipListener;
import com.shanthan.customerdetailsbatch.mapper.CustomerFieldMapper;
import com.shanthan.customerdetailsbatch.processor.CustomerProcessor;
import com.shanthan.customerdetailsbatch.reader.CustomerReader;
import com.shanthan.customerdetailsbatch.validator.CustomerValidator;
import com.shanthan.customerdetailsbatch.writer.CustomerData;
import com.shanthan.customerdetailsbatch.writer.CustomerDataWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.shanthan.customerdetailsbatch.util.CustomerConstants.*;

@Configuration
@EnableBatchProcessing
public class CustomerDetailsBatchConfig {

    @Value("${batch-properties.inputFilePath}")
    private String inputFiles;

    @Value("${batch-properties.badRecordFilePath}")
    private String badRecordFile;

    @Value("${batch-properties.outputFilePath}")
    private String outputFile;

    private final String[] customerOutputPattern =
            {CUSTOMER_ID, ACCOUNT_NUMBER, FIRST_NAME, LAST_NAME, PHONE_NUMBER, NOTIFICATION_PREFERENCE};
    private final String[] badRecordPattern =
            {CUSTOMER_ID, FIRST_NAME, LAST_NAME, PHONE_NUMBER, EMAIL_ADDRESS, ERROR_REASON};

    @Bean
    public List<CustomerAccount> customerAccounts() {
        return new ArrayList<>();
    }

    @Bean
    @StepScope
    public MultiResourceItemReader<Customer> multiCustomerFileReader() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver(classLoader);
        Resource[] resources = patternResolver.getResources("file:" + inputFiles);
        return new MultiResourceItemReaderBuilder<Customer>()
                .name(MULTI_READER)
                .resources(resources)
                .delegate(customerReader())
                .build();
    }

    @Bean
    public FlatFileItemReader<Customer> customerFlatFileItemReader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("customerFlatFileItemReader")
                .lineTokenizer(customerLineTokenizer())
                .lineMapper(customerLineMapper())
                .build();
    }

    @Bean
    public CustomerReader customerReader() {
        return new CustomerReader(customerFlatFileItemReader());
    }

    @Bean
    public FieldSetMapper<Customer> customerFieldSetMapper() {
        return new CustomerFieldMapper();
    }

    @Bean
    public DefaultLineMapper<Customer> customerLineMapper() {
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(customerLineTokenizer());
        lineMapper.setFieldSetMapper(customerFieldSetMapper());
        return lineMapper;
    }

    @Bean
    public DelimitedLineTokenizer customerLineTokenizer() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames(RECORD_ID, CUSTOMER_ID, FIRST_NAME, MIDDLE_NAME, LAST_NAME, ADDRESS_1,
                ADDRESS_2, CITY, STATE, ZIPCODE, EMAIL_ADDRESS, HOME_PHONE, CELL_PHONE, WORK_PHONE,
                NOTIFICATION_PREFERENCE);
        return lineTokenizer;
    }

    @Bean
    public CustomerValidator validator() {
        return new CustomerValidator();
    }

    @Bean
    public ValidatingItemProcessor<Customer> customerValidatingItemProcessor() {
        ValidatingItemProcessor<Customer> validatingItemProcessor = new ValidatingItemProcessor<>();
        validatingItemProcessor.setValidator(validator());
        validatingItemProcessor.setFilter(true);
        return validatingItemProcessor;
    }

    @Bean
    public CustomerProcessor customerProcessor() {
        return new CustomerProcessor();
    }

    @Bean
    public CompositeItemProcessor<Customer, CustomerAccount> processor() {
        CompositeItemProcessor<Customer, CustomerAccount> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(customerValidatingItemProcessor(), customerProcessor()));
        return processor;
    }

    @Bean
    public FlatFileItemWriter<CustomerData> customerAccountDataWriter() {
        BeanWrapperFieldExtractor<CustomerData> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(customerOutputPattern);
        DelimitedLineAggregator<CustomerData> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(PIPE_DELIMITER);
        lineAggregator.setFieldExtractor(fieldExtractor);
        FlatFileItemWriter<CustomerData> customerDataWriter = new FlatFileItemWriter<>();
        customerDataWriter.setResource(new FileSystemResource(outputFile));
        customerDataWriter.setLineAggregator(lineAggregator);
        customerDataWriter.setAppendAllowed(true);
        return customerDataWriter;
    }

    @Bean
    public FlatFileItemWriter<BadCustomerRecord> customerBadRecordWriter() {
        BeanWrapperFieldExtractor<BadCustomerRecord> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(badRecordPattern);
        DelimitedLineAggregator<BadCustomerRecord> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(PIPE_DELIMITER);
        lineAggregator.setFieldExtractor(fieldExtractor);
        FlatFileItemWriter<BadCustomerRecord> badDataWriter = new FlatFileItemWriter<>();
        badDataWriter.setResource(new FileSystemResource(badRecordFile));
        badDataWriter.setLineAggregator(lineAggregator);
        badDataWriter.setAppendAllowed(true);
        return badDataWriter;
    }

    @Bean
    public CustomerDataWriter writer() {
        return new CustomerDataWriter(customerAccountDataWriter());
    }

    @Bean
    public CustomerSkipListener customerSkipListener() {
        return new CustomerSkipListener(customerBadRecordWriter());
    }

    @Bean
    public Job customerJob(JobBuilderFactory jobBuilderFactory,
                           StepBuilderFactory stepBuilderFactory) throws IOException {
        return jobBuilderFactory.get("customerJob")
                .flow(customerStep(stepBuilderFactory))
                .end()
                .build();
    }

    @Bean
    public Step customerStep(StepBuilderFactory stepBuilderFactory) throws IOException {
        return stepBuilderFactory.get("customerStep")
                .<Customer, CustomerAccount>chunk(10)
                .reader(multiCustomerFileReader())
                .processor(processor())
                .writer(writer())
                .faultTolerant()
                .skip(Throwable.class)
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .listener(customerSkipListener())
                .build();
    }

}

