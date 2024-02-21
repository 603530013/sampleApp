package com.inetpsa.pims.spaceMiddleware.model.servicelist

import com.google.gson.annotations.SerializedName

@Deprecated("this should be replaced by ContractResponse")
internal data class Contract(
    @SerializedName("nac")
    val nac: List<NacItem?>? = null,

    @SerializedName("remoteLev")
    val remoteLev: List<RemoteLev?>? = null,

    @SerializedName("flexiLease")
    val flexiLease: List<FlexiLease?>? = null,

    @SerializedName("club")
    val club: List<ClubItem?>? = null,

    @SerializedName("services")
    val services: List<ContractServicesItem?>? = null,

    @SerializedName("bta")
    val bta: List<Bta?>? = null,

    @SerializedName("tmts")
    val tmts: List<Tmts?>? = null,

    @SerializedName("dscp")
    val dscp: List<Dscp?>? = null,

    @SerializedName("raccess")
    val raccess: List<Raccess?>? = null
)

@Deprecated("this should be replaced by ContractResponse")
internal open class BaseItem(
    @Transient
    open val code: String? = null,
    @Transient
    open val validity: Validity? = null,
    @Transient
    open val type: String? = null,
    @Transient
    open val title: String? = null,
    @Transient
    open val category: String? = null,
    @Transient
    open val status: Int? = null
)

@Deprecated("this should be replaced by ContractResponse")
internal data class RemoteLev(

    @SerializedName("code")
    override val code: String? = null,

    @SerializedName("fds")
    val fds: List<String?>? = null,

    @SerializedName("validity")
    override val validity: Validity? = null,

    @SerializedName("type")
    override val type: String? = null,

    @SerializedName("title")
    override val title: String? = null,

    @SerializedName("category")
    override val category: String? = null,

    @SerializedName("status")
    override val status: Int? = null,

    @SerializedName("is_extensible")
    val isExtensible: Boolean? = null,

    @SerializedName("associationId")
    val associationId: String? = null,

    @SerializedName("status_reason")
    val statusReason: String? = null
) : BaseItem(code, validity, type, title, category, status)

@Deprecated("this should be replaced by ContractResponse")
internal data class Bta(

    @SerializedName("code")
    override val code: String? = null,

    @SerializedName("level")
    val level: Int? = null,

    @SerializedName("secure_code")
    val secureCode: String? = null,

    @SerializedName("validity")
    override val validity: Validity? = null,

    @SerializedName("type")
    override val type: String? = null,

    @SerializedName("title")
    override val title: String? = null,

    @SerializedName("category")
    override val category: String? = null,

    @SerializedName("status")
    override val status: Int? = null,

    @SerializedName("is_extensible")
    val isExtensible: Boolean? = null
) : BaseItem(code, validity, type, title, category, status)

@Deprecated("this should be replaced by ContractResponse")
internal data class NacItem(

    @SerializedName("code")
    override val code: String? = null,

    @SerializedName("validity")
    override val validity: Validity? = null,

    @SerializedName("type")
    override val type: String? = null,

    @SerializedName("title")
    override val title: String? = null,

    @SerializedName("category")
    override val category: String? = null,

    @SerializedName("status")
    override val status: Int? = null,

    @SerializedName("is_extensible")
    val isExtensible: Boolean? = null,

    @SerializedName("status_reason")
    val statusReason: String? = null
) : BaseItem(code, validity, type, title, category, status)

@Deprecated("this should be replaced by ContractResponse")
internal data class Validity(

    @SerializedName("start")
    val start: Int? = null,

    @SerializedName("end")
    val end: Int? = null
)

@Deprecated("this should be replaced by ContractResponse")
internal data class ContractServicesItem(

    @SerializedName("code")
    override val code: String? = null,

    @SerializedName("validity")
    override val validity: Validity? = null,

    @SerializedName("label")
    val label: String? = null,

    @SerializedName("type")
    override val type: String? = null,

    @SerializedName("title")
    override val title: String? = null,

    @SerializedName("category")
    override val category: String? = null,

    @SerializedName("status")
    override val status: Int? = null,

    @SerializedName("is_extensible")
    val isExtensible: Boolean? = null,

    @SerializedName("products")
    val products: List<String?>? = null
) : BaseItem(code, validity, type, title, category, status)

@Deprecated("this should be replaced by ContractResponse")
internal data class FlexiLease(

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("status")
    val status: Int? = null
)

@Deprecated("this should be replaced by ContractResponse")
internal data class Tmts(

    @SerializedName("code")
    override val code: String? = null,

    @SerializedName("validity")
    override val validity: Validity? = null,

    @SerializedName("type")
    override val type: String? = null,

    @SerializedName("title")
    override val title: String? = null,

    @SerializedName("category")
    override val category: String? = null,

    @SerializedName("status")
    override val status: Int? = null,

    @SerializedName("is_extensible")
    val isExtensible: Boolean? = null,

    @SerializedName("status_reason")
    val statusReason: String? = null
) : BaseItem(code, validity, type, title, category, status)

@Deprecated("this should be replaced by ContractResponse")
internal data class Dscp(

    @SerializedName("code")
    override val code: String? = null,

    @SerializedName("validity")
    override val validity: Validity? = null,

    @SerializedName("type")
    override val type: String? = null,

    @SerializedName("title")
    override val title: String? = null,

    @SerializedName("category")
    override val category: String? = null,

    @SerializedName("status")
    override val status: Int? = null,

    @SerializedName("is_extensible")
    val isExtensible: Boolean? = null,

    @SerializedName("status_reason")
    val statusReason: String? = null
) : BaseItem(code, validity, type, title, category, status)

@Deprecated("this should be replaced by ContractResponse")
internal data class ClubItem(

    @SerializedName("code")
    override val code: String? = null,

    @SerializedName("validity")
    override val validity: Validity? = null,

    @SerializedName("type")
    override val type: String? = null,

    @SerializedName("title")
    override val title: String? = null,

    @SerializedName("category")
    override val category: String? = null,

    @SerializedName("status")
    override val status: Int? = null,

    @SerializedName("is_extensible")
    val isExtensible: Boolean? = null
) : BaseItem(code, validity, type, title, category, status)

@Deprecated("this should be replaced by ContractResponse")
internal data class Raccess(

    @SerializedName("code")
    override val code: String? = null,

    @SerializedName("fds")
    val fds: List<String?>? = null,

    @SerializedName("validity")
    override val validity: Validity? = null,

    @SerializedName("type")
    override val type: String? = null,

    @SerializedName("title")
    override val title: String? = null,

    @SerializedName("category")
    override val category: String? = null,

    @SerializedName("status")
    override val status: Int? = null,

    @SerializedName("is_extensible")
    val isExtensible: Boolean? = null,

    @SerializedName("associationId")
    val associationId: String? = null,

    @SerializedName("status_reason")
    val statusReason: String? = null
) : BaseItem(code, validity, type, title, category, status)
