package com.example.mymovieapp.features.profile.ui.components


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import androidx.wear.compose.material.Text
import com.example.mymovieapp.R
import com.example.mymovieapp.features.profile.data.TextAttributes
import com.example.mymovieapp.features.profile.data.buttonTitleTextAttributes
import com.example.mymovieapp.features.profile.ui.ProfileViewModel
import timber.log.Timber

@Composable
fun ProfileOptionList(
    optionList: List<ProfileOption>,
    viewModel: ProfileViewModel
) {
    LazyColumn(content = {
        itemsIndexed(optionList) { index: Int, item: ProfileOption ->
            ProfileOptionItem(
                profileOption = item,
                onClickItem = {
                    Timber.tag("Utku").d("Item Clicked $it")
                    viewModel.changeBottomSheetState(true)
                }
            )
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
    profileOption: ProfileOption,
    onClickItem: (String) -> Unit
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
                    onClickItem(profileOption.optionType.name)
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

        when (profileOption.optionType) {
            OptionType.ACCOUNT, OptionType.TERM, OptionType.FAQ -> {
                Icon(
                    painter = painterResource(id = R.drawable.forward_icon),
                    contentDescription = "Click",
                    tint = buttonIconColor,
                    modifier = Modifier
                        .padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                )
            }

            OptionType.LANGUAGE, OptionType.DARK_MODE -> {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 2.dp)
                        .border(width = 1.5.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp))
                ) {
                    Row {
                        Icon(
                            painter = painterResource(
                                id = R.drawable.translate_icon
                            ),
                            tint = buttonIconColor,
                            contentDescription = "translate_icon",
                            modifier = Modifier.padding(vertical = 4.dp).padding(start = 8.dp, end = 4.dp)
                        )

                        Text(
                            text = "English",
                            style = MaterialTheme.typography.subtitle1,
                            color = buttonIconColor,
                            modifier = Modifier.padding(vertical = 4.dp).padding(end = 8.dp)
                        )
                    }

                }
            }
            else  ->{
                // No Option
            }
        }
    }
}

data class ProfileOption(
    val optionType: OptionType,
    @StringRes val optionName: Int,
    @DrawableRes val optionImage: Int,
    val textAttributes: TextAttributes = buttonTitleTextAttributes
)

enum class OptionType {
    ACCOUNT, LANGUAGE, DARK_MODE, TERM, FAQ, LOG_OUT
}

val profileOptionList = listOf(
    ProfileOption(
        optionType = OptionType.ACCOUNT,
        optionName = R.string.account,
        optionImage = R.drawable.ic_account
    ),
    ProfileOption(
        optionType = OptionType.LANGUAGE,
        optionName = R.string.language,
        optionImage = R.drawable.translate_icon
    ),
    ProfileOption(
        optionType = OptionType.DARK_MODE,
        optionName = R.string.dark_mode,
        optionImage = R.drawable.dark_mode_icon
    ),
    ProfileOption(
        optionType = OptionType.TERM,
        optionName = R.string.terms_and_conditions,
        optionImage = R.drawable.ic_terms_and_conditions
    ),
    ProfileOption(
        optionType = OptionType.FAQ,
        optionName = R.string.faq,
        optionImage = R.drawable.ic_faq
    ),
    ProfileOption(
        optionType = OptionType.LOG_OUT,
        optionName = R.string.log_out,
        optionImage = R.drawable.log_out_icon
    )
)