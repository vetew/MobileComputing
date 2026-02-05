package com.example.mobiecomputing.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE id = 0")
    fun observeProfile(): Flow<ProfileEntity?>

    @Query("SELECT * FROM profile WHERE id = 0")
    suspend fun getProfile(): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(profile: ProfileEntity)

    @Query("UPDATE profile SET name = :name WHERE id = 0")
    suspend fun updateName(name: String)

    @Query("UPDATE profile SET imageUri = :imageUri WHERE id = 0")
    suspend fun updateImageUri(imageUri: String?)
}
