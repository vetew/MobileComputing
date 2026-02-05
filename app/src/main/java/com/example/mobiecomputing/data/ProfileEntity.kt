package com.example.mobiecomputing.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String,
    val imageUri: String? = null
)
