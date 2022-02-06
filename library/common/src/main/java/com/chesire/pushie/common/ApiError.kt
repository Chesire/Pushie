package com.chesire.pushie.common

/**
 * Information that can come back from a call to an API.
 */
data class ApiError(
    val code: Int? = null,
    val throwable: Throwable? = null
)
