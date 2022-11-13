package ru.hukumister

class TransactionStoreBase<K, T> : TransactionStore<K, T> {

    private val stack = ArrayDeque<TransactionData<K, T>>()

    override fun put(key: K, value: T) {
        if (stack.isNotEmpty()) {
            val transactionData = stack.last()

            val newData = transactionData.data
                .toMutableMap()
                .apply { set(key, value) }

            updateLastTransaction(newData)
        } else {
            stack.addLast(TransactionData(mapOf(key to value)))
        }
    }

    override fun get(key: K): T? = stack.lastOrNull()?.data?.get(key)

    override fun delete(key: K) {
        val transactionData = stack.lastOrNull()
        if (transactionData != null) {
            val newData = transactionData.data
                .toMutableMap()
                .apply { remove(key) }

            updateLastTransaction(newData)
        }
    }

    override fun transaction() {
        val lastTransaction = stack.last()
        stack.addLast(lastTransaction.copy())
    }

    override fun commit(): TransactionData<K, T>? {
        return if (stack.size > 1) {
            val transactionData = stack.removeLast()

            val newData = transactionData.data.toMutableMap()
            updateLastTransaction(newData)

            transactionData
        } else {
            null
        }
    }

    override fun rollback(): TransactionData<K, T>? {
        return if (stack.size > 1) {
            val transactionData = stack.removeLast()
            transactionData
        } else {
            null
        }
    }

    override fun count(value: T): Int {
        return stack.last().data.values
            .count { it == value }
    }

    private fun updateLastTransaction(newData: MutableMap<K, T>) {
        val lastIndex = stack.lastIndex
        stack[lastIndex] = TransactionData(newData)
    }
}
