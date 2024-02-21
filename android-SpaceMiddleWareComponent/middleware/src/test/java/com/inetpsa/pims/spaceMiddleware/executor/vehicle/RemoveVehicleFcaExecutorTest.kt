package com.inetpsa.pims.spaceMiddleware.executor.vehicle

import com.inetpsa.mmx.foundation.data.IDataManager
import com.inetpsa.mmx.foundation.extensions.toJson
import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse
import com.inetpsa.mmx.foundation.networkManager.NetworkResponse.Success
import com.inetpsa.mmx.foundation.tools.FCAApiKey
import com.inetpsa.mmx.foundation.tools.StoreMode.APPLICATION
import com.inetpsa.mmx.foundation.tools.StoreMode.SECURE
import com.inetpsa.mmx.foundation.tools.TokenType
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.executor.FcaExecutorTestHelper
import com.inetpsa.pims.spaceMiddleware.model.vehicles.remove.RemoveVehicleInput
import com.inetpsa.pims.spaceMiddleware.util.PimsErrors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.Instant

@Deprecated("try to switch to use this class SetRemoveVehiclePsaExecutorTest")
internal class RemoveVehicleFcaExecutorTest : FcaExecutorTestHelper() {

    private lateinit var executor: RemoveVehicleFcaExecutor

    private val vehicleParams = RemoveVehicleInput(
        vin = "testVin",
        reason = "test_reason",
        reasonId = "10"
    )

    @Before
    override fun setup() {
        super.setup()
        executor = spyk(RemoveVehicleFcaExecutor(baseCommand))
        every { executor.now() } returns Instant.ofEpochMilli(nowMilliseconds)
    }

    @Test
    fun `when execute then make a delete API call`() {
        val pin = "testPin"
        coEvery { communicationManager.delete<Unit>(any(), any()) } returns NetworkResponse.Success(Unit)
        coEvery { executor.getPinToken() } returns pin

        val reason = mapOf(
            Constants.PARAM_REASON_ID to vehicleParams.reasonId,
            Constants.PARAM_REASON to vehicleParams.reason
        ).filterValues { it?.isNotBlank() == true }

        val bodyInfoFca = mapOf(
            Constants.PARAM_PIN_AUTH to pin,
            Constants.PARAM_REASON to reason
        ).toJson()

        runTest {
            val response = executor.execute(vehicleParams)

            verify {
                executor.request(
                    type = eq(Unit::class.java),
                    urls = eq(arrayOf("/v1/accounts/", uid, "/vehicles/", vehicleParams.vin, "/")),
                    body = eq(bodyInfoFca)
                )
            }

            coVerify {
                communicationManager.delete<Unit>(
                    request = any(),
                    tokenType = eq(TokenType.AWSToken(FCAApiKey.SDP))
                )
            }

            Assert.assertEquals(true, response is Success)
            val success = (response as Success).response
            Assert.assertEquals(Unit, success)
        }
    }

    @Test
    fun `when execute params with the right vin, right reason and right reasonId then return vin`() {
        val vin = "testVin"
        val reason = "delete"
        val reasonId = "0"
        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAM_REASON to reason,
            Constants.PARAM_REASON_ID to reasonId
        )
        val output = RemoveVehicleInput(vin, reason, reasonId)

        val param = executor.params(input)

        Assert.assertEquals(output, param)
    }

    @Test
    fun `when execute params with missing params then throw missing parameter`() {
        val vin = " "
        val reason = "delete"
        val reasonId = "0"
        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAM_REASON to reason,
            Constants.PARAM_REASON_ID to reasonId
        )
        val exception = PIMSFoundationError.missingParameter(Constants.PARAM_VIN)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when execute params with invalid reason then throw invalid parameter`() {
        val vin = "testVin"
        val reason = "invalid"
        val reasonId = "0"
        val input = mapOf(
            Constants.PARAM_VIN to vin,
            Constants.PARAM_REASON to reason,
            Constants.PARAM_REASON_ID to reasonId
        )

        val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_REASON)

        try {
            executor.params(input)
        } catch (ex: PIMSError) {
            Assert.assertEquals(exception.code, ex.code)
            Assert.assertEquals(exception.message, ex.message)
        }
    }

    @Test
    fun `when getPinToken then return pin token`() {
        val dataManager: IDataManager = mockk()
        val expiryData = Instant.MAX
        every { middlewareComponent.dataManager } returns dataManager
        every { dataManager.read(Constants.STORAGE_KEY_PIN_TOKEN, SECURE) } returns "test_pinToken"
        every { dataManager.read(Constants.STORAGE_KEY_PIN_TOKEN_EXPIRY, APPLICATION) } returns expiryData.toString()

        val actual = executor.getPinToken()
        Assert.assertEquals("test_pinToken", actual)
    }

    @Test
    fun `when getPinToken and pinToken is null then throw needPinToken error`() {
        val dataManager: IDataManager = mockk()
        val expect = PimsErrors.needPinToken()
        every { middlewareComponent.dataManager } returns dataManager
        every { dataManager.read(Constants.STORAGE_KEY_PIN_TOKEN, SECURE) } returns null

        try {
            executor.getPinToken()
        } catch (error: PIMSError) {
            Assert.assertEquals(expect.code, error.code)
            Assert.assertEquals(expect.message, error.message)
        }
    }

    @Test
    fun `when getPinToken and read pinTokenExpiry error then throw needPinToken error`() {
        val dataManager: IDataManager = mockk()
        val expect = PimsErrors.needPinToken()
        every { middlewareComponent.dataManager } returns dataManager
        every { dataManager.read(Constants.STORAGE_KEY_PIN_TOKEN, SECURE) } returns "test_pinToken"
        every { dataManager.read(Constants.STORAGE_KEY_PIN_TOKEN_EXPIRY, APPLICATION) } returns null

        try {
            executor.getPinToken()
        } catch (error: PIMSError) {
            Assert.assertEquals(expect.code, error.code)
            Assert.assertEquals(expect.message, error.message)
        }
    }

    @Test
    fun `when getPinToken and pinToken is expired then throw needPinToken error and delete expired token`() {
        val dataManager: IDataManager = mockk()
        val expect = PimsErrors.needPinToken()
        val expiryData = Instant.MIN
        every { middlewareComponent.dataManager } returns dataManager
        every { dataManager.read(Constants.STORAGE_KEY_PIN_TOKEN, SECURE) } returns "test_pinToken"
        every { dataManager.read(Constants.STORAGE_KEY_PIN_TOKEN_EXPIRY, APPLICATION) } returns expiryData.toString()
        justRun { dataManager.delete(any(), any()) }

        try {
            executor.getPinToken()
        } catch (error: PIMSError) {
            Assert.assertEquals(expect.code, error.code)
            Assert.assertEquals(expect.message, error.message)
        }
        verify { dataManager.delete(Constants.STORAGE_KEY_PIN_TOKEN, SECURE) }
        verify { dataManager.delete(Constants.STORAGE_KEY_PIN_TOKEN_EXPIRY, APPLICATION) }
    }
}
