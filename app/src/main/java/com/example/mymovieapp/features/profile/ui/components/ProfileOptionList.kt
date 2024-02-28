package com.example.mymovieapp.features.profile.ui.components


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mymovieapp.R
import timber.log.Timber

@Composable
fun ProfileOptionList(optionList: List<ProfileOption>) {
    LazyColumn(content = {
        itemsIndexed(optionList) { index: Int, item: ProfileOption ->
            ProfileOptionItem(profileOption = item)
            if (index != optionList.lastIndex) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(backgroundColor)
                )
            }
        }
    })
}

@Composable
fun ProfileOptionItem(
    profileOption: ProfileOption
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.Gray),
                onClick = {
                    profileOption.buttonClick.invoke()
                }
            )
    ) {
        Icon(
            painter = painterResource(id = profileOption.optionImage),
            contentDescription = stringResource(id = profileOption.optionName),
            tint = buttonIconColor,
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )
        ProfileText(
            text = stringResource(id = profileOption.optionName),
            textAttributes = profileOption.textAttributes,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        )

        Icon(
            painter = painterResource(id = R.drawable.forward_icon),
            contentDescription = "Click",
            tint = buttonIconColor,
            modifier = Modifier
                .padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
        )
    }
}

data class ProfileOption(
    @StringRes val optionName: Int,
    @DrawableRes val optionImage: Int,
    val buttonClick: () -> Unit = {
        Timber.tag("Utku").d(optionName.toString())
    },
    val textAttributes: TextAttributes = buttonTitleTextAttributes
)

val profileOptionList = listOf(
    ProfileOption(
        optionName = R.string.account,
        optionImage = R.drawable.ic_account
    ),
    ProfileOption(
        optionName = R.string.language,
        optionImage = R.drawable.translate_icon
    ),
    ProfileOption(
        optionName = R.string.dark_mode,
        optionImage = R.drawable.dark_mode_icon
    ),
    ProfileOption(
        optionName = R.string.terms_and_conditions,
        optionImage = R.drawable.ic_terms_and_conditions
    ),
    ProfileOption(
        optionName = R.string.faq,
        optionImage = R.drawable.ic_faq
    ),
    ProfileOption(
        optionName = R.string.log_out,
        optionImage = R.drawable.log_out_icon
    )
)