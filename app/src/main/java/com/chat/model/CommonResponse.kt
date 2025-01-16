package com.chat.model

data class CommonResponse<T>(
    val status: String,
    val message: String,
    val data: T
)