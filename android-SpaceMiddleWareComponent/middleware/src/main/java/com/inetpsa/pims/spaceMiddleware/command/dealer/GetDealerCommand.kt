package com.inetpsa.pims.spaceMiddleware.command.dealer

import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.GetAdvisorDealerReviewConfigurationPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.GetDealerListNearbyPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.GetDealerListPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.GetPreferredDealerPsaExecutor

@Deprecated("should be replaced with DealerCommandGet")
internal class GetDealerCommand : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.PARAM_ACTION_DEALER_LIST_DETAILS -> GetDealerListPsaExecutor(this)
        Constants.PARAM_ACTION_DEALER_LIST_NEARBY_DETAILS -> GetDealerListNearbyPsaExecutor(this)
        Constants.PARAM_ACTION_PREFERRED_DEALER_DETAILS -> GetPreferredDealerPsaExecutor(this)
        Constants.PARAM_ACTION_ADVISOR_DEALER_REVIEW_DETAILS -> GetAdvisorDealerReviewConfigurationPsaExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }
}
