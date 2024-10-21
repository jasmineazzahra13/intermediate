package com.dicoding.intermediate.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dicoding.intermediate.data.md.Remote
import com.dicoding.intermediate.data.md.Story

@Database(
    entities = [Story::class, Remote::class],
    version = 1,
    exportSchema = false
)

abstract class StoryDatabase : RoomDatabase() {
    abstract fun getStoryDao(): StoryDao
    abstract fun remoteDao(): RemoteDao
}