package com.inetpsa.pims.spaceMiddleware.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.inetpsa.pims.PIMSPredefinedSections
import com.inetpsa.pims.spaceMiddleware.sample.api.AccountApi
import com.inetpsa.pims.spaceMiddleware.sample.api.AssistanceApi
import com.inetpsa.pims.spaceMiddleware.sample.api.ConfigurationApi
import com.inetpsa.pims.spaceMiddleware.sample.api.ContractsApi
import com.inetpsa.pims.spaceMiddleware.sample.api.DealerApi
import com.inetpsa.pims.spaceMiddleware.sample.api.DestinationApi
import com.inetpsa.pims.spaceMiddleware.sample.api.FeaturesApi
import com.inetpsa.pims.spaceMiddleware.sample.api.LocationsApi
import com.inetpsa.pims.spaceMiddleware.sample.api.LogIncidentApi
import com.inetpsa.pims.spaceMiddleware.sample.api.NewAssistanceApi
import com.inetpsa.pims.spaceMiddleware.sample.api.NewDealerApi
import com.inetpsa.pims.spaceMiddleware.sample.api.NewDestinationApi
import com.inetpsa.pims.spaceMiddleware.sample.api.NewEServicesApi
import com.inetpsa.pims.spaceMiddleware.sample.api.NewLocationsApi
import com.inetpsa.pims.spaceMiddleware.sample.api.NewLogIncidentApi
import com.inetpsa.pims.spaceMiddleware.sample.api.NewRadioPlayerApi
import com.inetpsa.pims.spaceMiddleware.sample.api.NewSettingsApi
import com.inetpsa.pims.spaceMiddleware.sample.api.NewVehicleApi
import com.inetpsa.pims.spaceMiddleware.sample.api.RadioPlayerApi
import com.inetpsa.pims.spaceMiddleware.sample.api.SettingsApi
import com.inetpsa.pims.spaceMiddleware.sample.api.UserApi
import com.inetpsa.pims.spaceMiddleware.sample.api.VehicleApi
import com.inetpsa.pims.uisample.SampleUiFactory
import com.inetpsa.pims.uisample.views.models.Component

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as? BaseApplication)?.middlewareFacade?.let { middleware ->

            val predefinedSections = listOf(
                PIMSPredefinedSections.PIMS_BASIC_AUTHENTICATION,
                PIMSPredefinedSections.PIMS_FCA_BASIC_AUTHENTICATION,
                PIMSPredefinedSections.PIMS_FCA_STRONG_AUTHENTICATION
            )

            val apis = listOf(
                UserApi(),
                NewVehicleApi(),
                NewEServicesApi(),
                VehicleApi(),
                ContractsApi(),
                NewAssistanceApi(),
                NewDealerApi(),
                NewSettingsApi(),
                AssistanceApi(),
                SettingsApi(),
                AccountApi(),
                LocationsApi(),
                LogIncidentApi(),
                DealerApi(),
                DestinationApi(),
                RadioPlayerApi(),
                NewRadioPlayerApi(),
                ConfigurationApi(),
                NewDestinationApi(),
                NewLocationsApi(),
                NewLogIncidentApi(),
                FeaturesApi()
            )
                .sortedByDescending { it.apiName }
                .flatMap { it.generate() }

            val component = Component(middleware, Component.Type.BOTH, apis)
            val view = SampleUiFactory.create().initialize(this, listOf(component), predefinedSections)
            setContentView(view)
        }
    }
}
