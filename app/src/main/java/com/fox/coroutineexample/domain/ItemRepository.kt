package com.fox.coroutineexample.domain

interface ItemRepository {
    fun loadData()

    //    fun getCurrencyListFlow(): StateFlow<List<Item>>
    suspend fun refreshList()
}