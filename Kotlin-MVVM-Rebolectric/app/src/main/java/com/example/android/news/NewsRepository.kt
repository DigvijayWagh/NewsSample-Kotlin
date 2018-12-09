package com.example.android.news

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread

class NewsRepository(private val wordDao: NewsDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNews: LiveData<List<News>> = wordDao.getNews()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(news: News) {
        wordDao.insert(news)
    }
}
