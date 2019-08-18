package com.money.transfer.service;

import com.money.transfer.dao.AccountDao;
import com.money.transfer.model.NewAccountRequestModel;
import com.money.transfer.routing.Server;
import com.money.transfer.utils.TestOperations;
import io.restassured.RestAssured;
import io.undertow.util.StatusCodes;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceTest {

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
    @DisplayName("Should be able to create new account with new incremental ID")
    public void createAccountTest() {
        NewAccountRequestModel newAccountRequest = new NewAccountRequestModel();
        newAccountRequest.setAccountHolderName("John Cena");
        newAccountRequest.setBalance(new BigDecimal("2000"));
        given().contentType("application/json").body(newAccountRequest)
                .when()
                .post("/accounts")
                .then()
                .body("accountHolderName", equalTo("John Cena"))
                .body("balance", equalTo(2000))
                .body("accountId", notNullValue());
    }

    @Test
    @DisplayName("Should return existing account details when queried with account number")
    public void getAccountTest() {
        Account account = accountDao.listAccounts().get(0);
        expect().statusCode(200)
                .when()
                .get("/accounts/"+account.getAccountId())
                .then()
                .body("accountHolderName", equalTo("John Doe"))
                .body("balance", equalTo(200))
                .body("accountId", notNullValue());
    }

    @Test
    @DisplayName("Should return account not found if account id not present")
    public void getAccountTestAccountIdNotPresent() {
        Account account = accountDao.listAccounts().get(accountDao.listAccounts().size()-1);
        long id = account.getAccountId() + 1;
        expect().statusCode(StatusCodes.NOT_FOUND).contentType("text/plain")
                .when()
                .get("/accounts/"+id);
    }


    @Test
    @Order(2)
    @DisplayName("Should be able to delete the account given account id")
    public void deleteAccount() {
        Account account = accountDao.listAccounts().get(0);
        expect().statusCode(StatusCodes.NO_CONTENT)
                .when()
                .delete("/accounts/"+account.getAccountId());
    }

    @Test
    @DisplayName("Should return account not found if account id not present")
    public void deleteAccountTestAccountIdNotPresent() {
        Account account = accountDao.listAccounts().get(accountDao.listAccounts().size()-1);
        long id = account.getAccountId() + 1;
        expect().statusCode(StatusCodes.NOT_FOUND).contentType("text/plain")
                .when()
                .delete("/accounts/"+id);
    }

    @Test
    @DisplayName("Should list all the accounts")
    public void listAccounts() {
        expect().statusCode(200)
                .when()
                .get("/accounts")
                .then()
                .body("size()", is(4));
    }

    @AfterAll
    public static void killServer(){
        Server.stop();
    }
}