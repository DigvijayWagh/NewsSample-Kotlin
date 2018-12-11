package com.example.android.news

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread

class NewsRepository(private val newsDao: NewsDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allNews: LiveData<List<News>> = newsDao.getNews()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(news: News) {
        newsDao.insert(news)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun deleteAll() {
        newsDao.deleteAll()
    }

}
