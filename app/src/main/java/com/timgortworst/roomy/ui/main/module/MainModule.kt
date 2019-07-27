package com.timgortworst.roomy.ui.main.module

import com.timgortworst.roomy.ui.category.module.CategoryListModule
import com.timgortworst.roomy.ui.category.view.CategoryListFragment
import com.timgortworst.roomy.ui.event.module.EventListModule
import com.timgortworst.roomy.ui.event.view.EventListFragment
import com.timgortworst.roomy.ui.main.view.MainActivity
import com.timgortworst.roomy.ui.main.view.MainView
import com.timgortworst.roomy.ui.user.module.UserListModule
import com.timgortworst.roomy.ui.user.view.UserListFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {

    @Binds
    internal abstract fun provideMainView(mainActivity: MainActivity): MainView

    @ContributesAndroidInjector(modules = [(EventListModule::class)])
    internal abstract fun provideAgendaFragment(): EventListFragment

    @ContributesAndroidInjector(modules = [(CategoryListModule::class)])
    internal abstract fun provideTasksFragment(): CategoryListFragment

    @ContributesAndroidInjector(modules = [(UserListModule::class)])
    internal abstract fun provideHouseMatesFragment(): UserListFragment
}