package com.dicoding.intermediate.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.intermediate.data.md.Remote

@Dao
interface RemoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remote: List<Remote>)

    @Query("SELECT * FROM remote WHERE id = :id")
    suspend fun getRemoteId(id: String): Remote?

    @Query("DELETE FROM remote")
    suspend fun deleteRemote()
}