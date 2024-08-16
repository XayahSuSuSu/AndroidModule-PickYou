package com.xayah.libpickyou.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.xayah.libpickyou.R
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Creates a [DialogState] and acts as a slot with [DialogState.Insert].
 */
@Composable
internal fun rememberDialogState(): DialogState {
    val state = remember { DialogState() }
    state.Insert()
    return state
}

enum class DismissState {
    DISMISS,
    CANCEL,
    CONFIRM;

    val isConfirm get() = this == CONFIRM
}

internal class DialogState {
    private var content: (@Composable () -> Unit)? by mutableStateOf(null)

    @Composable
    internal fun Insert() = content?.invoke()

    private fun dismiss() {
        content = null
    }

    /**
     * Return **Pair<Boolean, T>**.
     *
     * If user clicks **confirmButton**, then return **Pair(true, T)**,
     * otherwise return **Pair(false, T)**.
     */
    private suspend fun <T> open(
        initialState: T,
        title: String,
        icon: ImageVector? = null,
        confirmText: String? = null,
        dismissText: String? = null,
        block: @Composable (MutableState<T>) -> Unit,
    ): Pair<DismissState, T> {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation { dismiss() }
            content = {
                val uiState = remember { mutableStateOf(initialState) }
                AlertDialog(
                    onDismissRequest = {
                        dismiss()
                        continuation.resume(Pair(DismissState.DISMISS, uiState.value))
                    },
                    confirmButton = {
                        Button(text = confirmText ?: stringResource(id = R.string.confirm), onClick = {
                            dismiss()
                            continuation.resume(Pair(DismissState.CONFIRM, uiState.value))
                        })
                    },
                    dismissButton = {
                        TextButton(text = dismissText ?: stringResource(id = R.string.cancel), onClick = {
                            dismiss()
                            continuation.resume(Pair(DismissState.CANCEL, uiState.value))
                        })
                    },
                    title = { Text(text = title) },
                    icon = icon?.let { { Icon(imageVector = icon, contentDescription = null) } },
                    text = {
                        block(uiState)
                    },
                )
            }
        }
    }

    suspend fun openEdit(
        defValue: String = "",
        title: String,
        icon: ImageVector,
        label: String? = null,
    ) = open(
        initialState = defValue,
        title = title,
        icon = icon,
        block = { state ->
            TextField(
                value = state.value,
                onValueChange = { state.value = it },
                label = if (label == null) {
                    null
                } else {
                    { Text(text = label) }
                },
                singleLine = true
            )
        }
    )

    suspend fun openConfirm(
        defValue: Boolean = false,
        title: String,
        icon: ImageVector,
        text: String,
    ) = open(
        initialState = defValue,
        title = title,
        icon = icon,
        block = { _ ->
            Text(text = text)
        }
    )
}
