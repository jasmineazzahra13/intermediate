package com.dicoding.intermediate.data.md

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote")
data class Remote(
    @PrimaryKey val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)