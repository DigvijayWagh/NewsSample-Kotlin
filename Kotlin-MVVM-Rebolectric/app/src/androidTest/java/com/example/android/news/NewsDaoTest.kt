package com.example.android.news


import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class NewsDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var newsDao: NewsDao
    private lateinit var db: NewsRoomDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, NewsRoomDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        newsDao = db.newsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetnews() {
        val news = News("news","test","http://www.donegalhimalayans.com/images/That%20fish%20was%20this%20big.jpg")
        newsDao.insert(news)
        val allnews = newsDao.getNews().waitForValue()
        assertEquals(allnews[0].title, news.title)
    }

    @Test
    @Throws(Exception::class)
    fun getAllnews() {
        val news = News("aaa","test","http://www.donegalhimalayans.com/images/That%20fish%20was%20this%20big.jpg")
        newsDao.insert(news)
        val news2 = News("bbb","test2","http://www.donegalhimalayans.com/images/That%20fish%20was%20this%20big.jpg")
        newsDao.insert(news2)
        val allnews = newsDao.getNews().waitForValue()
        assertEquals(allnews[0].title, news.title)
        assertEquals(allnews[1].title, news2.title)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() {
        val news = News("word","","")
        newsDao.insert(news)
        val news2 = News("news2","","")
        newsDao.insert(news2)
        newsDao.deleteAll()
        val allnews = newsDao.getNews().waitForValue()
        assertTrue(allnews.isEmpty())
    }
}
