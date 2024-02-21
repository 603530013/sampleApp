package com.inetpsa.pims.spaceMiddleware.command.settings

import androidx.annotation.VisibleForTesting
import com.inetpsa.mmx.foundation.extensions.has
import com.inetpsa.mmx.foundation.monitoring.PIMSFoundationError
import com.inetpsa.pims.spaceMiddleware.Constants
import com.inetpsa.pims.spaceMiddleware.command.BaseBrandCommand
import com.inetpsa.pims.spaceMiddleware.executor.BaseFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BaseLocalExecutor
import com.inetpsa.pims.spaceMiddleware.executor.BasePsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetAppPrivacyFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetAppTermsFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetAppTermsPSAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetBcallContactFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetConnectedPrivacyFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetConnectedTermsFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetCurrentLanguageExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetEcallContactFcaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetLanguageListExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetPaymentListPsaExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetRoadSideContactFCAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetRoadSideContactPSAExecutor
import com.inetpsa.pims.spaceMiddleware.executor.settings.get.GetWebTermsFCAExecutor

internal class SettingsCommandGet : BaseBrandCommand() {

    override suspend fun getPsaExecutor(): BasePsaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.CONTACT -> handlePsaContactExecutor()
        Constants.Input.ActionType.PAYMENT -> handlePsaPaymentExecutor()
        Constants.Input.ActionType.APP_TERMS -> GetAppTermsPSAExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handlePsaContactExecutor(): BasePsaExecutor<*, *> =
        when (parameters.has<String>(Constants.Input.ACTION)) {
            Constants.Input.Action.ROADSIDE -> GetRoadSideContactPSAExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handlePsaPaymentExecutor(): BasePsaExecutor<*, *> =
        when (parameters.has<String>(Constants.Input.ACTION)) {
            Constants.Input.Action.LIST -> GetPaymentListPsaExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }

    override suspend fun getFcaExecutor(): BaseFcaExecutor<*, *> = when (actionType) {
        Constants.Input.ActionType.CONTACT -> handleFcaContactExecutor()
        Constants.Input.ActionType.APP_TERMS -> GetAppTermsFCAExecutor(this)
        Constants.Input.ActionType.WEB_TERMS -> GetWebTermsFCAExecutor(this)
        Constants.Input.ActionType.APP_PRIVACY -> GetAppPrivacyFCAExecutor(this)
        Constants.Input.ActionType.CONNECTED_TERMS -> GetConnectedTermsFCAExecutor(this)
        Constants.Input.ActionType.CONNECTED_PRIVACY -> GetConnectedPrivacyFCAExecutor(this)
        else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION_TYPE)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handleFcaContactExecutor(): BaseFcaExecutor<*, *> =
        when (parameters.has<String>(Constants.Input.ACTION)) {
            Constants.Input.Action.ROADSIDE -> GetRoadSideContactFCAExecutor(this)
            Constants.Input.Action.E_CALL -> GetEcallContactFcaExecutor(this)
            Constants.Input.Action.B_CALL -> GetBcallContactFcaExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }

    override suspend fun getCommonExecutor(): BaseLocalExecutor<*, *>? = when (actionType) {
        Constants.Input.ActionType.LANGUAGE -> handleBaseLanguagesExecutor()
        else -> null
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun handleBaseLanguagesExecutor(): BaseLocalExecutor<*, *> =
        when (parameters.has<String>(Constants.Input.ACTION)) {
            Constants.Input.Action.LIST -> GetLanguageListExecutor(this.middlewareComponent, this.parameters)
            Constants.Input.Action.CURRENT -> GetCurrentLanguageExecutor(this)
            else -> throw PIMSFoundationError.invalidParameter(Constants.Input.ACTION)
        }
}
