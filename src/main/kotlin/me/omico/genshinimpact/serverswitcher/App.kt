package me.omico.genshinimpact.serverswitcher

import androidx.compose.desktop.Window
import me.omico.genshinimpact.serverswitcher.ui.Main
import me.omico.genshinimpact.serverswitcher.ui.MainViewModel
import javax.swing.UIManager

fun main() = App().create()

class App {

    fun create() {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        val viewModel = MainViewModel()
        Window(
            title = "Genshin Impact server switcher"
        ) {
            Main(viewModel = viewModel)
        }
    }
}
