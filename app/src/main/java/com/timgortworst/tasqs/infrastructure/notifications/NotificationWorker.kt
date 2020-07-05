package com.timgortworst.tasqs.infrastructure.notifications

import android.content.Context
import androidx.work.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.timgortworst.tasqs.R
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.Duration
import org.threeten.bp.ZonedDateTime
import java.util.concurrent.TimeUnit

class NotificationWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params), KoinComponent {
    private val workManager = WorkManager.getInstance(context)
    private val notifications: Notifications by inject()

    override fun doWork() = try {
        val title = inputData.getString(NotificationQueueImpl.NOTIFICATION_TITLE_KEY)
            ?: context.getString(R.string.app_name)
        val text = inputData.getString(NotificationQueueImpl.NOTIFICATION_MSG_KEY)
            ?: context.getString(R.string.notification_default_msg)
        val id = tags.first().toString()

        notifications.notify(
            id,
            context.getString(R.string.notification_title, title),
            context.getString(R.string.notification_message, text)
        )

        setTomorrowReminder(id)

        Result.success()
    } catch (e: Exception) {
        FirebaseCrashlytics.getInstance().recordException(e)
        Result.failure()
    }

    private fun setTomorrowReminder(
        id: String
    ) {
        val now = ZonedDateTime.now()
        val nextReminder = now.plusDays(1)
        val initialDelay = Duration.between(now, nextReminder).toMillis()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(id)
            .setInputData(inputData)
            .build()

        workManager.enqueueUniqueWork(id, ExistingWorkPolicy.REPLACE, workRequest)
    }
}