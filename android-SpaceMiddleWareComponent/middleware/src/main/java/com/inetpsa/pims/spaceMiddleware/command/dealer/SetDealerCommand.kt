package com.inetpsa.pims.spaceMiddleware.command.dealer

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.EnrollPreferredDealerPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.RemovePreferredDealerPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.SendAdvisorDealerReviewPsaExecutor

@Deprecated("should be replaced with DealerCommandSet")
internal class SetDealerCommand : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_PREFERRED_DEALER_ENROLL -> EnrollPreferredDealerPsaExecutor(this)
        Constants.PARAM_ACTION_PREFERRED_DEALER_REMOVE -> RemovePreferredDealerPsaExecutor(this)
        Constants.PARAM_ACTION_SEND_ADVISOR_DEALER_REVIEW -> SendAdvisorDealerReviewPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
