package ru.hukumister.main

import ru.hukumister.store.CommandType
import ru.hukumister.store.ThreadSafeTransactionStore
import ru.hukumister.store.TransactionStoreBase
import java.util.*

private val transactionCore = ThreadSafeTransactionStore(TransactionStoreBase<String, String>())

fun main() {
    val reader = Scanner(System.`in`)
    println("Enter command:")
    do {
        val line = reader.nextLine()
        val args = line.split(" ")
        val commandType = CommandType.of(args.firstOrNull().orEmpty())
        when (commandType) {
            CommandType.UNKNOWN -> println("Unknown command")
            CommandType.EXIT -> println("Exit program")
            else -> {
                val output = handleTransactionCommand(commandType, args)
                if (output.isNotEmpty()) {
                    println(output)
                }
            }
        }
    } while (commandType != CommandType.EXIT)
}

private fun handleTransactionCommand(
    commandType: CommandType,
    args: List<String>
): String {
    val commandArgs = args.subList(1, args.size)
    return when (commandType) {
        CommandType.SET -> handleSetCommand(commandArgs)
        CommandType.GET -> handleGetCommand(commandArgs)
        CommandType.DELETE -> handleDeleteCommand(commandArgs)
        CommandType.COUNT -> handleCountCommand(commandArgs)
        CommandType.BEGIN -> handleBeginCommand()
        CommandType.COMMIT -> handleCommitCommand()
        CommandType.ROLLBACK -> handleRollbackCommand()
        else -> error("Unsupported type")
    }
}

fun handleRollbackCommand(): String {
    val lastTransaction = transactionCore.rollback()
    return if (lastTransaction != null) "" else "no transaction"
}

fun handleCommitCommand(): String {
    val lastTransaction = transactionCore.commit()
    return if (lastTransaction != null) "" else "no transaction"
}

fun handleBeginCommand(): String {
    transactionCore.transaction()
    return ""
}

fun handleCountCommand(args: List<String>): String {
    return if (args.size == 1) {
        transactionCore.count(args.first()).toString()
    } else {
        "Wrong format. Please enter: value"
    }
}

fun handleDeleteCommand(args: List<String>): String {
    return if (args.size == 1) {
        transactionCore.delete(args.first())
        ""
    } else {
        "Wrong format. Please enter: key"
    }
}

fun handleGetCommand(args: List<String>): String {
    return if (args.size == 1) {
        transactionCore.get(args.first()) ?: "key not set"
    } else {
        "Wrong format. Please enter: key"
    }
}

fun handleSetCommand(args: List<String>): String {
    return if (args.size == 2) {
        val (key, value) = args
        transactionCore.put(key, value)
        ""
    } else {
        "Wrong format. Please enter: key value"
    }
}