package com.example.demo;


import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.example.demo.dao.BankTransaction;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	@Autowired private JobBuilderFactory jobBuilderFactory;
	@Autowired private StepBuilderFactory stepBuilderFactory;
	@Autowired private ItemReader<BankTransaction> itemReader;
	@Autowired private ItemWriter<BankTransaction> itemWriter;
	//@Autowired private ItemProcessor<BankTransaction,BankTransaction> bankTransactionItemProcessor;
	
	@Bean
	public Job bankJob() {
		Step step= stepBuilderFactory.get("step-load-data") // creer un nom pour le step
				.<BankTransaction,BankTransaction>chunk(100) // methode generique (input , output)
				.reader(itemReader)
				.processor(compositeItemProcessor())
				.writer(itemWriter)
				.build();
		return jobBuilderFactory.get("bank-data-loader-job").start(step).build(); // return le job
	}
	
	@Bean
	public FlatFileItemReader<BankTransaction> fileReader(@Value("${inputFile}") Resource resource){
		FlatFileItemReader<BankTransaction> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setName("FFIR1");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setResource(resource);
		flatFileItemReader.setLineMapper(lineMapper());
		return flatFileItemReader;
	}

	@Bean
	public LineMapper<BankTransaction> lineMapper() {
		// TODO Auto-generated method stub
		DefaultLineMapper<BankTransaction> defaultLineMapper = new DefaultLineMapper<>();
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames("id","accountID","strTransactionDate","transactionType","amount");
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		BeanWrapperFieldSetMapper<BankTransaction> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
		beanWrapperFieldSetMapper.setTargetType(BankTransaction.class);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		return defaultLineMapper;
	}
	
	@Bean ItemProcessor<BankTransaction, BankTransaction> compositeItemProcessor(){
		List<ItemProcessor<BankTransaction,BankTransaction>> itemProcessors = new ArrayList<>();
		itemProcessors.add(itemProcessor1());
		itemProcessors.add(itemProcessor2());
		CompositeItemProcessor<BankTransaction, BankTransaction> compositeItemProcessor = new CompositeItemProcessor<>();
		compositeItemProcessor.setDelegates(itemProcessors);
		return compositeItemProcessor;
		
	}

	@Bean
	BankTransactionItemProcessor itemProcessor1(){
		return new BankTransactionItemProcessor();
	}
	
	@Bean
	BankTransactionAnalyticsProcessor itemProcessor2(){
		return new BankTransactionAnalyticsProcessor();
	}
}



