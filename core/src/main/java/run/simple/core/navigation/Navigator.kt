package run.simple.core.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey

/**
 * Интерфейс для провайдинга конкретных экранов.
 * */
fun interface EntryProvider {

    fun get(key: NavKey): NavEntry<NavKey>
}

interface Navigator {

    val entryProvider: EntryProvider
    val backStack: SnapshotStateList<NavKey>
    fun goTo(destination: NavKey): Boolean
    fun goBack(): Boolean
}

