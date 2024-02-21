package com.inetpsa.pims.spaceMiddleware.executor.vehicle.mapper

import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse.ServicesConnected
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse.ServicesConnected.Offer
import com.inetpsa.pims.spaceMiddleware.model.responses.psa.VehicleDetailsResponse.ServicesConnected.Offer.Price
import com.inetpsa.pims.spaceMiddleware.model.vehicles.service.ServicesOutput
import com.inetpsa.pims.spaceMiddleware.model.vehicles.service.ServicesOutput.Service
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class VehicleServicesPsaMapperTest {

    private val sampleURL =
        "https://www.example.com/login?vin=[VIN]&token=[TOKEN_CVS]&schema=[Mymark]"
    private val sampleURL2 =
        "https://www.sample.com/login?vin=[VIN]&token=[TOKEN_CVS]&schema=[Mymark]"
    private lateinit var vehicleServicesPsaMapper: VehicleServicesPsaMapper

    @Before
    fun setUp() {
        vehicleServicesPsaMapper = VehicleServicesPsaMapper()
    }

    @Test
    fun `test transformService() returns correct service`() {
        val encryptedVin = "encryptedVin"
        val replacedSchema = "replacedSchema"
        val replacedToken = "replacedToken"
        val expectedOutput = getExpectedServiceOutput(
            false,
            expectedTransformedURL =
            "https://www.example.com/login?vin=encryptedVin&token=replacedToken&schema=replacedSchema"
        )
        val result =
            vehicleServicesPsaMapper.transformService(
                createServiceConnected(sampleURL, false),
                replacedSchema,
                encryptedVin,
                replacedToken
            )
        assertEquals(expectedOutput, result)
    }

    @Test
    fun `test transformService() when offer is null`() {
        val encryptedVin = "encryptedVin"
        val replacedSchema = "replacedSchema"
        val replacedToken = "replacedToken"
        val expectedOutput = getExpectedServiceOutput(
            true,
            expectedTransformedURL =
            "https://www.example.com/login?vin=encryptedVin&token=replacedToken&schema=replacedSchema"
        )
        val result =
            vehicleServicesPsaMapper.transformService(
                createServiceConnected(sampleURL, true),
                replacedSchema,
                encryptedVin,
                replacedToken
            )
        assertEquals(expectedOutput, result)
    }

    @Test
    fun `test transformService() with all null parameters`() {
        val encryptedVin = null
        val replacedSchema = null
        val replacedToken = null
        val expectedOutput = getExpectedServiceOutput(
            false,
            expectedTransformedURL =
            "https://www.example.com/login?vin=&token=&schema="
        )
        val result =
            vehicleServicesPsaMapper.transformService(
                createServiceConnected(sampleURL, false),
                replacedSchema,
                encryptedVin,
                replacedToken
            )
        assertEquals(expectedOutput, result)
    }

    @Test
    fun `test transformUrl() returns correct transformed URL`() {
        val encryptedVin = "encryptedVin"
        val replacedSchema = "replacedSchema"
        val replacedToken = "replacedToken"
        val expectedTransformedURL =
            "https://www.example.com/login?vin=encryptedVin&token=replacedToken&schema=replacedSchema"
        val transformedURL = vehicleServicesPsaMapper.transformUrl(
            sampleURL,
            replacedSchema,
            encryptedVin,
            replacedToken
        )
        assertNotEquals(sampleURL, transformedURL)
        assertEquals(expectedTransformedURL, transformedURL)
    }

    @Test
    fun `test transformUrl() with all null parameters`() {
        val encryptedVin = null
        val replacedSchema = null
        val replacedToken = null
        val expectedTransformedURL =
            "https://www.example.com/login?vin=&token=&schema="
        val transformedURL = vehicleServicesPsaMapper.transformUrl(
            sampleURL,
            replacedSchema,
            encryptedVin,
            replacedToken
        )
        assertNotEquals(sampleURL, transformedURL)
        assertEquals(expectedTransformedURL, transformedURL)
    }

    @Test
    fun `test transformUrl() returns null when URL is null`() {
        val encryptedVin = "encryptedVin"
        val replacedSchema = "replacedSchema"
        val replacedToken = "replacedToken"
        val transformedURL = vehicleServicesPsaMapper.transformUrl(
            null,
            replacedSchema,
            encryptedVin,
            replacedToken
        )
        assertNull(transformedURL)
    }

    @Test
    fun `test transform() returns empty list when list(ServiceConnected) is null`() {
        val expectedServiceOutput = ServicesOutput(emptyList())
        val result = vehicleServicesPsaMapper.transform(null, "", "", "")
        assertNotNull(result)
        assertEquals(expectedServiceOutput, result)
    }

    @Test
    fun `test transform() with list of service`() {
        val expectedTransformedURL = "https://www.example.com/login?vin=&token=&schema="
        val expectedServiceOutput = ServicesOutput(
            listOf(
                getExpectedServiceOutput(
                    false,
                    expectedTransformedURL
                )
            )
        )
        val result = vehicleServicesPsaMapper.transform(
            listOf(createServiceConnected(sampleURL, false)),
            "",
            "",
            ""
        )
        assertEquals(expectedServiceOutput, result)
        assertEquals(expectedTransformedURL, result.services?.get(0)?.url)
    }

    @Test
    fun `test transform() with valid parameters returns ServicesOutput with transformed url`() {
        val encryptedVin = "encryptedVin"
        val replacedSchema = "replacedSchema"
        val replacedToken = "replacedToken"
        val expectedTransformedURL =
            "https://www.example.com/login?vin=encryptedVin&token=replacedToken&schema=replacedSchema"
        val expectedServiceOutput = ServicesOutput(
            listOf(
                getExpectedServiceOutput(
                    false,
                    expectedTransformedURL
                )
            )
        )
        val result = vehicleServicesPsaMapper.transform(
            listOf(createServiceConnected(sampleURL, false)),
            replacedSchema,
            encryptedVin,
            replacedToken
        )
        assertEquals(expectedServiceOutput, result)
        assert(result.services?.size == 1)
        assertEquals(expectedTransformedURL, result.services?.get(0)?.url)
    }

    @Test
    fun `test transform() with list(serviceConnected) size is more than 1`() {
        val encryptedVin = "encryptedVin"
        val replacedSchema = "replacedSchema"
        val replacedToken = "replacedToken"
        val expectedTransformedURL =
            "https://www.example.com/login?vin=encryptedVin&token=replacedToken&schema=replacedSchema"
        val expectedTransformedURL2 =
            "https://www.sample.com/login?vin=encryptedVin&token=replacedToken&schema=replacedSchema"
        val expectedServiceOutput = ServicesOutput(
            listOf(
                getExpectedServiceOutput(
                    false,
                    expectedTransformedURL
                ),
                getExpectedServiceOutput(
                    false,
                    expectedTransformedURL2
                )
            )
        )
        val result = vehicleServicesPsaMapper.transform(
            listOf(
                createServiceConnected(sampleURL, false),
                createServiceConnected(sampleURL2, false)
            ),
            replacedSchema,
            encryptedVin,
            replacedToken
        )
        assertEquals(expectedServiceOutput, result)
        assert(result.services?.size == 2)
        assertEquals(expectedTransformedURL, result.services?.get(0)?.url)
        assertEquals(expectedTransformedURL2, result.services?.get(1)?.url)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    /**
     * A method returns a sample ServiceConnected object for testing
     */
    private fun createServiceConnected(urlCVS: String, isOfferNull: Boolean): ServicesConnected {
        return ServicesConnected(
            id = "testServiceConnectedId",
            title = "testServiceConnectedTitle",
            category = "testServiceConnectedCategory",
            description = "testServiceConnectedDescription",
            url = urlCVS,
            urlSso = "testServiceConnectedUrlSso",
            urlCvs = urlCVS,
            price = 10f,
            currency = "Euro",
            offer = if (isOfferNull) {
                null
            } else {
                Offer(
                    "",
                    0.1f,
                    Price("", 10f, "", ""),
                    0
                )
            }
        )
    }

    /**
     * A method which returns expected Service for testing
     */
    private fun getExpectedServiceOutput(isOfferNull: Boolean, expectedTransformedURL: String): Service {
        val offer = Service.Offer(
            "",
            0.1f,
            Service.Offer.Price("", 10f, "", ""),
            0
        )
        return Service(
            id = "testServiceConnectedId",
            url = expectedTransformedURL,
            title = "testServiceConnectedTitle",
            category = "testServiceConnectedCategory",
            description = "testServiceConnectedDescription",
            price = 10f,
            currency = "Euro",
            offer = if (isOfferNull) null else offer
        )
    }
}
