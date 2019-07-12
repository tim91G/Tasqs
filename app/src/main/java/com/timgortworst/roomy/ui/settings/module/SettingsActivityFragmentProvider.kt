package com.timgortworst.roomy.ui.settings.module

import com.timgortworst.roomy.ui.settings.view.SettingsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module

abstract class SettingsActivityFragmentProvider {

    @ContributesAndroidInjector
    internal abstract fun provideSettingsFragment(): SettingsActivity.SettingsFragment
}