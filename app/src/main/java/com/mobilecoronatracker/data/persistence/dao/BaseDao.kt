package com.mobilecoronatracker.data.persistence.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    fun insert(data: T)

    @Insert
    fun insert(vararg data: T)

    @Update
    fun update(vararg data: T)

    @Delete
    fun delete(vararg data: T)
}
