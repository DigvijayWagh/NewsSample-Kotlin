package com.example.android.news

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "news_table")
data class News(@PrimaryKey @ColumnInfo(name = "title") val title:String,val description:String?, val imageHref:String?)
