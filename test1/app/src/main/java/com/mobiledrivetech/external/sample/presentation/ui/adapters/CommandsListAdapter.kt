package com.mobiledrivetech.external.sample.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mobiledrivetech.external.sample.R
import com.mobiledrivetech.external.sample.databinding.ItemLayoutBinding
import com.mobiledrivetech.external.sample.presentation.ui.models.DisplayedCommand
import com.mobiledrivetech.external.sample.presentation.ui.viewholders.BindingHolder

class CommandsListAdapter(
    private val list: List<DisplayedCommand>,
    private var itemClickListener: ItemClickListener
) : RecyclerView.Adapter<BindingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val binding = DataBindingUtil.inflate<ItemLayoutBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        holder.bindData(list[position])
        (holder as? BindingHolder)?.bindEvent(itemClickListener)
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int): Int = R.layout.item_layout

    fun interface ItemClickListener {
        fun onItemClickListener(item: DisplayedCommand)
    }
}
