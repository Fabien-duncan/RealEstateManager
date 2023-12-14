package com.openclassrooms.realestatemanager.common
/**
 * Sealed class representing the various states of a screen view, used for when the view retrieves the Properties
 *
 * @param T The type of data associated with the success state.
 */
sealed class ScreenViewState<out T> {
    object Loading:ScreenViewState<Nothing>()
    data class Success<T>(val data:T):ScreenViewState<T>()
    data class Error(val message:String?):ScreenViewState<Nothing>()
}