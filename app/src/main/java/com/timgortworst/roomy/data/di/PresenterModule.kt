package com.timgortworst.roomy.data.di


import com.timgortworst.roomy.presentation.features.event.presenter.EventEditPresenter
import com.timgortworst.roomy.presentation.features.event.presenter.EventInfoPresenter
import com.timgortworst.roomy.presentation.features.event.presenter.EventListPresenter
import com.timgortworst.roomy.presentation.features.event.view.EventEditView
import com.timgortworst.roomy.presentation.features.event.view.EventInfoView
import com.timgortworst.roomy.presentation.features.event.view.EventListView
import com.timgortworst.roomy.presentation.features.googlesignin.presenter.GoogleSignInPresenter
import com.timgortworst.roomy.presentation.features.googlesignin.view.GoogleSignInView
import com.timgortworst.roomy.presentation.features.main.presenter.MainPresenter
import com.timgortworst.roomy.presentation.features.main.view.MainView
import com.timgortworst.roomy.presentation.features.settings.presenter.SettingsPresenter
import com.timgortworst.roomy.presentation.features.settings.view.SettingsView
import com.timgortworst.roomy.presentation.features.setup.presenter.SetupPresenter
import com.timgortworst.roomy.presentation.features.setup.view.SetupView
import com.timgortworst.roomy.presentation.features.splash.presenter.SplashPresenter
import com.timgortworst.roomy.presentation.features.splash.view.SplashView
import com.timgortworst.roomy.presentation.features.user.presenter.UserListPresenter
import com.timgortworst.roomy.presentation.features.user.view.UserListView
import org.koin.dsl.module

val presenterModule = module(override = true) {
    factory { (view: SplashView) -> SplashPresenter(view, get()) }
    factory { (view: GoogleSignInView) -> GoogleSignInPresenter(view) }
    factory { (view: SetupView) -> SetupPresenter(view, get()) }
    factory { (view: MainView) -> MainPresenter(view, get(), get()) }
    factory { (view: EventListView) -> EventListPresenter(view, get()) }
    factory { (view: EventInfoView) -> EventInfoPresenter(view) }
    factory { (view: EventEditView) -> EventEditPresenter(view, get()) }
    factory { (view: UserListView) -> UserListPresenter(view, get()) }
    factory { (view: SettingsView) -> SettingsPresenter(view, get()) }
}