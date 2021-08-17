package com.mohammadosman.modelviewintent.presentation.viewmodel.mvi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammadosman.modelviewintent.presentation.viewmodel.mvi.ProfileIntent.*
import com.mohammadosman.modelviewintent.presentation.viewmodel.mvi.ProfileResult.*
import com.mohammadosman.modelviewintent.presentation.util.Resource
import com.mohammadosman.modelviewintent.presentation.util.Resource.*
import com.mohammadosman.modelviewintent.presentation.util.TAG
import com.mohammadosman.modelviewintent.usecase.NewFeedsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.Error

class ProfileViewModelMvi : ViewModel() {

    private val repo = NewFeedsUseCase()

    private val _uiState = MutableLiveData<ProfileState>()
    val uiState: LiveData<ProfileState> get() = _uiState

    private val _refreshTrigger = Channel<Refresh>()
    private val refreshTrigger = _refreshTrigger.receiveAsFlow()

    private val PULL_TO_REFRESH_LIMIT = 3

    var pullToRefreshInitialization = 1


    private val _sendIntent = Channel<ProfileIntent>()


    init {
        processIntent()
    }

    fun sendIntent(intent: ProfileIntent) {
        viewModelScope.launch {
            _sendIntent.send(intent)
        }
    }

    private fun onReceivedResult(result: ProfileResult) {
        when (result) {

            ResultLoading -> {
                loadingState(true)
            }

            is StringResult -> {
                updateValue(
                    value = result.res ?: "",
                    loadingStatus = false,
                )
            }

            ResultError -> {
            }
        }
    }

    private fun processIntent() {
        _sendIntent.receiveAsFlow().onEach { intent ->
            when (intent) {

                ForcePullToRefresh -> {
                    pullRefresh(Refresh.FORCE)
                }

                AutoPullToRefresh -> {
                    pullRefresh(Refresh.AUTO)
                }

                InitialData -> {
                    loadFromRepository()
                }

                StopForcePullToRefresh -> {
                    stopPullToRefresh()
                }

            }
        }.launchIn(viewModelScope)
    }


    private fun pullRefresh(refresh: Refresh) {
        viewModelScope.launch {
            if (refresh == Refresh.FORCE) {
                if (getInitPull() == getLimit()
                    && refresh == Refresh.FORCE
                ) {
                    Log.d(TAG, "FORCE_REF_IF")
                    sendIntent(intent = StopForcePullToRefresh)
                } else {
                    Log.d(TAG, "FORCE_REF_ELSE")
                    //pullToRefreshInitialization++
                    increment()
                    _refreshTrigger.send(refresh)
                }
            } else {
                Log.d(TAG, "AUTO_REF")
                _refreshTrigger.send(refresh)
            }
        }
    }

    private fun getInitPull(): Int {
        return reducer().initialForceToRefresh
    }

    private fun getLimit(): Int {
        return reducer().exhaustiveForceToRefresh
    }

    private fun increment() {
        updateUiState(
            reducer()
                .copy(
                    initialForceToRefresh = getInitPull()
                        .plus(1)
                )
        )
    }

    private fun stopPullToRefresh() {
        Log.d(TAG, "STOP PULL TO REF-----")
        updateUiState(
            reducer().copy(
                pullToRefLimitationError =
                "Enough Loading, You wanna do all Day?"
            )
        )
    }

    private fun loadFromRepository() {
        refreshTrigger.flatMapLatest { refresh ->
            repo.requestForData(
                forceRefresh = refresh == Refresh.AUTO
            )
        }.onEach { res ->
            resourceResponse(res)
        }.launchIn(viewModelScope)
    }

    private fun loadingState(state: Boolean) {
        updateUiState(
            reducer().copy(loadingState = state)
        )
    }

    private fun updateValue(value: String, loadingStatus: Boolean) {
        updateUiState(
            reducer().copy(
                data = value,
                loadingState = loadingStatus,
                pullToRefLimitationError = null
            )
        )
    }


    private fun updateUiState(profileState: ProfileState) {
        _uiState.value = profileState
    }

    private fun reducer(): ProfileState {
        return _uiState.value ?: ProfileState()
    }

    enum class Refresh { AUTO, FORCE }

    private fun resourceResponse(resource: Resource<String>) {
        when (resource) {
            is Success -> {
                onReceivedResult(
                    result = StringResult(
                        resource.data ?: ""
                    )
                )
            }

            is Error -> {
            }
            is Loading -> {
                onReceivedResult(result = ResultLoading)
            }

        }
    }

}