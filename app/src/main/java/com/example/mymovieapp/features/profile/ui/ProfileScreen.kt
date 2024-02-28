package com.example.mymovieapp.features.profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mymovieapp.R
import com.example.mymovieapp.features.profile.ui.components.ProfileText
import com.example.mymovieapp.features.profile.ui.components.UserAvatarImage
import com.example.mymovieapp.features.profile.ui.components.subTitle1TextAttributes
import com.example.mymovieapp.features.profile.ui.components.titleTextAttributes

@Preview(showBackground = true)
@Composable
fun ProfileScreen() {
    var userName by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserAvatarImage("https://firebasestorage.googleapis.com/v0/b/mymovieappdb.appspot.com/o/users%2FuserImages%2FB94NH4uJFyVLmDyGQun3xuLI20C3%2FprofilePicture.jpg?alt=media&token=733df820-deb0-4fc7-b83d-938e9ddbb665")
        ProfileText(
            text = "Utku Sarıçan",
            textAttributes = titleTextAttributes,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )
        ProfileText(
            text = "@utkusarican",
            textAttributes = subTitle1TextAttributes,
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )

        UserNameField(userName = userName)
        ChangeUserNameButton(
            onChanged = {
            userName = "Utku Sarican"
        })
    }
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