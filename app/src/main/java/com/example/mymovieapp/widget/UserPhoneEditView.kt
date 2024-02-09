package com.example.mymovieapp.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymovieapp.R
import com.example.mymovieapp.databinding.UserPhoneEditViewBinding
import com.example.mymovieapp.features.auth.ui.AuthenticationViewModel
import com.example.mymovieapp.features.auth.ui.adapter.PhoneCountryListAdapter
import com.example.mymovieapp.model.auth.CountryPhoneCodeModel
import com.example.mymovieapp.utils.EqualSpacingItemDecoration
import com.example.mymovieapp.utils.MyClickListeners
import com.example.mymovieapp.utils.extensions.dp
import com.example.mymovieapp.utils.extensions.toGone
import com.example.mymovieapp.utils.extensions.toVisible

class UserPhoneEditView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    val mBinding : UserPhoneEditViewBinding
    private val countrySelectionClickListener : MyClickListeners<CountryPhoneCodeModel>
    private val recyclerAdapter : PhoneCountryListAdapter

    private var flagButtonClicked = false

    init {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.user_phone_edit_view,
            this,
            true
        )

        countrySelectionClickListener = object : MyClickListeners<CountryPhoneCodeModel>{
            override fun click(item: CountryPhoneCodeModel) {
                mBinding.apply {
                    userPhoneCountryFlag.text = item.countryFlag
                    userPhoneNumber.editText?.hint = "(${item.countryPhoneCode})"
                    userPhoneCountryFlagRecyclerView.toGone()
                    dropdownButton.rotation = 0f
                }
            }

        }

        recyclerAdapter = PhoneCountryListAdapter(countrySelectionClickListener)

        dropDownButtonClick()
        dropDownButtonStatus()
        setupDropDownCountryList()
    }

    private fun setupDropDownCountryList() {
        mBinding.apply {
            userPhoneCountryFlagRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
            userPhoneCountryFlagRecyclerView.addItemDecoration(EqualSpacingItemDecoration(
                4.dp,
                EqualSpacingItemDecoration.VERTICAL
            ))
            userPhoneCountryFlagRecyclerView.adapter = recyclerAdapter
        }
    }

    fun setCountryList(newList : List<CountryPhoneCodeModel>) {
        recyclerAdapter.submitList(newList)
    }

    fun setViewModel(viewModel: AuthenticationViewModel){
        mBinding.viewModel = viewModel
    }

    private fun dropDownButtonStatus() {
        mBinding.apply {
            if (flagButtonClicked) {
                userPhoneCountryFlagRecyclerView.toVisible()
                dropdownButton.rotation = 180f
            } else {
                userPhoneCountryFlagRecyclerView.toGone()
                dropdownButton.rotation = 0f
            }
        }
    }

    private fun dropDownButtonClick() {
        mBinding.dropdownButton.setOnClickListener {
            flagButtonClicked = !flagButtonClicked
            dropDownButtonStatus()
        }
        mBinding.userPhoneCountryFlag.setOnClickListener {
            flagButtonClicked = !flagButtonClicked
            dropDownButtonStatus()
        }
    }
}