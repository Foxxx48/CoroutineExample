package com.fox.coroutineexample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fox.coroutineexample.State
import com.fox.coroutineexample.data.ItemRepository
import com.fox.coroutineexample.domain.ItemRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ItemViewModel: ViewModel() {
    private val repository = ItemRepositoryImpl()

    private val loadingFlow = MutableSharedFlow<State.UiState>()


    val state =
        repository.getCurrencyListFlow
            .filter { it.isNotEmpty() }
            .map { State.UiState.Content(itemList = it)  }
            .onStart {
                State.UiState.Loading
            }
            .mergeWith(loadingFlow)

    fun <T> Flow<T>.mergeWith (another: Flow<T>) : Flow<T> {
        return  merge(this, another)
    }


    fun refreshList() {
        viewModelScope.launch {
            loadingFlow.emit((State.UiState.Loading))
            repository.refreshList()
        }
    }
}