package com.timgortworst.roomy.ui.event.presenter

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.timgortworst.roomy.model.Category
import com.timgortworst.roomy.model.EventMetaData
import com.timgortworst.roomy.model.User
import com.timgortworst.roomy.repository.EventRepository
import com.timgortworst.roomy.repository.UserRepository
import com.timgortworst.roomy.ui.event.view.EventEditView
import com.timgortworst.roomy.utils.CoroutineLifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class EventEditPresenter(
    private val view: EventEditView,
    private val agendaRepository: EventRepository,
    private val userRepository: UserRepository
) : DefaultLifecycleObserver {

    private val scope = CoroutineLifecycleScope(Dispatchers.Main)

    init {
        if (view is LifecycleOwner) {
            view.lifecycle.addObserver(scope)
        }
    }

    fun insertOrUpdateEvent(
        eventId: String,
        category: Category,
        user: User,
        eventMetaData: EventMetaData,
        isDone: Boolean = false
    ) = scope.launch {

        if (eventId.isNotBlank()) {
            agendaRepository.updateAgendaEvent(eventId, category, user, eventMetaData, isDone)
        } else {
            agendaRepository.insertAgendaEvent(category, user, eventMetaData, isDone)
        }
    }

    fun fetchUsers() = scope.launch {
        val userList = userRepository.getUsersForHouseholdId(userRepository.getHouseholdIdForCurrentUser())
        view.presentUserList(userList.toMutableList())
    }

    fun fetchCategories() = scope.launch {
        val categories = agendaRepository.getCategories()
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
        private const val TAG = "EditAgendaEventPresenter"
    }
}
