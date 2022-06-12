package com.chesire.pushie.datasource.pwpush.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PushedEntity(
    @PrimaryKey
    val id: String,
    val url: String,
    val createdAt: String
)
