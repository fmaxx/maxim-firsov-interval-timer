package run.simple.interval_timer.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavKey
import run.simple.core.navigation.EntryProvider
import run.simple.core.navigation.Navigator

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