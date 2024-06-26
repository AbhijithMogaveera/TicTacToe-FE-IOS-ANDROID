package com.shared.profile.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDetailsResponse(
    val user_name:String,
    val bio:String,
    val profile_picture:String
)