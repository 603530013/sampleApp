package com.mobiledrivetech.external.middleware.foundation.commandManager

import androidx.annotation.VisibleForTesting
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.util.DefaultDispatcherProvider
import com.mobiledrivetech.external.middleware.util.DispatcherProvider
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections
import java.util.concurrent.Executors

class CommandExecutor(
    workerCount: Int,
    private val dispatcher: DispatcherProvider = DefaultDispatcherProvider()
) {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val threadPool =
        Executors.newFixedThreadPool(workerCount).asCoroutineDispatcher()

    private val scope = CoroutineScope(threadPool + CoroutineName("IO"))

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var onGoingCommands: MutableCollection<Command> =
        Collections.synchronizedCollection(mutableListOf<Command>())

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var producerScope: ProducerScope<Command>? = null

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    // producerScope is promote to StableAPI on coroutine 1.6.0
    internal var flow = callbackFlow {
        producerScope = this
        awaitClose()
    }.buffer(6)
        .catch {
            MDLog.error("CommandExecutor throw exception")
        }

    init {
        flow.onEach { collectCommand(it) }
            .launchIn(scope)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun collectCommand(command: Command) {
        executeCommand(command)
    }

    fun send(command: Command) {
        if (onGoingCommands.none { it.identifier == command.identifier }) {
            scope.launch {
                producerScope?.send(command)
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun executeCommand(command: Command) {
        onGoingCommands.add(command)
        command.execute(dispatcher) {
            scope.launch {
                withContext(dispatcher.main()) {
                    command.execute()
                }
            }
            onGoingCommands.removeIf { it == command }
        }
    }
}
