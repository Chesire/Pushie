package com.chesire.pushie.datasource.pwpush.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chesire.pushie.datasource.pwpush.local.entity.PushedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PushedDao {

    @Query("SELECT * FROM PushedEntity")
    fun flowAll(): Flow<List<PushedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: PushedEntity)
}
