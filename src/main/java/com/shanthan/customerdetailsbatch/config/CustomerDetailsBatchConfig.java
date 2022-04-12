package com.shanthan.customerdetailsbatch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class CustomerDetailsBatchConfig {

    @Value("${batch-properties.inputFilePath}")
    private String inputFile;

    @Value("${batch-properties.reportFilePath}")
    private String reportFile;

    @Value("${batch-properties.outputFilePath}")
    private String outputFile;
}

