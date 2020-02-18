package com.timgortworst.roomy.presentation.features.event.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.timgortworst.roomy.data.model.Event

class EventItemDetailsLookup(private val recyclerView: RecyclerView) :
        ItemDetailsLookup<String>() {

    override fun getItemDetails(motionEvent: MotionEvent): ItemDetails<String>? =
        recyclerView.findChildViewUnder(motionEvent.x, motionEvent.y)?.let {
            return (recyclerView.getChildViewHolder(it) as? EventListAdapter.ViewHolder)?.getItemDetails()
        }
}