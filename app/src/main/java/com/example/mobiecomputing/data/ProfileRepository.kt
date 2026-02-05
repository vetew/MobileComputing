package com.example.mobiecomputing.data

import android.content.Context
import android.net.Uri
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class ProfileSettings(
    val name: String,
    val imageUri: String?
)

class ProfileRepository(private val context: Context) {
    private val database: ProfileDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            ProfileDatabase::class.java,
            "profile.db"
        ).allowMainThreadQueries().build()
    }

    private val profileDao: ProfileDao by lazy { database.profileDao() }

    val profileFlow: Flow<ProfileSettings> = profileDao.observeProfile().map { profile ->
        ProfileSettings(
            name = profile?.name.orEmpty(),
            imageUri = profile?.imageUri
        )
    }

    suspend fun updateName(name: String) {
        val current = profileDao.getProfile()
        profileDao.upsert(
            ProfileEntity(
                name = name,
                imageUri = current?.imageUri
            )
        )
    }

    suspend fun updateImageUri(uri: Uri) {
        val current = profileDao.getProfile()
        profileDao.upsert(
            ProfileEntity(
                name = current?.name.orEmpty(),
                imageUri = uri.toString()
            )
        )
    }
}
