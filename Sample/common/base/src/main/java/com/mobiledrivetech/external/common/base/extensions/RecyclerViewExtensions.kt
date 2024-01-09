package com.mobiledrivetech.external.common.base.extensions

import android.widget.ListView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Get implementation manger that lays out items in a grid.
 *
 * @return Recycle view grid layout manager if configured, otherwise null.
 */
val RecyclerView.gridLayoutManager: GridLayoutManager?
    get() = layoutManager as? GridLayoutManager

/**
 * Get implementation manager which provides similar functionality [ListView].
 *
 * @return Recycle view linear layout manager if configured, otherwise null.
 */
val RecyclerView.linearLayoutManager: LinearLayoutManager?
    get() = layoutManager as? LinearLayoutManager

fun <T> RecyclerView.Adapter<*>.autoNotify(
    oldList: List<T>,
    newList: List<T>,
    compareItems: (T, T) -> Boolean = { old, new -> old == new },
    compareContents: (T, T) -> Boolean = { old, new -> old == new }
) {
    val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compareItems(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return compareContents(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size
    })

    diff.dispatchUpdatesTo(this)
}
