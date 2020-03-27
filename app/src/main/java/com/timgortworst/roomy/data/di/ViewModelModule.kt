package com.timgortworst.roomy.data.di

import com.timgortworst.roomy.presentation.features.main.MainViewModel
import com.timgortworst.roomy.presentation.features.settings.SettingsViewModel
import com.timgortworst.roomy.presentation.features.signin.SignInViewModel
import com.timgortworst.roomy.presentation.features.task.viewmodel.TaskListViewModel
import com.timgortworst.roomy.presentation.features.user.UserViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TaskListViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { MainViewModel(get(), get()) }
}
