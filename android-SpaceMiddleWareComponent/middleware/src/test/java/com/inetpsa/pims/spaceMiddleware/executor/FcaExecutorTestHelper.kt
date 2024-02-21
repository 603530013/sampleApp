package com.inetpsa.pims.spaceMiddleware.executor

import com.inetpsa.mmx.foundation.communication.ICommunicationManager
import com.inetpsa.mmx.foundation.tools.Brand
import com.inetpsa.mmx.foundation.tools.Market.EMEA
import com.inetpsa.pims.spaceMiddleware.FoundationMockTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before

internal open class FcaExecutorTestHelper : FoundationMockTest() {

    protected val communicationManager: ICommunicationManager = mockk()

    @Before
    override fun setup() {
        super.setup()
        every { configurationManager.brand } returns Brand.ALFAROMEO
        every { configurationManager.market } returns EMEA
        every { middlewareComponent.communicationManager } returns communicationManager
    }
}
