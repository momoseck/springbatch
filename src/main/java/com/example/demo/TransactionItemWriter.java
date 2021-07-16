package com.example.demo;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dao.BankTransaction;
import com.example.demo.dao.BankTransactionRepository;

@Component
public class TransactionItemWriter implements ItemWriter<BankTransaction> {
	@Autowired
	private BankTransactionRepository bankTransactionrepository;

	@Override
	public void write(List<? extends BankTransaction> items) throws Exception {
		// TODO Auto-generated method stub
		bankTransactionrepository.saveAll(items);

	}

}
