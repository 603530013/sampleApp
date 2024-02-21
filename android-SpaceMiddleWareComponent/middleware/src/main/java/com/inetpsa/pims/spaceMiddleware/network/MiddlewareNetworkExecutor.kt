package com.inetpsa.pims.spaceMiddleware.network

import androidx.annotation.VisibleForTesting
import androidx.core.text.HtmlCompat
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.inetpsa.mmx.foundation.extensions.asJson
import com.inetpsa.mmx.foundation.monitoring.ErrorCode
import com.inetpsa.mmx.foundation.monitoring.PIMSErrorFactory
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.monitoring.logger.PIMSLogger
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.MymResponse
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors.typeError
import com.inetpsa.pims.spaceMiddleware.util.Utils
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException

internal class MiddlewareNetworkExecutor(private val client: OkHttpClient) {

    companion object {

        private const val ERROR_CODE_JSON_SYNTAX_EXCEPTION = -3
        private const val ERROR_CODE_JSON_PARSE_EXCEPTION = -4
        private const val HTTP_STATUS_CODE_NO_CONTENT = 204
        private const val HTTP_STATUS_CODE_SUCCESS_RANGE_START = 200
        private const val HTTP_STATUS_CODE_SUCCESS_RANGE_END = 299
    }

    @Throws(IOException::class, IllegalStateException::class, JsonParseException::class, JsonSyntaxException::class)
    fun <T> execute(request: Request, type: Type): NetworkResponse<T> {
        return safeNetworkCall {
            val response = client.newCall(request).execute()

            when (response.code) {
                HTTP_STATUS_CODE_NO_CONTENT -> handle204Response()

                in HTTP_STATUS_CODE_SUCCESS_RANGE_START..HTTP_STATUS_CODE_SUCCESS_RANGE_END ->
                    handle2xxResponse(response, type)

                else -> handleNot2xxResponse(response)
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Suppress("SwallowedException")
    internal infix fun <T> safeParsingCall(action: () -> NetworkResponse<T>) = try {
        action.invoke()
    } catch (ex: JsonSyntaxException) {
        PIMSLogger.w(ex)
        val error = PIMSErrorFactory.create(
            ERROR_CODE_JSON_SYNTAX_EXCEPTION,
            "attempts to read a malformed JSON element"
        )
        NetworkResponse.Failure(error)
    } catch (ex: JsonParseException) {
        PIMSLogger.w(ex)
        val error = PIMSErrorFactory.create(
            ERROR_CODE_JSON_PARSE_EXCEPTION,
            "there is a serious issue that occurs during parsing of a Json string "
        )
        NetworkResponse.Failure(error)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Suppress("TooGenericExceptionCaught")
    internal infix fun <T> safeNetworkCall(action: () -> NetworkResponse<T>) = try {
        action.invoke()
    } catch (ex: IllegalStateException) {
        PIMSLogger.e(ex)
        val error = PIMSErrorFactory.create(-1, "call has already been executed: ${ex.message}")
        NetworkResponse.Failure(error)
    } catch (ex: SocketTimeoutException) {
        PIMSLogger.e(ex)
        NetworkResponse.Failure(PIMSFoundationError.timeoutError)
    } catch (ex: UnknownHostException) {
        PIMSLogger.e(ex)
        val error = PIMSFoundationError.networkError
        NetworkResponse.Failure(error)
    } catch (ex: ConnectionShutdownException) {
        PIMSLogger.e(ex)
        val error = PIMSFoundationError.networkError
        NetworkResponse.Failure(error)
    } catch (ex: IOException) {
        PIMSLogger.e(ex)
        val error = PIMSFoundationError.networkError
        NetworkResponse.Failure(error)
    } catch (ex: Exception) {
        PIMSLogger.e(ex)
        val error = PIMSFoundationError.unknownError
        NetworkResponse.Failure(error)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Suppress("UNCHECKED_CAST")
    internal fun <T> handle204Response(): NetworkResponse<T> =
        NetworkResponse.Success(Unit) as NetworkResponse<T>

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun <T> handleNot2xxResponse(response: Response): NetworkResponse<T> {
        val message = when (response.message.isBlank()) {
            true ->
                HtmlCompat
                    .fromHtml(response.body?.string() ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
                    .toString()
            else -> response.message
        }
        val error = PIMSFoundationError.serverError(response.code, message)
        return NetworkResponse.Failure(error)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun <T> handle2xxResponse(response: Response, type: Type): NetworkResponse<T> =
        response.body?.string()
            ?.takeIf { it.isNotBlank() }
            ?.let { body ->
                val result = handleMyMResponse<T>(body, type)
                val syntaxFailure = result is NetworkResponse.Failure &&
                    result.error?.code == ERROR_CODE_JSON_SYNTAX_EXCEPTION
                return if (syntaxFailure) {
                    handleTypeResponse(body, type)
                } else {
                    result
                }
            } ?: kotlin.run {
            (NetworkResponse.Success(Unit) as? NetworkResponse<T>)
                ?: NetworkResponse.Failure(typeError("failed to return response with empty body to type $type "))
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun <T> handleMyMResponse(response: String, type: Type): NetworkResponse<T> =
        safeParsingCall {
            val rawType = TypeToken.get(type).rawType
            if (rawType == MymResponse::class.java) {
                throw IllegalArgumentException(
                    "$type should never contain `MymResponse` as a super type, " +
                        "put subType directly and it will be handled systematically"
                )
            }

            val mymType = TypeToken.getParameterized(MymResponse::class.java, type).type

            val jsonResponse: MymResponse<T> = Utils.getJsonClient().fromJson(response, mymType)

            when {
                jsonResponse.error != null -> {
                    val mymError = jsonResponse.error
                    val error = PIMSFoundationError.serverError(mymError.code, mymError.message)
                    NetworkResponse.Failure(error)
                }

                !jsonResponse.errors.isNullOrEmpty() -> {
                    val mymError = jsonResponse.errors
                    val error = PIMSFoundationError.serverError(ErrorCode.serverError, mymError.asJson())
                    NetworkResponse.Failure(error)
                }

                jsonResponse.success != null -> {
                    val safeResponse = jsonResponse.success
                    NetworkResponse.Success(safeResponse)
                }

                else -> NetworkResponse.Failure(PIMSFoundationError.unknownError)
            }
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun <T> handleTypeResponse(response: String, type: Type): NetworkResponse<T> =
        safeParsingCall {
            val jsonResponse: T = Utils.getJsonClient().fromJson(response, type)
            NetworkResponse.Success(jsonResponse)
        }
}
