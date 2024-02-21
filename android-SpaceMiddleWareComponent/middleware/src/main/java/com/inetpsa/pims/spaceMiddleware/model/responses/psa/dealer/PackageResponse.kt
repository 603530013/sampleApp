package com.inetpsa.pims.spaceMiddleware.model.responses.psa.dealer

import com.google.gson.annotations.SerializedName

internal class PackageResponse(
    @SerializedName("vin") val vin: String? = null,
    @SerializedName("rrdi") val rrdi: String? = null,
    @SerializedName("operations") val operations: List<Operations>? = null
) {

    internal data class Operations(
        @SerializedName("code") val code: String? = null,
        @SerializedName("icon") val icon: String? = null,
        @SerializedName("title") val title: String? = null,
        @SerializedName("type") val type: Int? = null,
        @SerializedName("maintenance") val maintenance: Int? = null,
        @SerializedName("security") val security: Int? = null,
        @SerializedName("packages") val packages: List<Packages>? = null
    ) {

        companion object {

            /**
             * Type for group of package (with prices)
             */
            const val TYPE_PACKAGE = 0

            /**
             * Type for group of packages related to a car maintenance step
             */
            const val TYPE_MAINTENANCE = 1

            /**
             * Type for interventions (no prices, and no packages)
             */
            const val TYPE_INTERVENTION = 2
            const val TYPE_ALL = 100
        }

        internal data class Packages(
            @SerializedName("reference") val reference: String? = null,
            @SerializedName("reference_tp") val referenceTp: String? = null,
            @SerializedName("reference_ct") val referenceCt: String? = null,
            @SerializedName("title") val title: String? = null,
            @SerializedName("nature") val nature: String? = null,
            @SerializedName("type_btob") val typeBtob: Boolean? = null,
            @SerializedName("type_fdz") val typeFdz: String? = null,
            @SerializedName("type_di") val typeDi: String? = null,
            @SerializedName("price") val price: Float? = null,
            @SerializedName("is_any_pg_in_brr") val isAnyPgInBrr: Boolean? = null,
            @SerializedName("type") val type: Int? = null,
            @SerializedName("validity") val validity: Validity? = null,
            @SerializedName("sub_packages") val subPackages: List<String>? = null,
            @SerializedName("description") val description: List<String>? = null
        ) {

            internal data class Validity(
                @SerializedName("start") val start: Long? = null,
                @SerializedName("end") val end: Long? = null
            )
        }
    }
}
