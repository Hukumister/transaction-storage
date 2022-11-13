## Summary

This is a simple naive implementation of Transactional Key Value Store. It's create a copy of all data for every new
transaction, but architecture allow to improve it if it needs.

`TransactionStoreBase` – base implementation, witch provide needed functionality
`ThreadSafeTransactionStore` – just a decorator with provide thread safety

## Test

For testing, you can find jar in /artifact/transaction.
Launch app:

```
java -jar transaction.jar
```

## Devops stuff

For build, I use shadow jar plugin to simplify it.
Build app:

```
./gradlew shadowJar
```