package com.mohammadosman.modelviewintent.usecase

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import kotlin.collections.HashMap

class NewFeedsUseCase {


    private val dataSource: HashMap<String, String> = HashMap()
    var id = ""

    fun showNewFeeds(
        userInput: String? = "",
        onSuccess: (String) -> Unit,
        onFailed: (String) -> Unit
    ) {
        if (userInput == "NewFeeds") {
            onSuccess("Success Fully loaded New Feeds.")
        } else {
            onFailed(
                "Failed to loadAnything."
            )
        }
    }


    fun requestForData(forceRefresh: Boolean) = networkBoundResource(
        query = {
            returnFromLocal()
        },
        fetch = {
            fetchFromNetwork()
        },
        saveFetchResult = { data ->
            insertIntoLocal(data)
        },
        shouldFetch = { data ->
            if (forceRefresh) {
                true
            } else {
                true
            }
        }
    )


    private suspend fun fetchFromNetwork(): String {
        delay(2_000)
        return "Hey there!! im coming from network"
    }


    private fun insertIntoLocal(string: String) {
        id = UUID.randomUUID().toString()
        dataSource.put(id, string)
    }


    private fun returnFromLocal(): Flow<String> {
        return flow {
            val data = dataSource.get(id) ?: "NO STRINGS."
            emit(data)
        }
    }


}
