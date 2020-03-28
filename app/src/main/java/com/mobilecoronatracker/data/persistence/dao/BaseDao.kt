package com.mobilecoronatracker.data.persistence.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: T): Long

    @Insert
    suspend fun insert(vararg data: T)

    @Update
    suspend fun update(vararg data: T)

    @Delete
    suspend fun delete(vararg data: T)
}
