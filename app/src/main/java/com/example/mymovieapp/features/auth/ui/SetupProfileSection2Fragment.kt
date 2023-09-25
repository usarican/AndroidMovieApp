package com.example.mymovieapp.features.auth.ui

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseFragment
import com.example.mymovieapp.databinding.FragmentSetupProfileSection2Binding
import com.example.mymovieapp.utils.PathHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SetupProfileSection2Fragment : BaseFragment<FragmentSetupProfileSection2Binding>(R.layout.fragment_setup_profile_section2) {

    val viewModel : AuthenticationViewModel by activityViewModels()

    @Inject
    lateinit var pathHelper: PathHelper

    override fun setUpUI() {
        binding.apply {
            val countryList = pathHelper.getCountryPhoneCode() ?: emptyList()
            userPhoneEditView.setCountryList(countryList)
        }
        setupGenreListEditText()
    }

    private fun setupGenreListEditText() {
        val genreList = listOf("Male","Female")
        val adapter = ArrayAdapter(requireContext(),R.layout.user_genre_list_item,genreList)
        (binding.setupSection2UserGenre.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}