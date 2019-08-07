package com.timgortworst.roomy.ui.event.presenter

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.timgortworst.roomy.domain.EventInteractor
import com.timgortworst.roomy.model.Category
import com.timgortworst.roomy.model.EventMetaData
import com.timgortworst.roomy.model.User
import com.timgortworst.roomy.ui.event.view.EventEditView
import com.timgortworst.roomy.utils.CoroutineLifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class EventEditPresenter @Inject constructor(
        private val view: EventEditView,
        private val eventInteractor: EventInteractor
) : DefaultLifecycleObserver {

    private val scope = CoroutineLifecycleScope(Dispatchers.Main)

    init {
        if (view is LifecycleOwner) {
            view.lifecycle.addObserver(scope)
        }
    }

    fun createOrUpdateEvent(
            eventId: String?,
            category: Category,
            user: User,
            eventMetaData: EventMetaData
    ) = scope.launch {
        if (!eventId.isNullOrEmpty()) {
            eventInteractor.updateEvent(eventId, eventMetaData, category, user)
        } else {
            val householdId = eventInteractor.getHouseholdIdForUser()
            eventInteractor.createEvent(eventMetaData, category, user, householdId)
        }
    }

    fun getUsers() = scope.launch {
        val userList = eventInteractor.getUserListForCurrentHousehold()
        view.presentUserList(userList?.toMutableList() ?: mutableListOf())
    }

    fun getCategories() = scope.launch {
        val categories = eventInteractor.getCategories()
        view.presentCategoryList(categories.toMutableList())
    }

    fun formatDate(year: Int, month: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val formattedDayOfMonth = cal.get(Calendar.DAY_OF_MONTH).toString()
        val formattedMonth = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val formattedYear = cal.get(Calendar.YEAR).toString()

        view.presentFormattedDate(formattedDayOfMonth, formattedMonth, formattedYear)
    }


    companion object {
        private const val TAG = "EventEditPresenter"
    }
}
