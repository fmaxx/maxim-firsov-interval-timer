package run.simple.interval_timer.navigation

import androidx.compose.runtime.mutableStateListOf
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

class NavigatorImpl(
    startDestination: NavKey,
    override val entryProvider: EntryProvider,
) : Navigator {

    override val backStack: SnapshotStateList<NavKey> = mutableStateListOf(startDestination)

    override fun goTo(destination: NavKey): Boolean =
        backStack.add(destination)

    override fun goBack(): Boolean =
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
            true
        } else {
            false
        }
}