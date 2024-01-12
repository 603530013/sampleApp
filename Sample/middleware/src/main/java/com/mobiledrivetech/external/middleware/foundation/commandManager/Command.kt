package com.mobiledrivetech.external.middleware.foundation.commandManager

import android.os.Build
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.mobiledrivetech.external.middleware.Constants
import com.mobiledrivetech.external.middleware.extensions.sort
import com.mobiledrivetech.external.middleware.foundation.genericComponent.GenericComponentInterface
import com.mobiledrivetech.external.middleware.foundation.models.CommandName
import com.mobiledrivetech.external.middleware.foundation.models.CommandStatus
import com.mobiledrivetech.external.middleware.foundation.models.CommandType
import com.mobiledrivetech.external.middleware.foundation.monitoring.logger.MDLog
import com.mobiledrivetech.external.middleware.util.DispatcherProvider
import java.lang.ref.WeakReference
import java.util.Locale
import java.util.UUID


open class Command(
    @Expose var transactionId: String = "${UUID.randomUUID()}-${System.currentTimeMillis() / 1000}"
) : Cloneable {

    @Expose
    var name: CommandName? = null

    @Expose
    var type: CommandType? = null

    @Expose
    var parameters: Map<String, Any?>? = null

    @Expose
    var configuration: Map<String, Any?>? = null

    @Expose
    var identifier: String? = null

    constructor(
        name: CommandName?,
        type: CommandType?,
        parameters: Map<String, Any?>?,
        configuration: Map<String, Any?>? = null
    ) : this() {

        this.name = name
        this.type = type
        this.parameters = parameters
        this.configuration = configuration
        this.sdkVersion = configuration?.get(Constants.KEY_SDK_VERSION) as? String ?: ""
    }

    fun init(
        name: CommandName?,
        type: CommandType?,
        parameters: Map<String, Any?>?,
        configuration: Map<String, Any?>?
    ): Command {
        return clone().apply {
            this.transactionId = "${UUID.randomUUID()}-${System.currentTimeMillis() / 1000}"
            this.name = name
            this.type = type
            this.parameters = parameters
            this.configuration = configuration
            this.identifier = "$type+$name+${parameters?.sort()}".uppercase(Locale.ROOT)

            this.result = null
            this.error = null
            this.status = CommandStatus.WAITING
        }
    }

    @Expose
    var result: Map<String, Any?>? = null

    @Expose
    var sdkVersion: String? = null

    @Expose
    var status: CommandStatus = CommandStatus.WAITING

    @Expose
    var error: Map<String, Any>? = null

    @Expose
    private var context: Map<String, Any?>? = null

    @Expose
    private var callback: ((Map<String, Any?>) -> Unit)? = null


    @Expose
    var componentReference: WeakReference<GenericComponentInterface?>? = null

    init {
        context = fillContext()
    }

    fun register(result: ((Map<String, Any?>) -> Unit)?) {
        this.callback = result
        if (status != CommandStatus.WAITING) execute()
    }

    fun cancel() {
        callback = null
        status = CommandStatus.CANCELED
    }

    fun success(result: Map<String, Any?>? = emptyMap()) {
        this.status = CommandStatus.SUCCEEDED
        this.result = result
        this.error = null
    }

    fun failure(error: Map<String, Any>?) {
        this.status = CommandStatus.FAILED
        this.error = error
        this.result = null
    }

    internal fun execute() {
        if (status == CommandStatus.CANCELED) return

        MDLog.debug(
            "command: transactionId: $transactionId, name: $name," +
                    " parameters: $parameters, status: $status, result: $result"
        )

        callback?.invoke(toMap())
    }

    open suspend fun execute(callback: () -> Unit) {
    }

    internal suspend fun execute(dispatcher: DispatcherProvider, callback: () -> Unit) {
        execute(callback)
    }


    fun deserialize(jsonString: String): Command {
        val result = Gson().fromJson(jsonString, this::class.java)
        return result
    }

    private fun fillContext(): Map<String, Any?> {
        return mapOf(
            Pair(Constants.CONTEXT_KEY_PHONE_MODEL, Build.MODEL),
            Pair(Constants.CONTEXT_KEY_OS_VERSION, Build.VERSION.RELEASE),
            Pair(
                Constants.CONTEXT_KEY_ENVIRONMENT,
                configuration?.get(Constants.CONTEXT_KEY_ENVIRONMENT)
            ),
            Pair(Constants.CONTEXT_KEY_MODE, configuration?.get(Constants.CONTEXT_KEY_MODE)),
            Pair(
                Constants.CONTEXT_KEY_LOG_LEVEL,
                configuration?.get(Constants.CONTEXT_KEY_LOG_LEVEL)
            ),
            Pair(
                Constants.CONTEXT_KEY_CLIENT_ID,
                configuration?.get(Constants.CONTEXT_KEY_CLIENT_ID)
            ),
            Pair(
                Constants.CONTEXT_KEY_CLIENT_SECRET,
                configuration?.get(Constants.CONTEXT_KEY_CLIENT_SECRET)
            ),
            Pair(Constants.CONTEXT_KEY_BRAND, configuration?.get(Constants.CONTEXT_KEY_BRAND)),
            Pair(
                Constants.CONTEXT_KEY_RETURN_TYPE,
                configuration?.get(Constants.CONTEXT_KEY_RETURN_TYPE)
            ),
            Pair(Constants.CONTEXT_KEY_LOGIN, configuration?.get(Constants.CONTEXT_KEY_LOGIN)),
        )
    }

    fun toMap(): Map<String, Any?> = hashMapOf<String, Any?>().apply {
        this[Constants.KEY_TRANSACTION_ID] = transactionId
        this[Constants.KEY_NAME] = name?.name
        this[Constants.KEY_TYPE] = type?.type
        this[Constants.KEY_PARAMETERS] = parameters
        this[Constants.KEY_RESULT] = result
        this[Constants.KEY_SDK_VERSION] = sdkVersion
        this[Constants.KEY_STATUS] = status.name
        this[Constants.KEY_ERROR] = error
        this[Constants.KEY_CONTEXT] = context
    }

    override fun clone(): Command {
        return super.clone() as Command
    }
}
