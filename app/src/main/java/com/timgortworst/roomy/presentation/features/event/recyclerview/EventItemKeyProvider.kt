package com.timgortworst.roomy.presentation.features.event.recyclerview

import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.widget.RecyclerView

class EventItemKeyProvider(private val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) : ItemKeyProvider<String>(SCOPE_MAPPED) {

    override fun getKey(position: Int) = (adapter as EventListAdapter).getEvent(position).eventId

    override fun getPosition(eventId: String) = (adapter as EventListAdapter).getPosition(eventId)
}