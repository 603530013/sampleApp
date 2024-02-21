package com.inetpsa.pims.spaceMiddleware.executor.features.mapper

import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Config
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Config.DaysType
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Config.Engine.ICE
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Config.Protocol.NETWORK
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Config.Type.INTERNAL
import com.inetpsa.pims.spaceMiddleware.model.features.FeaturesOutput.Feature.Status
import com.inetpsa.pims.spaceMiddleware.model.responses.fca.VehicleResponse
import io.mockk.clearAllMocks
import io.mockk.spyk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class FeaturesFcaOutputMapperTest {

    private lateinit var mapper: FeaturesFcaOutputMapper

    private val vehicleResponse: VehicleResponse = VehicleResponse(
        activationSource = "testActivationSource",
        brandCode = "testBrandCode",
        channelFeatures = listOf(
            VehicleResponse.ChannelFeature(
                channels = listOf("testChannel"),
                featureCode = "testFeatureCode"
            )
        ),
        color = "testColor",
        company = "testCompany",
        customerRegStatus = "testCustomerRegStatus",
        enrollmentStatus = "testEnrollmentStatus",
        fuelType = "testFuelType",
        isCompanyCar = false,
        language = "testLanguage",
        make = "testMake",
        market = "testMarket",
        model = "testModel",
        modelDescription = "testModelDescription",
        navEnabledHU = false,
        nickname = "testNickname",
        pp = VehicleResponse.Pp(VehicleResponse.Activation(status = "testStatus", version = "testVersion")),
        privacyMode = "testPrivacyMode",
        radio = "testRadio",
        regStatus = "testRegStatus",
        regTimestamp = 10L,
        sdp = "testSdp",
        services = listOf(
            VehicleResponse.Service(service = "testService1", serviceEnabled = true, vehicleCapable = true)
        ),
        soldRegion = "testSoldRegion",
        subMake = "testSubMake",
        svla = VehicleResponse.Svla(status = "testStatus", timestamp = 10L),
        tc = VehicleResponse.Tc(
            activation = VehicleResponse.Activation(
                status = "testStatus",
                version = "testVersion"
            ),
            registration = VehicleResponse.Tc.Registration(
                status = "testStatus",
                version = "testVersion"
            )
        ),
        tcuType = "testTcuType",
        tsoBodyCode = "testTsoBodyCode",
        tsoModelYear = "testTsoModelYear",
        vin = "testVin",
        year = 1986,
        imageUrl = "testImageUrl"
    )

    private val featuresOutput = FeaturesOutput(
        features = listOf(
            Feature(
                code = "code",
                value = "testService1",
                config = Config(
                    schedule = 1,
                    shared = true,
                    repeat = true,
                    daysType = listOf(DaysType.WORK_DAYS),
                    version = 1F,
                    engine = ICE,
                    type = INTERNAL,
                    protocol = NETWORK
                ),
                status = Status.CAPABLE
            )
        )
    )

    @Before
    fun setup() {
        mapper = spyk(FeaturesFcaOutputMapper(vehicleResponse))
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when execute transformFeatureOutput method`() {
        val features = vehicleResponse.services.orEmpty()
            .filter { it.vehicleCapable }
            .mapNotNull { mapper.transformOutput(it) }
            .flatten()
        val featuresOutput = FeaturesOutput(features)
        assertEquals(featuresOutput, mapper.transformFeatureOutput())
    }

    @Test
    fun `when execute transformOutput method with null service value`() {
        val service = vehicleResponse.services.orEmpty().first()
        assertEquals(null, mapper.transformOutput(service))
    }

    @Test
    fun `when execute transformOutput method with invalid service`() {
        val service = VehicleResponse.Service(
            service = "invalidService",
            serviceEnabled = false,
            vehicleCapable = true
        )
        val actualFeatures = mapper.transformOutput(service)
        assertNull(actualFeatures)
    }

    @Test
    fun `when execute transformOutput method with valid service`() {
        val expectedFeatureOutput = listOf(
            Feature(
                code = "ENGINE_ON",
                value = "REON",
                config = Config(
                    version = 1F
                ),
                status = Status.ENABLE
            )
        )

        val service = VehicleResponse.Service(
            vehicleCapable = true,
            service = "REON",
            serviceEnabled = true

        )
        val actualFeatures = mapper.transformOutput(service)
        assertEquals(expectedFeatureOutput, actualFeatures)
    }
}
