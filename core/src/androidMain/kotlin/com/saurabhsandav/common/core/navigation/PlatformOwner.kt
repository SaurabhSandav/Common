package com.saurabhsandav.common.core.navigation

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner

public actual class PlatformOwner<ROUTE : Any>(
    private val routeEntry: RouteEntry<ROUTE>,
    private val context: Context?,
    private val hostLifecycle: Lifecycle,
    private val hostSavedStateRegistry: SavedStateRegistry,
    private val viewModelStoreHolder: ViewModelStoreHolder,
) : BackStackEventListener<ROUTE>,
    LifecycleOwner,
    ViewModelStoreOwner,
    HasDefaultViewModelProviderFactory,
    SavedStateRegistryOwner {

    private val hostObserver = LifecycleEventObserver { _, event ->
        lifecycleRegistry.handleLifecycleEvent(event)
    }
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val defaultFactory by lazy { SavedStateViewModelFactory() }
    private var savedStateRegistryAttached = false

    init {
        configureSavedState()
        hostLifecycle.addObserver(hostObserver)
    }

    public override fun onChanged(event: BackStackEvent<ROUTE>) {
        when {
            // Route removed
            event is BackStackEvent.RouteRemoved && event.entry.id == routeEntry.id -> {
                hostLifecycle.removeObserver(hostObserver)
                lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
                hostSavedStateRegistry.unregisterSavedStateProvider(routeEntry.id)
                viewModelStoreHolder.remove(routeEntry.id)?.clear()
            }
            // Route is visible
            event is BackStackEvent.RouteVisible && event.entry.id == routeEntry.id -> {
                hostLifecycle.addObserver(hostObserver)
            }
            // Route is not visible
            event is BackStackEvent.RouteVisible && event.entry.id != routeEntry.id -> {
                hostLifecycle.removeObserver(hostObserver)
                lifecycleRegistry.currentState = Lifecycle.State.CREATED
            }
        }
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    override fun getViewModelStore(): ViewModelStore = viewModelStoreHolder[routeEntry.id]

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory = defaultFactory

    override fun getDefaultViewModelCreationExtras(): CreationExtras {
        return MutableCreationExtras().apply {
            (context?.applicationContext as? Application)?.let { application ->
                this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] = application
            }
            this[SAVED_STATE_REGISTRY_OWNER_KEY] = this@PlatformOwner
            this[VIEW_MODEL_STORE_OWNER_KEY] = this@PlatformOwner
        }
    }

    override fun getSavedStateRegistry(): SavedStateRegistry = savedStateRegistryController.savedStateRegistry

    private fun configureSavedState() {

        if (!savedStateRegistryAttached) {
            savedStateRegistryController.performAttach()
            enableSavedStateHandles()
            savedStateRegistryAttached = true

            // Restore state
            val restored = hostSavedStateRegistry.consumeRestoredStateForKey(routeEntry.id)
            savedStateRegistryController.performRestore(restored)
        }

        hostSavedStateRegistry.registerSavedStateProvider(routeEntry.id) {
            Bundle().also { savedStateRegistryController.performSave(it) }
        }
    }

    public actual class Builder(
        private val context: Context?,
        private val hostLifecycle: Lifecycle,
        private val hostSavedStateRegistry: SavedStateRegistry,
        private val viewModelStoreHolder: ViewModelStoreHolder,
    ) {

        public actual fun <ROUTE : Any> build(
            navController: NavController<ROUTE>,
            routeEntry: RouteEntry<ROUTE>,
        ): PlatformOwner<ROUTE> {
            return PlatformOwner(
                routeEntry,
                context,
                hostLifecycle,
                hostSavedStateRegistry,
                viewModelStoreHolder,
            )
        }
    }
}

public class ViewModelStoreHolder : ViewModel() {

    private val stores = mutableMapOf<String, ViewModelStore>()

    public operator fun get(key: String): ViewModelStore {
        return stores.getOrPut(key) { ViewModelStore() }
    }

    public fun remove(key: String): ViewModelStore? {
        return stores.remove(key)
    }

    override fun onCleared() {
        stores.values.forEach { it.clear() }
    }
}
