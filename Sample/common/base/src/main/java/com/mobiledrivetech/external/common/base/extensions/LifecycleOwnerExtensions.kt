package com.mobiledrivetech.external.common.base.extensions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

/**
 * Adds the given observer to the observers list within the lifespan of the given
 * owner. The events are dispatched on the main thread. If LiveData already has data
 * set, it will be delivered to the observer.
 *
 * @param liveData The liveData to observe.
 * @param observer The observer that will receive the events.
 * @see LiveData.observe
 */
fun <T> LifecycleOwner.observe(liveData: LiveData<T>, observer: (T) -> Unit) {
    liveData.observe(
        this,
        Observer {
            it?.let { t -> observer(t) }
        }
    )
}

/**
 * Adds the given observer to the observers list within the lifespan of the given
 * owner. The events are dispatched on the main thread. If LiveData already has data
 * set, it will be delivered to the observer.
 *
 * @param liveData The mutableLiveData to observe.
 * @param observer The observer that will receive the events.
 * @see MutableLiveData.observe
 */
fun <T> LifecycleOwner.observe(liveData: MutableLiveData<T>, observer: (T) -> Unit) {
    liveData.observe(
        this,
        Observer {
            it?.let { t -> observer(t) }
        }
    )
}

/**
 * Flow operator that emits values from `this` upstream Flow when the [lifecycle] is
 * at least at [minActiveState] state. The emissions will be stopped when the lifecycle state
 * falls below [minActiveState] state.
 *
 * The flow will automatically start and cancel collecting from `this` upstream flow as the
 * [lifecycle] moves in and out of the target state.
 *
 * If [this] upstream Flow completes emitting items, `flowWithLifecycle` will trigger the flow
 * collection again when the [minActiveState] state is reached.
 *
 * This is NOT a terminal operator. This operator is usually followed by [collect], or
 * [onEach] and [launchIn] to process the emitted values.
 *
 * Note: this operator creates a hot flow that only closes when the [lifecycle] is destroyed or
 * the coroutine that collects from the flow is cancelled.
 *
 * ```
 * class MyActivity : AppCompatActivity() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         /* ... */
 *         // Launches a coroutine that collects items from a flow when the Activity
 *         // is at least started. It will automatically cancel when the activity is stopped and
 *         // start collecting again whenever it's started again.
 *         lifecycleScope.launch {
 *             flow
 *                 .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
 *                 .collect {
 *                     // Consume flow emissions
 *                  }
 *         }
 *     }
 * }
 * ```
 *
 * `flowWithLifecycle` cancels the upstream Flow when [lifecycle] falls below
 * [minActiveState] state. However, the downstream Flow will be active without receiving any
 * emissions as long as the scope used to collect the Flow is active. As such, please take care
 * when using this function in an operator chain, as the order of the operators matters. For
 * example, `flow1.flowWithLifecycle(lifecycle).combine(flow2)` behaves differently than
 * `flow1.combine(flow2).flowWithLifecycle(lifecycle)`. The former continues to combine both
 * flows even when [lifecycle] falls below [minActiveState] state whereas the combination is
 * cancelled in the latter case.
 *
 * Warning: [Lifecycle.State.INITIALIZED] is not allowed in this API. Passing it as a
 * parameter will throw an [IllegalArgumentException].
 *
 * Tip: If multiple flows need to be collected using `flowWithLifecycle`, consider using
 * the [Lifecycle.repeatOnLifecycle] API to collect from all of them using a different
 * [launch] per flow instead. That's more efficient and consumes less resources as no hot flows
 * are created.
 *
 * @param lifecycle The [Lifecycle] where the restarting collecting from `this` flow work will be
 * kept alive.
 * @param minActiveState [Lifecycle.State] in which the upstream flow gets collected. The
 * collection will stop if the lifecycle falls below that state, and will restart if it's in that
 * state again.
 * @return [Flow] that only emits items from `this` upstream flow when the [lifecycle] is at
 * least in the [minActiveState].
 */
// TODO Remove after migrate Lifecycle to 2.5.1
@OptIn(ExperimentalCoroutinesApi::class)
@Deprecated("In Develop we have the 2.5.1 version of Lifecycle, delete this after merge")
public fun <T> Flow<T>.flowWithLifecycle(
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = callbackFlow {
    lifecycle.repeatOnLifecycle(minActiveState) {
        this@flowWithLifecycle.collect {
            send(it)
        }
    }
    close()
}
