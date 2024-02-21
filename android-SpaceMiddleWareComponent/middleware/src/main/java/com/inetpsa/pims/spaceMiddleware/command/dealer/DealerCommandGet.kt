package com.inetpsa.pims.spaceMiddleware.command.dealer

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetAppointmentDetailsFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetAppointmentDetailsPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetAppointmentListFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetAppointmentListPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealerAgendaFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealerAgendaPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealerReviewPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealerServicesFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealerServicesPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealersFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetDealersPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetFavoriteDealerFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.get.GetFavoriteDealerPsaExecutor

internal class DealerCommandGet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.LIST -> GetDealersPsaExecutor(this)
        Constants.Input.ActionType.FAVORITE -> GetFavoriteDealerPsaExecutor(this)
        Constants.Input.ActionType.REVIEW -> GetDealerReviewPsaExecutor(this)
        Constants.Input.ActionType.APPOINTMENT -> handlePsaAppointmentActions()
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.LIST -> GetDealersFcaExecutor(this)
        Constants.Input.ActionType.FAVORITE -> GetFavoriteDealerFcaExecutor(this)
        Constants.Input.ActionType.APPOINTMENT -> handleFcaAppointmentActions()
        else -> throw PIMSFoundationError.invalidParameter(Constants.PARAM_ACTION_TYPE)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handlePsaAppointmentActions(): BasePsaExecutor<*, *> =
        when (parameters.has<String>(Constants.Input.ACTION)) {
            Constants.Input.Action.AGENDA -> GetDealerAgendaPsaExecutor(this)
            Constants.Input.Action.SERVICES -> GetDealerServicesPsaExecutor(this)
            Constants.Input.Action.LIST -> GetAppointmentListPsaExecutor(this)
            Constants.Input.Action.DETAILS -> GetAppointmentDetailsPsaExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handleFcaAppointmentActions(): BaseFcaExecutor<*, *> =
        when (parameters.has<String>(Constants.Input.ACTION)) {
            Constants.Input.Action.AGENDA -> GetDealerAgendaFcaExecutor(this)
            Constants.Input.Action.SERVICES -> GetDealerServicesFcaExecutor(this)
            Constants.Input.Action.LIST -> GetAppointmentListFcaExecutor(this)
            Constants.Input.Action.DETAILS -> GetAppointmentDetailsFcaExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }
}
