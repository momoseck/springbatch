package com.example.demo;

import java.text.SimpleDateFormat;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demo.dao.BankTransaction;

//@Component
public class BankTransactionItemProcessor implements ItemProcessor<BankTransaction, BankTransaction>{
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
	@Override
	public BankTransaction process(BankTransaction bankTransaction) throws Exception {
		// TODO Auto-generated method stub
		bankTransaction.setTransactionDate(dateFormat.parse(bankTransaction.getStrTransactionDate()));
		return bankTransaction;
	}

}
