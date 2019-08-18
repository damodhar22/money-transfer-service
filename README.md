
# money-transfer-service
A restful service to transfer money between accounts

**Technologies**
- Undertow server(no other libraries are used)
- Log4j
- Java8
- Rest assured
- Junit5

Application starts webserver at http://localhost:9090 by default

Application can be started as gradle task:

```sh
./gradlew run
```

Run the tests using the gradle task:

```sh
./gradlew test
```

## Account API

**/accounts**

**GET** - retrieves all accounts from database

Response:

**Status: 200 OK**
```javascript
[
  {
    "accountId": 1,
    "accountHolderName": "James Bond",
    "balance": 1000
  },
  {
    "accountId": 2,
    "accountHolderName": "John Doe",
    "balance": 110.5
  }
]
```
---
**/accounts**

**POST** - persists new account
 
**Request Body** - NewAccount request object

Sample request:
```javascript
{
  "accountHolderName":"James Bond",
  "balance": 5.0
}
```

Sample response:

**Status: 201 CREATED**
```javascript
{
  "accountId": 1,
  "accountHolderName": "James Bond",
  "balance": 5.0
}
```
---
**/accounts/{accountId}** - account id

**GET** - retrieves account by id from database

Response:

**Status: 200 OK**
```javascript
{
  "accountId": 1,
  "accountHolderName": "James Bond",
  "balance": 5.0
}
```
Account doesn't exist:
**Status: 404 account not found**

---
**/accounts/{accountId}** - account id

**DELETE** - deletes account by id from database

Response:

**Status: 204 No Content**

Account doesn't exist:
**Status: 404 account not found**

## Transaction API

**/accounts/deposit**

**POST** - deposits amount in the account
 
**Request Body** - DepositRequest object

Sample request:
```javascript
{
  "accountId": "1",
  "amount": 5.0
}
```

Sample response:
**Status: 200 OK**
```javascript
{
  "accountId": 1,
  "accountHolderName": "James Bond",
  "balance": 5.0
}
```
Account doesn't exist:
**Status: 404 account not found**

Invalid amount:
**Status: 400 Bad request**

---
**/accounts/withdraw**

**POST** - deposits amount in the account
 
**Request Body** - DepositRequest object

Sample request:
```javascript
{
  "accountId": "1",
  "amount": 5.0
}
```
Sample response:
**Status: 200 OK**
```javascript
{
  "accountId": 1,
  "accountHolderName": "James Bond",
  "balance": 5.0
}
```
Account doesn't exist:
**Status: 404 account not found**

Invalid amount:
**Status: 400 Bad request**

Insufficient balance on source account:
**Status: 400 Bad request**

---

**/transfer**

**POST** - submit new transfer request between accounts

**Request Body** - TransferRequest object

Sample request:
```javascript
{
  "payerAccountId": "1",
  "payeeAccountId": "2",
  "transferAmount": 5.0
}
```

Sample response:
**Status: 200 OK**

Invalid amount:
**Status: 400 Bad request**

Insufficient balance on source account:
**Status: 400 Bad request**

One of the transfer accounts doesn't exist:
**Status: 400 Bad Request**

