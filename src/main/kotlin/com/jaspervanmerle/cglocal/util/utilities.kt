package com.jaspervanmerle.cglocal.util

import com.jaspervanmerle.cglocal.CGLocal
import com.jaspervanmerle.cglocal.controller.StatusController
import com.jaspervanmerle.cglocal.view.MainView
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.layout.BorderPane
import mu.KotlinLogging
import tornadofx.*
import java.awt.Desktop
import java.io.File
import java.net.URL
import kotlin.concurrent.thread
import kotlin.reflect.KClass

val logger = KotlinLogging.logger {}

@Suppress("UNCHECKED_CAST")
fun <T : UIComponent> setCenter(newCenter: KClass<T>, parent: KClass<*> = MainView::class) {
    if (!CGLocal.stopping) {
        (find(parent as KClass<T>).root as BorderPane).center.replaceWith(find(newCenter).root)
    }
}

fun setStatus(status: String) {
    if (!CGLocal.stopping) {
        logger.info("Changing status to \"$status\"")
        find(StatusController::class).status = status
    }
}

inline fun icon(icon: FontAwesomeIcon, op: FontAwesomeIconView.() -> Unit = {}): FontAwesomeIconView {
    val iconView = FontAwesomeIconView(icon)
    iconView.op()
    return iconView
}

fun openBrowser(url: String) {
    if (Desktop.isDesktopSupported()) {
        // On Linux, getDesktop().browse() hangs the application
        // That issue can be fixed by running the call in a separate thread
        thread {
            logger.info("Opening $url")
            Desktop.getDesktop().browse(URL(url).toURI())
        }
    }
}

fun File.getSource(): String {
    val sb = StringBuilder()

    forEachLine {
        sb.append(it)
        sb.append("\n")
    }

    return sb.toString()
}
