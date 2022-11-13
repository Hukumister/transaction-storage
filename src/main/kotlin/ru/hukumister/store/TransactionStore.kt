package ru.hukumister.store

interface TransactionStore<K, T> {

    fun put(key: K, value: T)

    fun get(key: K): T?

    fun delete(key: K)

    fun transaction()

    fun commit(): TransactionData<K, T>?

    fun rollback(): TransactionData<K, T>?

    fun count(value: T): Int
}