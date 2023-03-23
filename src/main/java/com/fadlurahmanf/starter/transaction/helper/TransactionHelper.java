package com.fadlurahmanf.starter.transaction.helper;

import com.fadlurahmanf.starter.general.constant.DateFormatter;

import java.util.Date;

public class TransactionHelper {
    public static final String FUND_TRANSFER = "TFF1001";
    public static final String VIRTUAL_ACCOUNT = "TFF1002";

    public static String generateTransactionId(TransactionType type, Long todayTransactionNumber) {
        if (type == TransactionType.FUND_TRANSFER) {
            return getDate() + FUND_TRANSFER + getTransactionNumber(todayTransactionNumber);
        } else {
            return getDate() + VIRTUAL_ACCOUNT + getTransactionNumber(todayTransactionNumber);
        }
    }

    public static String getTransactionNumber(Long todayTransactionNumber){
        int length = todayTransactionNumber.toString().length();
        int trigger = 15 - length;
        StringBuilder transactionNumber = new StringBuilder();
        for(int i = 1; i<=trigger; i++){
            transactionNumber.append("0");
        }
        return transactionNumber.toString() + todayTransactionNumber;
    }

    private static String getDate() {
        return DateFormatter.sdf2.format(new Date());
    }
}

