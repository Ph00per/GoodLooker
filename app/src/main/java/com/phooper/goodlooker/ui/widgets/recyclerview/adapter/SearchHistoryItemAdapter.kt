package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phooper.goodlooker.R
import com.phooper.goodlooker.db.entity.SearchHistory
import kotlinx.android.synthetic.main.item_search_history.view.*

class SearchHistoryItemAdapter :
    RecyclerView.Adapter<SearchHistoryItemAdapter.SearchViewHolder>() {
    var dataList = mutableListOf<SearchHistory>()
    var onItemClick: ((String) -> Unit)? = null
    var onDeleteBtnClick: ((Int, Int) -> Unit)? = null

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(dataList[adapterPosition].text)
            }
            itemView.delete_btn.setOnClickListener {
                onDeleteBtnClick?.invoke(dataList[adapterPosition].id, adapterPosition)
            }
        }

        val searchedText: TextView = itemView.history_item_textview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_search_history,
                parent,
                false
            )
        )

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.searchedText.text = dataList[position].text
    }

    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setData(list: List<SearchHistory>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

}