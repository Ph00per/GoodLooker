package com.phooper.goodlooker.ui.global.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phooper.goodlooker.R
import com.phooper.goodlooker.entity.FavouritePost
import kotlinx.android.synthetic.main.item_favourite_post.view.*

class FavouritePostItemAdapter :
    RecyclerView.Adapter<FavouritePostItemAdapter.FavouritePostViewHolder>() {
    var dataList = mutableListOf<FavouritePost>()
    var onItemClick: ((String) -> Unit)? = null

    inner class FavouritePostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(dataList[adapterPosition].postLink)
            }
        }

        val title: TextView = itemView.post_title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FavouritePostViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_favourite_post,
                parent,
                false
            )
        )

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: FavouritePostViewHolder, position: Int) {
        holder.title.text = dataList[position].postTitle
    }

    fun setData(list: List<FavouritePost>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

}