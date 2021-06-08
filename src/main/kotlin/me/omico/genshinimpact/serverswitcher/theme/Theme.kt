package me.omico.genshinimpact.serverswitcher.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme {
        Surface(content = content)
    }
}
