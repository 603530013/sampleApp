package com.mobiledrivetech.external.sample.presentation.ui.viewholders

import androidx.recyclerview.widget.RecyclerView
import com.mobiledrivetech.external.sample.databinding.ItemLayoutBinding
import com.mobiledrivetech.external.sample.presentation.ui.adapters.CommandsListAdapter
import com.mobiledrivetech.external.sample.presentation.ui.models.DisplayedCommand

class BindingHolder(private var binding: ItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindData(item: DisplayedCommand) {
        binding.item = item
    }

    fun bindEvent(event: CommandsListAdapter.ItemClickListener) {
        binding.itemClickListener = event
    }
}
