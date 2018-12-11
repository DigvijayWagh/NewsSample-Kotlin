package com.example.android.news

import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants=BuildConfig::class, packageName = "com.example.android.news")
class ExampleUnitTest {
    var mainActivity:MainActivity= null!!;
    @Before
    fun init(){
        mainActivity = Robolectric.setupActivity(MainActivity::class.java)
    }

    @Test
    fun checkHeading(){
        val textView=mainActivity.title

        val stringValue=textView.toString()

        assertThat(stringValue,equalTo("NEWS"))
    }

}