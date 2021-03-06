package com.timgortworst.tasqs.presentation.features.user

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.timgortworst.tasqs.R
import com.timgortworst.tasqs.domain.model.response.Response
import com.timgortworst.tasqs.domain.model.User

@BindingAdapter("dataVisibility")
fun ViewGroup.dataVisibility(responseState: Response<List<User>>?) {
    visibility = if (responseState is Response.Success) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["userListLoading", "userRemovalLoading"], requireAll = false)
fun ProgressBar.loadingVisibility(
    userListResponse: Response<List<User>>?,
    userRemovalResponse: Response<String>?
) {
    visibility = if (userListResponse is Response.Loading ||
        userRemovalResponse is Response.Loading) View.VISIBLE else View.GONE
}

@BindingAdapter("adminLabelVisibility")
fun TextView.adminLabelVisibility(isAdmin: Boolean) {
    visibility = if (isAdmin) View.VISIBLE else View.GONE
}

@BindingAdapter("messageVisibility")
fun TextView.messageVisibility(responseState: Response<List<User>>?) {
    visibility = if (responseState is Response.Error) View.VISIBLE else View.GONE
    (responseState as? Response.Error)?.let {
        text = context.getString(R.string.error_generic)
    }
}

@BindingAdapter("users")
fun RecyclerView.setUsers(responseState: Response<List<User>>?) {
    (responseState as? Response.Success)?.let {
        (adapter as UserAdapter).addAll(it.data!!)
    }
}
