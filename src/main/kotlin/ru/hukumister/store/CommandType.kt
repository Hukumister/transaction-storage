package ru.hukumister.store

enum class CommandType {
    SET,
    GET,
    DELETE,
    COUNT,
    BEGIN,
    COMMIT,
    ROLLBACK,
    EXIT,
    UNKNOWN;

    companion object {

        fun of(value: String): CommandType {
            return values().firstOrNull { it.name == value } ?: UNKNOWN
        }
    }
}