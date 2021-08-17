package com.mohammadosman.modelviewintent.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammadosman.modelviewintent.presentation.util.Resource
import com.mohammadosman.modelviewintent.usecase.NewFeedsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class NewFeedsViewModel : ViewModel() {

    private val newFeedsUseCase = NewFeedsUseCase()

    private val _handleUiEvents = Channel<UiEvents>()
    val handleUiEvents = _handleUiEvents.receiveAsFlow()

    private val _refreshTrigger = Channel<Refresh>()
    private val refreshTrigger = _refreshTrigger.receiveAsFlow()

    fun response(
        userInput: String? = ""
    ) {
        newFeedsUseCase.showNewFeeds(
            userInput = userInput,
            onSuccess = {
                sendEvents(
                    UiEvents.ThrowResponseMsg(
                        msg = it
                    )
                )
            },
            onFailed = {
                sendEvents(
                    UiEvents.ThrowResponseMsg(
                        msg = it
                    )
                )
            }
        )
    }


    private fun sendEvents(events: UiEvents) {
        viewModelScope.launch {
            _handleUiEvents.send(events)
        }
    }

    val newFeeds = refreshTrigger.flatMapLatest { refresh ->
        newFeedsUseCase.requestForData(
            refresh == Refresh.FORCE
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun forceRefresh() {
        if (newFeeds.value !is Resource.Loading) {
            viewModelScope.launch {
                _refreshTrigger.send(Refresh.FORCE)
            }
        }
    }

    fun onStart() {
        if (newFeeds.value !is Resource.Loading) {
            viewModelScope.launch {
                _refreshTrigger.send(Refresh.AUTO)
            }
        }
    }


    sealed class UiEvents {
        data class ThrowResponseMsg(
            val msg: String
        ) : UiEvents()
    }

    enum class Refresh {
        FORCE, AUTO
    }


}