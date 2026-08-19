package com.zqlq.composewan.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zqlq.composewan.R
import com.zqlq.composewan.ui.common.NETWORK_UNAVAILABLE_MARKER

/**
 * 空数据错误页：展示原因并点击重试。
 */
@Composable
fun ErrorRetryPane(
    message: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val displayMessage =
        when {
            message == NETWORK_UNAVAILABLE_MARKER -> stringResource(R.string.network_unavailable)
            message.isNullOrBlank() -> stringResource(R.string.load_failed)
            else -> message
        }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .clickable(onClick = onRetry)
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = displayMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.click_to_retry),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
