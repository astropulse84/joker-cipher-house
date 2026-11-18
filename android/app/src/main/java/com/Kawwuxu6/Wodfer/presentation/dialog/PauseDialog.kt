package com.Kawwuxu6.Wodfer.presentation.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Kawwuxu6.Wodfer.R
import com.Kawwuxu6.Wodfer.core.theme.GoldLine
import com.Kawwuxu6.Wodfer.core.theme.Ivory
import com.Kawwuxu6.Wodfer.core.theme.IvoryDim
import com.Kawwuxu6.Wodfer.core.theme.VelvetDeep

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onCaseFiles: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        shape = RoundedCornerShape(20.dp),
        containerColor = VelvetDeep,
        titleContentColor = Ivory,
        textContentColor = IvoryDim,
        title = {
            Text(
                text = stringResource(R.string.dialog_pause_title),
                style = MaterialTheme.typography.headlineMedium,
                color = Ivory
            )
        },
        text = {
            Text(
                text = stringResource(R.string.dialog_pause_body),
                style = MaterialTheme.typography.bodyMedium,
                color = IvoryDim
            )
        },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(
                    text = stringResource(R.string.resume),
                    style = MaterialTheme.typography.labelLarge,
                    color = GoldLine
                )
            }
        },
        dismissButton = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 4.dp)
            ) {
                TextButton(onClick = onCaseFiles) {
                    Text(
                        text = stringResource(R.string.case_files),
                        style = MaterialTheme.typography.labelLarge,
                        color = IvoryDim
                    )
                }
                TextButton(onClick = onMenu) {
                    Text(
                        text = stringResource(R.string.menu),
                        style = MaterialTheme.typography.labelLarge,
                        color = IvoryDim
                    )
                }
            }
        }
    )
}
