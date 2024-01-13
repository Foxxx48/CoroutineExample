package com.fox.coroutineexample.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fox.coroutineexample.State
import com.fox.coroutineexample.data.ItemRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ItemViewModel : ViewModel() {
    private val repository = ItemRepositoryImpl()

    private val loadingFlow = MutableSharedFlow<State.UiState>()


    val state =
        repository.getCurrencyListFlow
            .filter { it.isNotEmpty() }
            .map { State.UiState.Content(itemList = it) as State.UiState }

            .onStart {
                State.UiState.Loading
            }
            .catch {
                Log.d("CryptoApp", "LogD: state ViewModel")
                emit(State.UiState.Error(it, "OOPS state ViewModel"))
            }
            .mergeWith(loadingFlow)


    fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
        return merge(this, another)
    }


    fun refreshList() {
        viewModelScope.launch {
            loadingFlow.emit((State.UiState.Loading))
            repository.refreshList()
        }
    }
}