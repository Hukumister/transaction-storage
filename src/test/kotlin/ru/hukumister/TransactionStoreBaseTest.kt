package ru.hukumister

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class TransactionStoreBaseTest {

    private val transactionCore = TransactionStoreBase<String, String>()

    @Test
    internal fun `set and get a value`() {
        transactionCore.put("foo", "123")

        assertEquals("123", transactionCore.get("foo"))
    }

    @Test
    internal fun `delete a value`() {
        transactionCore.put("foo", "123")
        transactionCore.delete("foo")

        assertNull(transactionCore.get("foo"))
    }

    @Test
    internal fun `count the number of occurrences of a value`() {
        transactionCore.put("foo", "123")
        transactionCore.put("bar", "123")
        transactionCore.put("baz", "456")

        assertEquals(2, transactionCore.count("123"))
        assertEquals(1, transactionCore.count("456"))
        assertEquals(0, transactionCore.count("789"))
    }

    @Test
    internal fun `commit a transaction`() {
        transactionCore.put("bar", "123")
        transactionCore.transaction()

        transactionCore.put("foo", "456")

        assertEquals("123", transactionCore.get("bar"))

        transactionCore.delete("bar")
        transactionCore.commit()

        assertNull(transactionCore.get("bar"))
        assertNull(transactionCore.rollback())

        assertEquals("456", transactionCore.get("foo"))
        assertNull(transactionCore.commit())
    }

    @Test
    internal fun `rollback a transaction`() {
        transactionCore.put("foo", "123")
        transactionCore.put("bar", "abc")

        transactionCore.transaction()

        assertEquals("123", transactionCore.get("foo"))
        transactionCore.put("bar", "def")

        assertEquals("def", transactionCore.get("bar"))

        transactionCore.rollback()

        assertEquals("123", transactionCore.get("foo"))
    }

    @Test
    internal fun `nested transactions`() {
        transactionCore.put("foo", "123")
        transactionCore.put("bar", "456")

        transactionCore.transaction()
        transactionCore.put("foo", "456")

        assertEquals("456", transactionCore.get("foo"))

        transactionCore.transaction()
        assertEquals(2, transactionCore.count("456"))

        transactionCore.put("foo", "789")

        assertEquals("789", transactionCore.get("foo"))

        transactionCore.rollback()
        assertEquals("456", transactionCore.get("foo"))

        transactionCore.delete("foo")
        assertNull(transactionCore.get("foo"))

        transactionCore.rollback()
        assertEquals("123", transactionCore.get("foo"))
    }

    @Test
    internal fun `initial has empty store`() {
        assertNull(transactionCore.get("foo"))
        assertNull(transactionCore.get("bar"))
    }

    @Test
    internal fun `count in nested transaction`() {
        transactionCore.put("foo", "123")
        transactionCore.transaction()

        transactionCore.put("foo", "123")
        transactionCore.transaction()

        transactionCore.put("foo", "123")
        transactionCore.transaction()

        assertEquals(1, transactionCore.count("123"))
    }
}