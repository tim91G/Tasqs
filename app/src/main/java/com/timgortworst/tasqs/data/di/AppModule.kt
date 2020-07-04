package com.timgortworst.tasqs.data.di

import com.timgortworst.tasqs.data.sharedpref.SharedPrefManager
import com.timgortworst.tasqs.data.sharedpref.SharedPrefs
import com.timgortworst.tasqs.presentation.features.notifications.NotificationQueue
import com.timgortworst.tasqs.presentation.features.notifications.NotificationQueueImpl
import com.timgortworst.tasqs.presentation.features.notifications.Notifications
import com.timgortworst.tasqs.presentation.features.notifications.NotificationsImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    factory<NotificationQueue> { NotificationQueueImpl(androidContext()) }
    factory<Notifications> { NotificationsImpl(androidContext()) }
    single { SharedPrefManager(androidContext()) }
    single { SharedPrefs(get()) }
}