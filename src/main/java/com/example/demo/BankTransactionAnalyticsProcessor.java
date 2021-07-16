package com.example.demo;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demo.dao.BankTransaction;

import lombok.Getter;

//@Component
public class BankTransactionAnalyticsProcessor implements ItemProcessor<BankTransaction, BankTransaction> {
	@Getter
	private double totalDebit;
	@Getter
	private double totalCredit;

	@Override
	public BankTransaction process(BankTransaction bankTransaction) throws Exception {
		//
		if(bankTransaction.getTransactionType().equals("D"))
			totalDebit += bankTransaction.getAmount();
		else if (bankTransaction.getTransactionType().equals("C"))
			totalCredit +=bankTransaction.getAmount();
		return bankTransaction;
	}
}