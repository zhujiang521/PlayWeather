package com.zj.model

sealed class PlayState<out R> {
    fun isLoading() = this is PlayLoading
    fun isSuccessful() = this is PlaySuccess

    override fun toString(): String {
        return when (this) {
            is PlaySuccess<*> -> "Success[data=$data]"
            is PlayNoContent -> "Success[reason=$reason]"
            is PlayError -> "Error[exception=${error}]"
            PlayLoading -> "Loading"
        }
    }
}

data class PlaySuccess<out T>(val data: T) : PlayState<T>()
data class PlayNoContent(val reason: String) : PlayState<Nothing>()
data class PlayError(val error: Throwable) : PlayState<Nothing>()
object PlayLoading : PlayState<Nothing>()