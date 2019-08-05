package com.timgortworst.roomy.ui.user.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.timgortworst.roomy.R
import com.timgortworst.roomy.customview.BottomSheetMenu
import com.timgortworst.roomy.model.BottomMenuItem
import com.timgortworst.roomy.model.User
import com.timgortworst.roomy.ui.main.view.MainActivity
import com.timgortworst.roomy.ui.user.adapter.UserListAdapter
import com.timgortworst.roomy.ui.user.presenter.UserListPresenter
import com.timgortworst.roomy.utils.isAirplaneModeEnabled
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_recycler_view.*
import kotlinx.android.synthetic.main.fragment_recycler_view.view.*
import kotlinx.android.synthetic.main.layout_list_state.view.*
import javax.inject.Inject


class UserListFragment : Fragment(), UserListView {
    private lateinit var userListAdapter: UserListAdapter
    private lateinit var activityContext: MainActivity
    private var recyclerView: RecyclerView? = null

    @Inject lateinit var presenter: UserListPresenter

    companion object {
        fun newInstance(): UserListFragment {
            return UserListFragment()
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        activityContext = (activity as? MainActivity) ?: return
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_recycler_view, container, false)
        userListAdapter = UserListAdapter(object : UserListAdapter.OnUserLongClickListener {
            override fun onUserClick(user: User) {
                presenter.showContextMenuIfUserHasPermission(user)
            }
        })
        recyclerView = view.recycler_view
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe_container?.isEnabled = false

        listenToUsers(activityContext.isAirplaneModeEnabled())

        recyclerView?.apply {
            val linearLayoutManager = LinearLayoutManager(activityContext)
            val dividerItemDecoration = DividerItemDecoration(activityContext, linearLayoutManager.orientation)
            layoutManager = linearLayoutManager
            adapter = userListAdapter
            addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachUserListener()
    }

    fun listenToUsers(isAirplaneModeEnabled: Boolean) {
        presenter.listenToUsers(isAirplaneModeEnabled)
    }

    override fun showContextMenuFor(user: User) {
        var bottomSheetMenu: BottomSheetMenu? = null

        val items = arrayListOf(
                BottomMenuItem(R.drawable.ic_delete, "Delete") {
                    presenter.deleteUser(user)
                    bottomSheetMenu?.dismiss()
                }
        )
        bottomSheetMenu = BottomSheetMenu(activityContext, user.name, items)
        bottomSheetMenu.show()
    }

    override fun presentEditedUser(user: User) {
        userListAdapter.updateUser(user)
    }

    override fun presentDeletedUser(user: User) {
        userListAdapter.removeUser(user)
    }

    override fun presentAddedUser(user: User) {
        userListAdapter.addUser(user)
    }

    override fun setLoadingView(isLoading: Boolean) {
        swipe_container?.isRefreshing = isLoading
    }

    override fun setErrorView(isVisible: Boolean, title: Int?, text: Int?) {
        layout_list_state_error?.apply {
            title?.let { this.state_title.text = activityContext.getString(it) }
            text?.let { this.state_message.text = activityContext.getString(it) }
            visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }
}
