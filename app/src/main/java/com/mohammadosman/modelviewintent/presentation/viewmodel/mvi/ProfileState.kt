package com.mohammadosman.modelviewintent.presentation.viewmodel.mvi

data class ProfileState(
    val data: String? = null,
    val loadingState: Boolean = false,
    val pullToRefreshToFalse: Boolean = true,
    val pullToRefLimitationError: String? = null,
    val initialForceToRefresh: Int = 1,
    val exhaustiveForceToRefresh: Int = 3
)