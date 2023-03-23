package com.example.bretterverse.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var userId: String? = "",
    var username: String? = "",
    var email: String? = "",
    var bio: String? = "",
    var imageUrl: String? = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "userId" to userId,
            "username" to username,
            "email" to email,
            "bio" to bio,
            "imageUrl" to imageUrl
        )
    }
}
