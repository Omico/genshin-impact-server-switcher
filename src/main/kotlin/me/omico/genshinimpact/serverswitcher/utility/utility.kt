package me.omico.genshinimpact.serverswitcher.utility

import java.io.File

fun resourceAsFile(path: String): File = ClassLoader.getSystemResource(path).path.toFile()

fun String.toFile(): File = File(this)
