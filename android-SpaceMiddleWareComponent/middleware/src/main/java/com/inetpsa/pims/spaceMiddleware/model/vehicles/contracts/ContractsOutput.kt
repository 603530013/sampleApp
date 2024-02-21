package com.inetpsa.pims.spaceMiddleware.model.vehicles.contracts

import com.google.gson.annotations.SerializedName

@Suppress("LongParameterList")
internal data class ContractsOutput(val contracts: List<BaseItem>) {

    enum class Status {
        @SerializedName("active")
        Active,

        @SerializedName("terminatedExpired")
        TerminatedExpired,

        @SerializedName("pendingAssociation")
        PendingAssociation,

        @SerializedName("pendingActivation")
        PendingActivation,

        @SerializedName("pendingSubscription")
        PendingSubscription,

        @SerializedName("cancelled")
        Cancelled,

        @SerializedName("deactivated")
        Deactivated,

        @SerializedName("terminated")
        Terminated
    }

    internal class RemoteLev(
        code: String?,
        validity: Validity?,
        title: String?,
        category: String?,
        status: Status?,
        val fds: List<String?>?,
        val isExtensible: Boolean?,
        val associationId: String?,
        val statusReason: String?
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
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
        code: String?,
        validity: Validity?,
        title: String?,
        category: String?,
        status: Status?,
        val level: Int?,
        val secureCode: String?,
        val isExtensible: Boolean?
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
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
        code: String?,
        validity: Validity?,
        title: String?,
        category: String?,
        status: Status?,
        val topMainImage: String?,
        val hasFreeTrial: Boolean?,
        val isExtensible: Boolean?,
        val statusReason: String?,
        val subscriptionTechCreationDate: String?,
        val urlSso: String?,
        val associationId: Any?
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
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

    internal class ServicesItem(
        code: String?,
        validity: Validity?,
        title: String?,
        category: String?,
        status: Status?,
        val label: String?,
        val isExtensible: Boolean?,
        val products: List<String?>?
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
        title = title,
        category = category,
        status = status
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ServicesItem) return false
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
        code: String?,
        validity: Validity?,
        title: String?,
        category: String?,
        status: Status?,
        val topMainImage: String?,
        val hasFreeTrial: Boolean?,
        val isExtensible: Boolean?,
        val urlSso: String?,
        val associationId: Any?,
        val subscriptionTechCreationDate: String?,
        val statusReason: String?
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
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
        code: String?,
        validity: Validity?,
        title: String?,
        category: String?,
        status: Status?,
        val isExtensible: Boolean?,
        val statusReason: String?
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
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
        code: String?,
        validity: Validity?,
        title: String?,
        category: String?,
        status: Status?,
        val isExtensible: Boolean?
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
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
        code: String?,
        validity: Validity?,
        title: String?,
        category: String?,
        status: Status?,
        val fds: List<String?>?,
        val isExtensible: Boolean?,
        val associationId: String?,
        val statusReason: String?
    ) : ExtraBaseItem(
        code = code,
        validity = validity,
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
        code: String?,
        open val validity: Validity?,
        open val category: String?,
        status: Status?,
        title: String?
    ) : BaseItem(code, status, title) {

        internal data class Validity(
            val start: Int?,
            val end: Int?
        )

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ExtraBaseItem) return false
            if (!super.equals(other)) return false

            if (validity != other.validity) return false
            if (category != other.category) return false

            return true
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (validity?.hashCode() ?: 0)
            result = 31 * result + (category?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "ExtraBaseItem(" +
                "validity=$validity, " +
                "category=$category" +
                ")" +
                " ${super.toString()}"
        }
    }

    internal open class BaseItem(
        open val code: String?,
        open val status: Status?,
        open val title: String?
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is BaseItem) return false

            if (code != other.code) return false
            if (status != other.status) return false
            if (title != other.title) return false

            return true
        }

        override fun hashCode(): Int {
            var result = code?.hashCode() ?: 0
            result = 31 * result + (status?.ordinal ?: 0)
            result = 31 * result + (title?.hashCode() ?: 0)
            return result
        }

        override fun toString(): String {
            return "BaseItem(" +
                "code=$code, " +
                "status=$status, " +
                "title=$title" +
                ")"
        }
    }
}
