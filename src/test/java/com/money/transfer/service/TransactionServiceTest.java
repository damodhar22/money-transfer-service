package com.money.transfer.service;

import com.money.transfer.dao.AccountDao;
import com.money.transfer.model.CreditDebitRequestModel;
import com.money.transfer.model.TransferRequest;
import com.money.transfer.routing.Server;
import com.money.transfer.utils.TestOperations;
import io.restassured.RestAssured;
import io.undertow.util.StatusCodes;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionServiceTest {
    private static AccountDao accountDao = AccountDao.getInstance();

    @BeforeAll
    public static void testSetUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9090;
        TestOperations.setUpTestData();
        Server.start();
    }

    @Test
    @Order(1)
    @DisplayName("Should be able to deposit amount to the account")
    void depositInAccount() {
        Account account = accountDao.listAccounts().get(0);
        CreditDebitRequestModel creditDebitRequestModel = new CreditDebitRequestModel();
        creditDebitRequestModel.setAccountId(account.getAccountId());
        creditDebitRequestModel.setAmount(new BigDecimal(200));
        given().contentType("application/json").body(creditDebitRequestModel)
                .when()
                .post("/accounts/deposit")
                .then()
                .body("accountHolderName", equalTo("James Bond"))
                .body("balance", equalTo(300))
                .body("accountId", notNullValue());
    }

    @Test
    @DisplayName("Bad request in case of invalid amount")
    void depositInAccountInvalidAmount() {
        Account account = accountDao.listAccounts().get(0);
        CreditDebitRequestModel creditDebitRequestModel = new CreditDebitRequestModel();
        creditDebitRequestModel.setAccountId(account.getAccountId());
        creditDebitRequestModel.setAmount(new BigDecimal("-1"));
        given().contentType("application/json").body(creditDebitRequestModel)
                .when()
                .post("/accounts/deposit")
                .then()
                .statusCode(StatusCodes.BAD_REQUEST);
    }

    @Test
    @DisplayName("Account not found in case of account number is not present")
    void depositInAccountAmountNotFound() {
        Account account = accountDao.listAccounts().get(accountDao.listAccounts().size()-1);
        CreditDebitRequestModel creditDebitRequestModel = new CreditDebitRequestModel();
        creditDebitRequestModel.setAccountId(account.getAccountId()+1);
        creditDebitRequestModel.setAmount(new BigDecimal("100"));
        given().contentType("application/json").body(creditDebitRequestModel)
                .when()
                .post("/accounts/deposit")
                .then()
                .statusCode(StatusCodes.NOT_FOUND);
    }

    @Test
    @Order(2)
    @DisplayName("Should be able to withdraw amount from the account")
    void withdrawFromAccount() {
        Account account = accountDao.listAccounts().get(0);
        CreditDebitRequestModel creditDebitRequestModel = new CreditDebitRequestModel();
        creditDebitRequestModel.setAccountId(account.getAccountId());
        creditDebitRequestModel.setAmount(new BigDecimal(200));
        given().contentType("application/json").body(creditDebitRequestModel)
                .when()
                .post("/accounts/withdraw")
                .then()
                .body("accountHolderName", equalTo("James Bond"))
                .body("balance", equalTo(100))
                .body("accountId", notNullValue());
    }

    @Test
    @DisplayName("Should throw error incase of insufficient funds")
    void withdrawFromAccountInsufficientFunds() {
        Account account = accountDao.listAccounts().get(0);
        CreditDebitRequestModel creditDebitRequestModel = new CreditDebitRequestModel();
        creditDebitRequestModel.setAccountId(account.getAccountId());
        creditDebitRequestModel.setAmount(new BigDecimal(10000));
        given().contentType("application/json").body(creditDebitRequestModel)
                .when()
                .post("/accounts/withdraw")
                .then()
                .statusCode(StatusCodes.BAD_REQUEST);
    }

    @Test
    @DisplayName("Bad request in case of invalid amount")
    void withdrawFromAccountInvalidAmount() {
        Account account = accountDao.listAccounts().get(0);
        CreditDebitRequestModel creditDebitRequestModel = new CreditDebitRequestModel();
        creditDebitRequestModel.setAccountId(account.getAccountId());
        creditDebitRequestModel.setAmount(new BigDecimal("-1"));
        given().contentType("application/json").body(creditDebitRequestModel)
                .when()
                .post("/accounts/withdraw")
                .then()
                .statusCode(StatusCodes.BAD_REQUEST);
    }

    @Test
    @DisplayName("Account not found in case of account number is not present")
    void withdrawFromAccountAmountNotFound() {
        Account account = accountDao.listAccounts().get(accountDao.listAccounts().size()-1);
        CreditDebitRequestModel creditDebitRequestModel = new CreditDebitRequestModel();
        creditDebitRequestModel.setAccountId(account.getAccountId()+1);
        creditDebitRequestModel.setAmount(new BigDecimal("-1"));
        given().contentType("application/json").body(creditDebitRequestModel)
                .when()
                .post("/accounts/withdraw")
                .then()
                .statusCode(StatusCodes.NOT_FOUND);
    }

    @Test
    @DisplayName("Should be able to transfer money from one account to another")
    void transfer() {
        TransferRequest transferRequest = new TransferRequest();
        Account account1 = accountDao.listAccounts().get(0);
        Account account2 = accountDao.listAccounts().get(1);
        transferRequest.setPayeeAccountId(account1.getAccountId());
        transferRequest.setPayerAccountId(account2.getAccountId());
        transferRequest.setTransferAmount(new BigDecimal("100"));
        given().contentType("application/json").body(transferRequest)
                .when()
                .post("/transfer")
                .then()
                .statusCode(StatusCodes.OK);
    }

    @Test
    @DisplayName("Invalid payer account number should return not found error")
    void transferInvalidPayerAccount() {
        Account account1 = accountDao.listAccounts().get(0);
        Account account2 = accountDao.listAccounts().get(accountDao.listAccounts().size()-1);
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setPayeeAccountId(account2.getAccountId()+1);
        transferRequest.setPayerAccountId(account1.getAccountId());
        transferRequest.setTransferAmount(new BigDecimal("100"));
        given().contentType("application/json").body(transferRequest)
                .when()
                .post("/transfer")
                .then()
                .statusCode(StatusCodes.NOT_FOUND);
    }

    @Test
    @DisplayName("Invalid payee account number should return not found error")
    void transferInvalidPayeeAccount() {
        Account account1 = accountDao.listAccounts().get(0);
        Account account2 = accountDao.listAccounts().get(accountDao.listAccounts().size()-1);
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setPayeeAccountId(account1.getAccountId());
        transferRequest.setPayerAccountId(account2.getAccountId()+1);
        transferRequest.setTransferAmount(new BigDecimal("100"));
        given().contentType("application/json").body(transferRequest)
                .when()
                .post("/transfer")
                .then()
                .statusCode(StatusCodes.NOT_FOUND);
    }

    @Test
    @DisplayName("Should return insufficient funds error in case of payer balance is less than transfer amount")
    void transferInsufficientFunds() {
        Account account1 = accountDao.listAccounts().get(0);
        Account account2 = accountDao.listAccounts().get(1);
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setPayeeAccountId(account1.getAccountId());
        transferRequest.setPayerAccountId(account2.getAccountId());
        transferRequest.setTransferAmount(new BigDecimal("2000"));
        given().contentType("application/json").body(transferRequest)
                .when()
                .post("/transfer")
                .then()
                .statusCode(StatusCodes.BAD_REQUEST);
    }

    @AfterAll
    public static void killServer(){
        Server.stop();
    }
}