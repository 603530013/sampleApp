package com.inetpsa.pims.spaceMiddleware.model.responses.fca.user

import com.google.gson.annotations.SerializedName

internal data class GigyaAccountResponse(
    @SerializedName("UID") val uid: String? = null,
    @SerializedName("UIDSignature") val uidSignature: String? = null,
    @SerializedName("apiVersion") val apiVersion: Int? = null,
    @SerializedName("created") val createdDate: String? = null,
    @SerializedName("createdTimestamp") val createdTimestamp: Long? = null,
    @SerializedName("emails") val emails: Emails? = null,
    @SerializedName("isActive") val isActive: Boolean? = null,
    @SerializedName("isRegistered") val isRegistered: Boolean? = null,
    @SerializedName("isVerified") val isVerified: Boolean? = null,
    @SerializedName("lastLogin") val lastLoginDate: String? = null,
    @SerializedName("lastLoginTimestamp") val lastLoginTimestamp: Long? = null,
    @SerializedName("lastUpdated") val lastUpdated: String? = null,
    @SerializedName("lastUpdatedTimestamp") val lastUpdatedTimestamp: Long? = null,
    @SerializedName("loginProvider") val loginProvider: String? = null,
    @SerializedName("oldestDataUpdated") val oldestDataUpdated: String? = null,
    @SerializedName("oldestDataUpdatedTimestamp") val oldestDataUpdatedTimestamp: Long? = null,
    @SerializedName("profile") val profile: Profile? = null,
    @SerializedName("registered") val registered: String? = null,
    @SerializedName("registeredTimestamp") val registeredTimestamp: Long? = null,
    // @SerializedName("sessionInfo") val sessionInfo: SessionInfo? = null,
    @SerializedName("signatureTimestamp") val signatureTimestamp: Long? = null,
    @SerializedName("socialProviders") val socialProviders: String? = null,
    @SerializedName("verified") val verified: String? = null,
    @SerializedName("verifiedTimestamp") val verifiedTimestamp: Long? = null
) {

    data class Emails(
        @SerializedName("unverified") val unverified: List<String> = ArrayList(),
        @SerializedName("verified") val verified: List<String> = ArrayList()
    )

    data class Profile(
        @SerializedName("activities") val activities: String? = null,
        @SerializedName("address") val address: String? = null,
        @SerializedName("age") val age: Int? = null,
        @SerializedName("bio") val bio: String? = null,
        @SerializedName("birthDay") val birthDay: Int? = null,
        @SerializedName("birthMonth") val birthMonth: Int? = null,
        @SerializedName("birthYear") val birthYear: Int? = null,
        // @SerializedName("certifications") val certifications: List<Certification>? = null,
        @SerializedName("city") val city: String? = null,
        @SerializedName("country") val country: String? = null,
        // @SerializedName("education") val education: List<Education>? = null,
        @SerializedName("educationLevel") val educationLevel: String? = null,
        @SerializedName("email") val email: String? = null,
        // @SerializedName("favorites") val favorites: List<Favorites>? = null,
        @SerializedName("firstName") val firstName: String? = null,
        @SerializedName("followersCounts") val followersCounts: Long? = null,
        @SerializedName("followingCount") val followingCount: Long? = null,
        @SerializedName("gender") val gender: String? = null,
        @SerializedName("hometown") val hometown: String? = null,
        @SerializedName("honors") val honors: String? = null,
        @SerializedName("industry") val industry: String? = null,
        @SerializedName("interestedIn") val interestedIn: String? = null,
        @SerializedName("interests") val interests: String? = null,
        @SerializedName("languages") val languages: String? = null,
        @SerializedName("lastLoginLocation") val lastLoginLocation: Location? = null,
        @SerializedName("lastName") val lastName: String? = null,
        // @SerializedName("activities") val likes: List<Like>? = null,
        @SerializedName("locale") val locale: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("nickname") val nickname: String? = null,
        // @SerializedName("oidcData") val oidcData: OidcData? = null,
        // @SerializedName("patents") val patents: List<Patent>? = null,
        @SerializedName("phones") val phones: List<Phone>? = null,
        @SerializedName("photoURL") val photoURL: String? = null,
        @SerializedName("politicalView") val politicalView: String? = null,
        @SerializedName("professionalHeadline") val professionalHeadline: String? = null,
        @SerializedName("profileURL") val profileURL: String? = null,
        @SerializedName("proxyEmail") val proxyEmail: String? = null,
        // @SerializedName("publications") val publications: List<Publication>? = null,
        @SerializedName("relationshipStatus") val relationshipStatus: String? = null,
        @SerializedName("religion") val religion: String? = null,
        // @SerializedName("skills") val skills: List<Skill>? = null,
        @SerializedName("specialities") val specialities: String? = null,
        @SerializedName("state") val state: String? = null,
        @SerializedName("thumbnailURL") val thumbnailURL: String? = null,
        @SerializedName("timezone") val timezone: String? = null,
        @SerializedName("username") val username: String? = null,
        @SerializedName("verified") val verified: String? = null,
        // @SerializedName("work") val work: List<Work>? = null,
        @SerializedName("zip") val zip: String? = null
    ) {

        data class Phone(
            @SerializedName("default") val default: String? = null,
            @SerializedName("number") val number: String? = null,
            @SerializedName("type") val type: String? = null
        )

        data class Location(
            @SerializedName("city") val city: String? = null,
            @SerializedName("country") val country: String? = null,
            @SerializedName("state") val state: String? = null,
            @SerializedName("coordinates") val coordinates: Coordinates? = null
        ) {

            data class Coordinates(
                @SerializedName("lat") var latitude: Float? = null,
                @SerializedName("lon") var longitude: Float? = null
            )
        }
    }
}
