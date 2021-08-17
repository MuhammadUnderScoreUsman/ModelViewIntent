package com.mohammadosman.modelviewintent.usecase

import kotlinx.coroutines.flow.flow


object AccountUseCase {

    fun giveResult(userInput: String, onError: (String) -> String) = flow {
        if (userInput == "fruits") {
            for (itm in giveList) {
                kotlinx.coroutines.delay(700L)
                emit(itm)
            }
        } else {
            emit(
                onError("Nothing To Load")
            )
        }

    }

    private val giveList = listOf(
        "Bnana",
        "Apple",
        "Orange",
        "Water Melon",
        "Grapes",
        "Mango",
        "Peach",
        "Leachie",
    )


    fun getString() = flow {
        emit("fruits")
    }

}