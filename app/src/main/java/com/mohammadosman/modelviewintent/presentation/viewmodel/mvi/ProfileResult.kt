package com.mohammadosman.modelviewintent.presentation.viewmodel.mvi

sealed class ProfileResult {
    object ResultLoading : ProfileResult()
    data class StringResult(
        val res: String
    ) : ProfileResult()

    object ResultError: ProfileResult()
}