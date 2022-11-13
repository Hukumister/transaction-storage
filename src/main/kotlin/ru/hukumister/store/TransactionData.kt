package ru.hukumister.store

data class TransactionData<K, V>(
    val data: Map<K, V>
)
