package com.fox.coroutineexample

import com.fox.coroutineexample.data.Item

sealed interface State {
    sealed class UiState {

        data class Content(
            val itemList : List<Item>
        ) : UiState()

        object Loading : UiState()

        object Initial : UiState()

        class Error(
           val throwable: Throwable,
            val message: String
        ) : UiState()

        object Empty : UiState()
    }
}