package com.phooper.goodlooker.ui.widgets.recyclerview.adapter

import android.view.View
import com.example.delegateadapter.delegate.KDelegateAdapter
import com.phooper.goodlooker.R
import com.phooper.goodlooker.ui.widgets.recyclerview.model.OLItemViewModel
import com.phooper.goodlooker.util.makeLinks
import kotlinx.android.synthetic.main.item_h1.text
import kotlinx.android.synthetic.main.item_ol_li.*

class OLItemDelegateAdapter(private val onLinkClick: ((String) -> Unit)) :
    KDelegateAdapter<OLItemViewModel>() {

    override fun getLayoutId() = R.layout.item_ol_li

    override fun isForViewType(items: MutableList<*>, position: Int) =
        items[position] is OLItemViewModel

    override fun onBind(item: OLItemViewModel, viewHolder: KViewHolder) {
        with(viewHolder) {
            number.text = item.num
            text.text = item.text
            item.hyperlinks?.forEach { hyperlink ->
                text.makeLinks(Pair(hyperlink.text, View.OnClickListener {
                    onLinkClick(hyperlink.link)
                }))
            }
        }
    }
}