package com.example.mymovieapp.features.profile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.mymovieapp.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserAvatarImage(
    userPhotoUri : String
){
    GlideImage(
        model = userPhotoUri,
        contentDescription = "userAvatar",
        loading = placeholder(R.drawable.user_photo),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(128.dp)
            .clip(CircleShape)
            .border(1.dp, Color.Black, CircleShape)
    )
}