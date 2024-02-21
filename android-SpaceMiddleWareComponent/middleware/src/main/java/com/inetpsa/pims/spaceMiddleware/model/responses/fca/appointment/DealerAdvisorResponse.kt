package com.inetpsa.pims.spaceMiddleware.model.responses.fca.appointment

import com.google.gson.annotations.SerializedName

internal data class DealerAdvisorResponse(
    @field:SerializedName("serviceAdvisors")
    val advisors: List<ServiceAdvisors>? = null
) {
    internal data class ServiceAdvisors(
        @field:SerializedName("id") val id: Int? = null,
        @field:SerializedName("name") val name: String? = null,
        @field:SerializedName("memberId") val memberId: Int? = null
    )
}
