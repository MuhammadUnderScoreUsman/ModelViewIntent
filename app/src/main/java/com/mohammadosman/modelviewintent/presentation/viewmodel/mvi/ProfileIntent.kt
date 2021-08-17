package com.mohammadosman.modelviewintent.presentation.viewmodel.mvi

sealed class ProfileIntent {

    object LoadingState: ProfileIntent()
    object ForcePullToRefresh: ProfileIntent()
    object AutoPullToRefresh: ProfileIntent()
    object StopForcePullToRefresh: ProfileIntent()

    object InitialData : ProfileIntent()
}