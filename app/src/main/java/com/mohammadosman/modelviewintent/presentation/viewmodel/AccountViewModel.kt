package com.mohammadosman.modelviewintent.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammadosman.modelviewintent.usecase.AccountUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {

    private val userInput: Flow<String> =
        AccountUseCase.getString().map { input ->
            input
        }

    val result = userInput.flatMapLatest { input ->
        AccountUseCase.giveResult(input) {
            receiveEvent(it)
            ""
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(1000),
        initialValue = null
    )


    private val _receiveEvent = Channel<String>()
    val receiveEvent: Flow<String> get() = _receiveEvent.receiveAsFlow()


    private fun receiveEvent(event: String) {
        viewModelScope.launch {
            _receiveEvent.send(event)
        }
    }
}