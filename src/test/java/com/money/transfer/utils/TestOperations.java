package com.money.transfer.utils;

import com.money.transfer.dao.AccountDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class TestOperations {

    private static final Logger log = LogManager.getLogger(TestOperations.class);

    static public void setUpTestData() {
        AccountDao accountDao = AccountDao.getInstance();
        accountDao.deleteAllAccounts();
        accountDao.createAccount("James Bond",new BigDecimal(100));
        accountDao.createAccount("John Doe",new BigDecimal(200));
        accountDao.createAccount("Jane Doe",new BigDecimal(110.50));
        accountDao.createAccount("Richard Roe",new BigDecimal(1000));
    }

}
