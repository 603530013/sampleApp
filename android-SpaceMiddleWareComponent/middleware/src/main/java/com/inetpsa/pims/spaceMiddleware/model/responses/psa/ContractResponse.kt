package com.inetpsa.pims.spaceMiddleware.model.responses.psa

import com.google.gson.annotations.SerializedName

@Suppress("LongParameterList")
internal data class ContractResponse(
    @SerializedName("nac") val nac: List<NacItem?>? = null,
    @SerializedName("remoteLev") val remoteLev: List<RemoteLev?>? = null,
    @SerializedName("flexiLease") val flexiLease: List<BaseItem?>? = null,
    @SerializedName("club") val club: List<ClubItem?>? = null,
    @SerializedName("services") val services: List<ContractServicesItem?>? = null,
    @SerializedName("bta") val bta: List<Bta?>? = null,
    @SerializedName("tmts") val tmts: List<Tmts?>? = null,
    @SerializedName("dscp") val dscp: List<Dscp?>? = null,
    @SerializedName("raccess") val raccess: List<RAccess?>? = null
) {

    internal companion object {

        const val STATUS_ACTIVE = 2
        const val STATUS_TERMINATED_EXPIRED = 8
        const val STATUS_PENDING_ACTIVATION = 101
        const val STATUS_PENDING_ASSOCIATION = 102
        const val STATUS_PENDING_SUBSCRIPTION = 103
        const val STATUS_SAMS_CANCELLED = 104
        const val STATUS_DEACTIVATED = 111
        const val STATUS_TERMINATED = 108
        const val STATUS_CANCELLED = 6
        const val STATUS_SAMS_EXPIRED = 106
        const val STATUS_SAMS_EXPIRED_IN = 107
    }

    internal class RemoteLev(
        code: String? = null,
        validity: Validity? = null,
        type: String? = null,
        title: String? = null,
        category: String? = null,
        status: Int? = null,
        @SerializedName("fds") val fds: List<String?>? = null,
        @SerializedName("isExtensible", alternate = ["is_extensible"]) val isExtensible: Boolean? = null,
        @SerializedName("associationId") val associationId: String? = null,
        @SerializedName("statusReason", alternate = ["status_reason"]) val statusReason: String? = null
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
        type = type,
        title = title,
        category = category,
        status = status
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is RemoteLev) return false
            if (!super.equals(other)) return false

            if (fds != other.fds) return false
            if (isExtensible != other.isExtensible) return false
            if (associationId != other.associationId) return false
            if (statusReason != other.statusReason) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (fds?.hashCode() ?: 0)
            result = 31 * result + (isExtensible?.hashCode() ?: 0)
            result = 31 * result + (associationId?.hashCode() ?: 0)
            result = 31 * result + (statusReason?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "RemoteLev(" +
                "fds=$fds, " +
                "isExtensible=$isExtensible, " +
                "associationId=$associationId, " +
                "statusReason=$statusReason" +
                ")" +
                " ${super.toString()}"
        }
    }

    internal class Bta(
        code: String? = null,
        validity: Validity? = null,
        type: String? = null,
        title: String? = null,
        category: String? = null,
        status: Int? = null,
        @SerializedName("level") val level: Int? = null,
        @SerializedName("secureCode", alternate = ["secure_code"]) val secureCode: String? = null,
        @SerializedName("isExtensible", alternate = ["is_extensible"]) val isExtensible: Boolean? = null
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
        type = type,
        title = title,
        category = category,
        status = status
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Bta) return false
            if (!super.equals(other)) return false

            if (level != other.level) return false
            if (secureCode != other.secureCode) return false
            if (isExtensible != other.isExtensible) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (level ?: 0)
            result = 31 * result + (secureCode?.hashCode() ?: 0)
            result = 31 * result + (isExtensible?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "Bta(" +
                "level=$level, " +
                "secureCode=$secureCode, " +
                "isExtensible=$isExtensible" +
                ")" +
                " ${super.toString()}"
        }
    }

    internal class NacItem(
        code: String? = null,
        validity: Validity? = null,
        type: String? = null,
        title: String? = null,
        category: String? = null,
        status: Int? = null,
        @SerializedName("topMainImage") val topMainImage: String? = null,
        @SerializedName("hasFreeTrial") val hasFreeTrial: Boolean? = null,
        @SerializedName("isExtensible", alternate = ["is_extensible"]) val isExtensible: Boolean? = null,
        @SerializedName("statusReason", alternate = ["status_reason"]) val statusReason: String? = null,
        @SerializedName("subscriptionTechCreationDate") val subscriptionTechCreationDate: String? = null,
        @SerializedName("urlSso") val urlSso: String? = null,
        @SerializedName("associationId") val associationId: Any? = null
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
        type = type,
        title = title,
        category = category,
        status = status
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is NacItem) return false
            if (!super.equals(other)) return false

            if (topMainImage != other.topMainImage) return false
            if (hasFreeTrial != other.hasFreeTrial) return false
            if (isExtensible != other.isExtensible) return false
            if (statusReason != other.statusReason) return false
            if (subscriptionTechCreationDate != other.subscriptionTechCreationDate) return false
            if (urlSso != other.urlSso) return false
            if (associationId != other.associationId) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (topMainImage?.hashCode() ?: 0)
            result = 31 * result + (hasFreeTrial?.hashCode() ?: 0)
            result = 31 * result + (isExtensible?.hashCode() ?: 0)
            result = 31 * result + (statusReason?.hashCode() ?: 0)
            result = 31 * result + (subscriptionTechCreationDate?.hashCode() ?: 0)
            result = 31 * result + (urlSso?.hashCode() ?: 0)
            result = 31 * result + (associationId?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "NacItem(" +
                "topMainImage=$topMainImage, " +
                "hasFreeTrial=$hasFreeTrial, " +
                "isExtensible=$isExtensible, " +
                "statusReason=$statusReason, " +
                "subscriptionTechCreationDate=$subscriptionTechCreationDate, " +
                "urlSso=$urlSso, " +
                "associationId=$associationId" +
                ")" +
                " ${super.toString()}"
        }
    }

    internal class ContractServicesItem(
        code: String? = null,
        validity: Validity? = null,
        type: String? = null,
        title: String? = null,
        category: String? = null,
        status: Int? = null,
        @SerializedName("label") val label: String? = null,
        @SerializedName("isExtensible", alternate = ["is_extensible"]) val isExtensible: Boolean? = null,
        @SerializedName("products") val products: List<String?>? = null
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
        type = type,
        title = title,
        category = category,
        status = status
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ContractServicesItem) return false
            if (!super.equals(other)) return false

            if (label != other.label) return false
            if (isExtensible != other.isExtensible) return false
            if (products != other.products) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (label?.hashCode() ?: 0)
            result = 31 * result + (isExtensible?.hashCode() ?: 0)
            result = 31 * result + (products?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "ContractServicesItem(" +
                "label=$label, " +
                "isExtensible=$isExtensible, " +
                "products=$products" +
                ")" +
                " ${super.toString()}"
        }
    }

    internal class Tmts(
        code: String? = null,
        validity: Validity? = null,
        type: String? = null,
        title: String? = null,
        category: String? = null,
        status: Int? = null,
        @SerializedName("topMainImage") val topMainImage: String? = null,
        @SerializedName("hasFreeTrial") val hasFreeTrial: Boolean? = null,
        @SerializedName("isExtensible", alternate = ["is_extensible"]) val isExtensible: Boolean? = null,
        @SerializedName("urlSso") val urlSso: String? = null,
        @SerializedName("associationId") val associationId: Any? = null,
        @SerializedName("subscriptionTechCreationDate") val subscriptionTechCreationDate: String? = null,
        @SerializedName("statusReason", alternate = ["status_reason"]) val statusReason: String? = null
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
        type = type,
        title = title,
        category = category,
        status = status
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Tmts) return false
            if (!super.equals(other)) return false

            if (topMainImage != other.topMainImage) return false
            if (hasFreeTrial != other.hasFreeTrial) return false
            if (isExtensible != other.isExtensible) return false
            if (urlSso != other.urlSso) return false
            if (associationId != other.associationId) return false
            if (subscriptionTechCreationDate != other.subscriptionTechCreationDate) return false
            if (statusReason != other.statusReason) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (topMainImage?.hashCode() ?: 0)
            result = 31 * result + (hasFreeTrial?.hashCode() ?: 0)
            result = 31 * result + (isExtensible?.hashCode() ?: 0)
            result = 31 * result + (urlSso?.hashCode() ?: 0)
            result = 31 * result + (associationId?.hashCode() ?: 0)
            result = 31 * result + (subscriptionTechCreationDate?.hashCode() ?: 0)
            result = 31 * result + (statusReason?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "Tmts(" +
                "topMainImage=$topMainImage, " +
                "hasFreeTrial=$hasFreeTrial, " +
                "isExtensible=$isExtensible, " +
                "urlSso=$urlSso, " +
                "associationId=$associationId, " +
                "subscriptionTechCreationDate=$subscriptionTechCreationDate, " +
                "statusReason=$statusReason" +
                ")" +
                " ${super.toString()}"
        }
    }

    internal class Dscp(
        code: String? = null,
        validity: Validity? = null,
        type: String? = null,
        title: String? = null,
        category: String? = null,
        status: Int? = null,
        @SerializedName("isExtensible", alternate = ["is_extensible"]) val isExtensible: Boolean? = null,
        @SerializedName("statusReason", alternate = ["status_reason"]) val statusReason: String? = null
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
        type = type,
        title = title,
        category = category,
        status = status
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Dscp) return false
            if (!super.equals(other)) return false

            if (isExtensible != other.isExtensible) return false
            if (statusReason != other.statusReason) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (isExtensible?.hashCode() ?: 0)
            result = 31 * result + (statusReason?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "Dscp(" +
                "isExtensible=$isExtensible, " +
                "statusReason=$statusReason" +
                ")" +
                " ${super.toString()}"
        }
    }

    internal class ClubItem(
        code: String? = null,
        validity: Validity? = null,
        type: String? = null,
        title: String? = null,
        category: String? = null,
        status: Int? = null,
        @SerializedName("isExtensible", alternate = ["is_extensible"]) val isExtensible: Boolean? = null
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
        type = type,
        title = title,
        category = category,
        status = status
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ClubItem) return false
            if (!super.equals(other)) return false

            if (isExtensible != other.isExtensible) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (isExtensible?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "ClubItem(" +
                "isExtensible=$isExtensible" +
                ")" +
                " ${super.toString()}"
        }
    }

    internal class RAccess(
        code: String? = null,
        validity: Validity? = null,
        type: String? = null,
        title: String? = null,
        category: String? = null,
        status: Int? = null,
        @SerializedName("fds") val fds: List<String?>? = null,
        @SerializedName("isExtensible", alternate = ["is_extensible"]) val isExtensible: Boolean? = null,
        @SerializedName("associationId") val associationId: String? = null,
        @SerializedName("statusReason", alternate = ["status_reason"]) val statusReason: String? = null
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
        type = type,
        title = title,
        category = category,
        status = status
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is RAccess) return false
            if (!super.equals(other)) return false

            if (fds != other.fds) return false
            if (isExtensible != other.isExtensible) return false
            if (associationId != other.associationId) return false
            if (statusReason != other.statusReason) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (fds?.hashCode() ?: 0)
            result = 31 * result + (isExtensible?.hashCode() ?: 0)
            result = 31 * result + (associationId?.hashCode() ?: 0)
            result = 31 * result + (statusReason?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "RAccess(" +
                "fds=$fds, " +
                "isExtensible=$isExtensible, " +
                "associationId=$associationId, " +
                "statusReason=$statusReason" +
                ")" +
                " ${super.toString()}"
        }
    }

    internal open class ExtraBaseItem(
        code: String? = null,
        @SerializedName("validity") open val validity: Validity? = null,
        type: String? = null,
        @SerializedName("category") open val category: String? = null,
        status: Int? = null,
        title: String? = null
    ) : BaseItem(code = code, status = status, title = title, type = type) {

        internal data class Validity(
            @SerializedName("start") val start: Int? = null,
            @SerializedName("end") val end: Int? = null
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ExtraBaseItem) return false
            if (!super.equals(other)) return false

            if (validity != other.validity) return false
            if (type != other.type) return false
            if (category != other.category) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (validity?.hashCode() ?: 0)
            result = 31 * result + (type?.hashCode() ?: 0)
            result = 31 * result + (category?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "ExtraBaseItem(" +
                "validity=$validity, " +
                "type=$type, " +
                "category=$category" +
                ")" +
                " ${super.toString()}"
        }
    }

    internal open class BaseItem(
        @SerializedName("code") open val code: String? = null,
        @SerializedName("status") open val status: Int? = null,
        @SerializedName("title") open val title: String? = null,
        @SerializedName("type") open val type: String? = null
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is BaseItem) return false

            if (code != other.code) return false
            if (status != other.status) return false
            if (title != other.title) return false
            if (type != other.type) return false

            return true
        }

        override fun hashCode(): Int {
            var result = code?.hashCode() ?: 0
            result = 31 * result + (status ?: 0)
            result = 31 * result + (title?.hashCode() ?: 0)
            result = 31 * result + (type?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "BaseItem(" +
                "code=$code, " +
                "status=$status, " +
                "title=$title" +
                "type=$type" +
                ")"
        }
    }
}
