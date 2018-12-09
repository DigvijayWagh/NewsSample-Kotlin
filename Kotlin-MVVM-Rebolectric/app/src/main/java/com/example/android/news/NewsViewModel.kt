package com.example.android.news

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: NewsRepository

    val allNews: LiveData<List<News>>

    init {
        val wordsDao = NewsRoomDatabase.getDatabase(application, scope).newsDao()
        repository = NewsRepository(wordsDao)
        allNews = repository.allNews
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */

    fun insert(news: News) = scope.launch(Dispatchers.IO) {
        repository.insert(news)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
