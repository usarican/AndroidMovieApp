package com.example.mymovieapp.features.profile.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mymovieapp.features.profile.ui.components.ProfileBottomSheet
import com.example.mymovieapp.features.profile.ui.components.ProfileOptionList
import com.example.mymovieapp.features.profile.ui.components.ProfileText
import com.example.mymovieapp.features.profile.ui.components.UserAvatarImage
import com.example.mymovieapp.features.profile.ui.components.profileOptionList
import com.example.mymovieapp.features.profile.data.subTitle1TextAttributes
import com.example.mymovieapp.features.profile.data.titleTextAttributes

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    
    val bottomSheetState by viewModel.bottomSheetState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserAvatarImage("https://firebasestorage.googleapis.com/v0/b/mymovieappdb.appspot.com/o/users%2FuserImages%2FB94NH4uJFyVLmDyGQun3xuLI20C3%2FprofilePicture.jpg?alt=media&token=733df820-deb0-4fc7-b83d-938e9ddbb665")
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(16.dp))
        ProfileText(
            text = "Utku Sarıçan",
            textAttributes = titleTextAttributes,
            modifier = Modifier.fillMaxWidth()
        )
        ProfileText(
            text = "@utkusarican",
            textAttributes = subTitle1TextAttributes,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
        )
        ProfileOptionList(profileOptionList,viewModel)

        if(bottomSheetState){
            ProfileBottomSheet() {
                viewModel.changeBottomSheetState(false)
            }
        }

    }
}

