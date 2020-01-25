package com.timgortworst.roomy.data.model.firestore

import com.timgortworst.roomy.data.model.EventMetaData
import com.timgortworst.roomy.data.model.EventMetaData.EventInterval.SINGLE_EVENT
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId

data class EventMetaDataJson(
        var eventDateTime: Long = Instant.now().toEpochMilli(),
        var eventTimeZone: String = ZoneId.systemDefault().id,
        var eventInterval: EventMetaData.EventInterval = SINGLE_EVENT
)