package com.example.mymovieapp.features.auth.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.mymovieapp.R
import com.example.mymovieapp.core.ui.BaseAdapter
import com.example.mymovieapp.databinding.PhoneCountryListItemBinding
import com.example.mymovieapp.model.auth.CountryPhoneCodeModel
import com.example.mymovieapp.utils.MyClickListeners

class PhoneCountryListAdapter(
    private val countrySelectionClickListener: MyClickListeners<CountryPhoneCodeModel>
) : BaseAdapter<CountryPhoneCodeModel,PhoneCountryListItemBinding>(COUNTRY_DIFF_UTIL) {


    companion object {
        private val COUNTRY_DIFF_UTIL = object : DiffUtil.ItemCallback<CountryPhoneCodeModel>(){
            override fun areItemsTheSame(
                oldItem: CountryPhoneCodeModel,
                newItem: CountryPhoneCodeModel
            ): Boolean {
                return oldItem.countryCode == newItem.countryCode
            }

            override fun areContentsTheSame(
                oldItem: CountryPhoneCodeModel,
                newItem: CountryPhoneCodeModel
            ): Boolean {
                return oldItem.countryCode == newItem.countryCode
            }

        }
    }

    override fun getResourceId(): Int = R.layout.phone_country_list_item

    override fun createHolderInstance(binding: PhoneCountryListItemBinding): BaseViewHolder {
        return object : BaseViewHolder(binding){
            override fun bind(model: CountryPhoneCodeModel) {
                binding.countryModel = model
                binding.root.setOnClickListener {
                    countrySelectionClickListener.click(model)
                }
            }
        }
    }

}