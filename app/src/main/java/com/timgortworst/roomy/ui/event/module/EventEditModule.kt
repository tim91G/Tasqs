package com.timgortworst.roomy.ui.event.module

import com.timgortworst.roomy.local.HuishoudGenootSharedPref
import com.timgortworst.roomy.repository.AgendaRepository
import com.timgortworst.roomy.repository.UserRepository
import com.timgortworst.roomy.ui.event.presenter.EventEditPresenter
import com.timgortworst.roomy.ui.event.view.EventEditActivity
import com.timgortworst.roomy.ui.event.view.EventEditView
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module

abstract class EventEditModule {

    @Binds
    internal abstract fun provideEditAgendaEventView(editAgendaEventActivity: EventEditActivity): EventEditView

    @Module
    companion object {

        @Provides
        @JvmStatic
        internal fun provideEditAgendaEventPresenter(
                view: EventEditView,
                agendaRepository: AgendaRepository,
                userRepository: UserRepository,
                sharedPref: HuishoudGenootSharedPref
        ): EventEditPresenter {
            return EventEditPresenter(view, agendaRepository, userRepository, sharedPref)
        }
    }
}