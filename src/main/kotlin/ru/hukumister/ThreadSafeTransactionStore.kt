package ru.hukumister

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

class ThreadSafeTransactionStore<K, T>(
    private val delegate: TransactionStore<K, T>
) : TransactionStore<K, T> {

    private val lock = ReentrantReadWriteLock()

    override fun put(key: K, value: T) = lock.write { delegate.put(key, value) }

    override fun get(key: K): T? = lock.read { delegate.get(key) }

    override fun delete(key: K) = lock.write { delegate.delete(key) }

    override fun transaction() = lock.write { delegate.transaction() }

    override fun commit() = lock.write { delegate.commit() }

    override fun rollback(): TransactionData<K, T>? = lock.write { delegate.rollback() }

    override fun count(value: T): Int = lock.read { delegate.count(value) }
}