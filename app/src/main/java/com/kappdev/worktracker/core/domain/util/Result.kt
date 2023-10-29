package com.kappdev.worktracker.core.domain.util

sealed class Result<out R> {
    data class Success<out R>(val value: R): Result<R>()
    data class Failure(val exception: Exception): Result<Nothing>()

    companion object
}

fun Result.Companion.fail(message: String): Result.Failure {
    return Result.Failure(Exception(message))
}

fun Result.Failure.getMessageOrEmpty(): String {
    return this.exception.message ?: ""
}
