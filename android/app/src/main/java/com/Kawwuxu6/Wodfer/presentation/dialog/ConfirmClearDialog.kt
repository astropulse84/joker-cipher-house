package com.Kawwuxu6.Wodfer.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.IvoryDim
import com.Kawwuxu6.Wodfer.core.theme.RubyBright
import com.Kawwuxu6.Wodfer.core.theme.VelvetDeep

@Composable
fun ConfirmClearDialog(
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        shape = RoundedCornerShape(20.dp),
        containerColor = VelvetDeep,
        title = {
            Text(
                text = stringResource(R.string.dialog_clear_title),
                style = MaterialTheme.typography.headlineMedium,
                color = Ivory
            )
        },
        text = {
            Text(
                text = stringResource(R.string.dialog_clear_body),
                style = MaterialTheme.typography.bodyMedium,
                color = IvoryDim
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.clear),
                    style = MaterialTheme.typography.labelLarge,
                    color = RubyBright
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = IvoryDim
                )
            }
        }
    )
}
