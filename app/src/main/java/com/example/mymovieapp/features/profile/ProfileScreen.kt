package com.example.mymovieapp.features.profile

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymovieapp.R

@Preview(showBackground = true)
@Composable
fun ProfileScreen() {
    var userName by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .clickable {
                println("Clicked.")
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserAvatarImage()
        Text(text = "Profile Screen Compose View")
        Spacer(modifier = Modifier
            .height(16.dp)
            .fillMaxWidth())
        UserNameField(userName = userName)
        ChangeUserNameButton(
            onChanged = {
            userName = "Utku Sarican"
        })
    }
}

@Composable
fun UserAvatarImage() {
    Image(
        painter = painterResource(id = R.drawable.user_photo),
        contentDescription = "userAvatar",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(128.dp)
            .clip(CircleShape)
            .border(1.dp, Color.Black, CircleShape)
    )
}

@Composable
fun UserNameField(userName: String) {
    Text(text = userName)
}

@Composable
fun ChangeUserNameButton(onChanged: () -> Unit) {
    Button(onClick = { onChanged.invoke() }) {
        Text(text = "Change The Name")
    }
}