package com.timgortworst.roomy.domain.model.response

import androidx.annotation.Keep
import com.timgortworst.roomy.R

@Keep
sealed class Response<out T> {
    object Loading : Response<Nothing>()
    data class Success<T>(val data: T? = null) : Response<T>()
    data class Error(val error: ErrorEntity? = null) : Response<Nothing>()
    data class Empty(val msg: Int = R.string.empty_string) : Response<Nothing>()
}