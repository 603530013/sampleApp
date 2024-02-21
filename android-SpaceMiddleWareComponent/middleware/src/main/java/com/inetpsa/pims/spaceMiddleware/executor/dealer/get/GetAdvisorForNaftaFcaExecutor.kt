package com.inetpsa.pims.spaceMiddleware.executor.dealer.get

import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants.Input.Appointment
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.dealer.appointment.department.BaseNaftaDepartmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.helpers.fca.BookingOnlineCache
import com.inetpsa.pims.spaceMiddleware.model.dealer.appointment.token.OssTokenInput
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment.DealerAdvisorResponse
import com.inetpsa.pims.spaceMiddleware.util.ifSuccess

internal class GetAdvisorForNaftaFcaExecutor(
    middlewareComponent: MiddlewareComponent,
    params: Map<String, Any?>? = null
) : BaseNaftaDepartmentFcaExecutor<OssTokenInput, DealerAdvisorResponse>(middlewareComponent, params) {

    override fun params(parameters: Map<String, Any?>?): OssTokenInput =
        OssTokenInput(dealerId = parameters has Appointment.BOOKING_ID)

    override suspend fun execute(
        input: OssTokenInput,
        token: String,
        departmentId: String
    ): NetworkResponse<DealerAdvisorResponse> {
        BookingOnlineCache.readAdvisors()?.let { return NetworkResponse.Success(DealerAdvisorResponse(it)) }

        val request = request(
            type = DealerAdvisorResponse::class.java,
            urls = arrayOf(
                "/v1/servicescheduler/department/",
                departmentId,
                "/advisors"
            ),
            headers = headers(token)
        )
        return communicationManager.get<DealerAdvisorResponse>(request, TokenType.AWSToken(FCAApiKey.SDP))
            .ifSuccess { response -> BookingOnlineCache.write(response) }
    }
}
