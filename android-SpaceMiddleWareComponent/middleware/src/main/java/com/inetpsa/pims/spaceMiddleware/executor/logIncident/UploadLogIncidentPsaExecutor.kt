package com.inetpsa.pims.spaceMiddleware.executor.logIncident

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkRequest
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseCommand
import com.inetpsa.pims.spaceMiddleware.model.logincident.UploadLogIncidentInput
import com.inetpsa.pims.spaceMiddleware.model.logincident.UploadLogIncidentOutput
import com.inetpsa.pims.spaceMiddleware.model.logincident.UploadLogIncidentResponse
import com.inetpsa.pims.spaceMiddleware.network.MiddlewareCommunicationManager
import com.inetpsa.pims.spaceMiddleware.util.FileSplitter
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import com.inetpsa.pims.spaceMiddleware.util.getBase64
import com.inetpsa.pims.spaceMiddleware.util.hasEnum
import com.inetpsa.pims.spaceMiddleware.util.hasOrNull
import com.inetpsa.pims.spaceMiddleware.util.transform

internal class UploadLogIncidentPsaExecutor(command: BaseCommand) :
    BaseLogIncidentPsaExecutor<UploadLogIncidentInput, UploadLogIncidentOutput>(command) {

    companion object {

        const val DEFAULT_QUALITY = 50
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal var fileSplitter = FileSplitter()

    override fun params(parameters: Map<String, Any?>?): UploadLogIncidentInput {
        val path: String = (parameters has Constants.BODY_PARAM_DATA)
        val type: UploadLogIncidentInput.Type = (parameters hasEnum Constants.BODY_PARAM_FILE_TYPE)
        val incidentId: String = (parameters has Constants.PARAM_INCIDENT_ID)
        val filename: String = (parameters has Constants.BODY_PARAM_FILE_NAME)
        val quality: Int? = parameters hasOrNull Constants.BODY_PARAM_QUALITY

        return UploadLogIncidentInput(
            incidentId = incidentId,
            path = path,
            filename = filename,
            type = type,
            quality = quality ?: DEFAULT_QUALITY
        )
    }

    override suspend fun execute(input: UploadLogIncidentInput): NetworkResponse<UploadLogIncidentOutput> =
        when (input.type) {
            UploadLogIncidentInput.Type.IMAGE -> handleImageRequest(input)
            else -> handleFileRequest(input)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun toRequest(input: UploadLogIncidentInput): NetworkRequest = request(
        type = UploadLogIncidentResponse::class.java,
        urls = arrayOf("v1/logincident/pj/", input.incidentId),
        body = input.asBody()
    )

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleFileRequest(input: UploadLogIncidentInput): NetworkResponse<UploadLogIncidentOutput> {
        val context = middlewareComponent.context
        val response = NetworkResponse.Success(UploadLogIncidentOutput(input.filename))

        fileSplitter.handleUploadFile(input.path, input.filename, context)
            .asSequence()
            .forEach { path ->
                val result = with(input.copy()) {
                    data = fileSplitter.toBase64(path, context).orEmpty()
                    postRequest(filename, toRequest(this))
                }
                if (result is NetworkResponse.Failure) {
                    return result
                }
            }
        return response
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun handleImageRequest(input: UploadLogIncidentInput): NetworkResponse<UploadLogIncidentOutput> {
        val context = middlewareComponent.context
        input.data = fileSplitter.handleUploadImage(input.path, input.quality, context).getBase64().orEmpty()
        input.title = input.filename
        return postRequest(
            fileName = input.filename,
            request = toRequest(input)
        )
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal suspend fun postRequest(
        fileName: String,
        request: NetworkRequest
    ): NetworkResponse<UploadLogIncidentOutput> =
        communicationManager.post<UploadLogIncidentResponse>(request, MiddlewareCommunicationManager.MymToken)
            .transform {
                when (it.success) {
                    true -> NetworkResponse.Success(UploadLogIncidentOutput(fileName))

                    else -> {
                        val failure = PimsErrors.serverError(
                            it.errors,
                            "error occurred when uploading $fileName"
                        )
                        NetworkResponse.Failure(failure)
                    }
                }
            }
}
