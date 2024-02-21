package com.inetpsa.pims.spaceMiddleware.command.locations

import com.inetpsa.mmx.foundation.monitoring.PIMSError
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.MiddlewareComponent
import com.inetpsa.pims.spaceMiddleware.executor.locations.GetDirectionsExecutor
import com.inetpsa.pims.spaceMiddleware.executor.locations.GetPlaceDetailsExecutor
import com.inetpsa.pims.spaceMiddleware.executor.locations.GetPlacesNearbySearchExecutor
import com.inetpsa.pims.spaceMiddleware.executor.locations.GetPlacesTextSearchExecutor
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetLocationsCommandTest {

    private val middlewareComponent: MiddlewareComponent = mockk()
    private lateinit var command: GetLocationsCommand

    @Before
    fun setup() {
        command = spyk(GetLocationsCommand())
        every { command.middlewareComponent } returns middlewareComponent
    }

    @After
    fun release() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `when look for executor with actionType textSearch then return GetPlacesTextSearchExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_TEXT_SEARCH

            val executor = command.getExecutor()
            Assert.assertEquals(true, executor is GetPlacesTextSearchExecutor)
        }
    }

    @Test
    fun `when look for executor with actionType nearbySearch then return GetPlacesNearbySearchExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_NEARBY_SEARCH

            val executor = command.getExecutor()
            Assert.assertEquals(true, executor is GetPlacesNearbySearchExecutor)
        }
    }

    @Test
    fun `when look for executor with actionType placeDetails then return GetPlaceDetailsExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_PLACE_DETAILS

            val executor = command.getExecutor()
            Assert.assertEquals(true, executor is GetPlaceDetailsExecutor)
        }
    }

    @Test
    fun `when look for executor with actionType directionsRoute then return GetDirectionsExecutor`() {
        runTest {
            every { command.actionType } returns Constants.PARAM_ACTION_DIRECTIONS_ROUTE

            val executor = command.getExecutor()
            Assert.assertEquals(true, executor is GetDirectionsExecutor)
        }
    }

    @Test
    fun `when look for executor with no existing actionType then throw PimsError exception`() {
        runTest {
            every { command.actionType } returns "details_test"
            val exception = PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)

            try {
                command.getExecutor()
            } catch (ex: PIMSError) {
                Assert.assertEquals(exception.code, ex.code)
                Assert.assertEquals(exception.message, ex.message)
            }
        }
    }
}
