package com.timgortworst.roomy.ui.event.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.timgortworst.roomy.model.User


class SpinnerUserAdapter(
    private val ctx: Context,
    private val textViewResourceId: Int,
    private val userList: MutableList<User>
) : ArrayAdapter<User>(ctx, textViewResourceId, userList) {

    override fun getCount(): Int {
        return userList.size
    }

    override fun getItem(position: Int): User? {
        return userList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val label = super.getView(position, convertView, parent) as TextView
        label.text = userList[position].name
        return label
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.text = userList[position].name

        return label
    }
}