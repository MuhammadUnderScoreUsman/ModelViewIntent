package com.mohammadosman.modelviewintent.presentation.util

sealed class Resource<T>(
    val data: T? = null,
    val throwable: Throwable? = null
) {

    class Success<T>(data: T) : Resource<T>(data)

    class Error<T>(data: T, throwable: Throwable?) : Resource<T>(data, throwable)

    class Loading<T>(data: T) : Resource<T>(data)
}