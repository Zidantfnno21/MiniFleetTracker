package com.example.minifleettracker.helper

sealed class UIEvent {
    data class ShowSnackbar(val message: String ? = null, ) : UIEvent()
    data class ShowAlert(
        val message: String? = null,
        val actionLabel: String? = null,
        val onAction: (() -> Unit)? = null
    ) : UIEvent()
}