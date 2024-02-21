package com.inetpsa.pims.spaceMiddleware.command.dealer

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.AddDealerAppointmentFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.AddDealerAppointmentPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.AddFavoriteDealerFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.AddFavoriteDealerPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.DeleteAppointmentsFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.dealer.set.RemoveFavoriteDealerPsaExecutor
import com.inetpsa.pims.spaceMiddleware.model.common.Action
import com.inetpsa.pims.spaceMiddleware.util.hasEnum

internal class DealerCommandSet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.FAVORITE -> handlePsaFavoriteActions()
        Constants.Input.ActionType.APPOINTMENT -> handlePsaAppointmentActions()
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.FAVORITE -> handleFcaFavoriteActions()
        Constants.Input.ActionType.APPOINTMENT -> handleFcaAppointmentActions()
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private fun handleFcaAppointmentActions(): BaseFcaExecutor<*, *> =
        when (parameters.hasEnum<Action>(Constants.Input.ACTION)) {
            Action.Add -> AddDealerAppointmentFcaExecutor(this)
            Action.Delete -> DeleteAppointmentsFcaExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handlePsaFavoriteActions(): BasePsaExecutor<*, *> =
        when (parameters.hasEnum<Action>(Constants.Input.ACTION)) {
            Action.Add -> AddFavoriteDealerPsaExecutor(this)
            Action.Remove -> RemoveFavoriteDealerPsaExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handleFcaFavoriteActions(): BaseFcaExecutor<*, *> =
        when (parameters.hasEnum<Action>(Constants.Input.ACTION)) {
            Action.Add -> AddFavoriteDealerFcaExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private fun handlePsaAppointmentActions(): BasePsaExecutor<*, *> =
        when (parameters.hasEnum<Action>(Constants.Input.ACTION)) {
            Action.Add -> AddDealerAppointmentPsaExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }
}
