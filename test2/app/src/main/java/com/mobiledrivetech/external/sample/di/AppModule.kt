package com.mobiledrivetech.external.sample.di

import com.mobiledrivetech.external.middleware.IMiddleware
import com.mobiledrivetech.external.middleware.Middleware
import com.mobiledrivetech.external.sample.data.datasource.TestDataSource
import com.mobiledrivetech.external.sample.data.repository.TestRepositoryImpl
import com.mobiledrivetech.external.sample.domain.repository.TestRepository
import com.mobiledrivetech.external.sample.domain.usecase.ExecuteCommandUC
import com.mobiledrivetech.external.sample.domain.usecase.ExecuteCommandUCImpl
import com.mobiledrivetech.external.sample.domain.usecase.GetAllCommandsUC
import com.mobiledrivetech.external.sample.domain.usecase.GetAllCommandsUCImpl
import com.mobiledrivetech.external.sample.domain.usecase.GetTestCommandResultUC
import com.mobiledrivetech.external.sample.domain.usecase.GetTestCommandResultUCImpl
import com.mobiledrivetech.external.sample.domain.usecase.InitializeMiddlewareUC
import com.mobiledrivetech.external.sample.domain.usecase.InitializeMiddlewareUCImpl
import com.mobiledrivetech.external.sample.framework.datasource.TestDataSourceImpl
import com.mobiledrivetech.external.sample.providers.FacadeDataProvider
import com.mobiledrivetech.external.sample.providers.FacadeDataProviderImp
import org.koin.dsl.module

val appModule = module {
    // sdk
    single<IMiddleware> { Middleware() }

    // provider
    single<FacadeDataProvider> { FacadeDataProviderImp(get()) }

    // data source
    single<TestDataSource> { TestDataSourceImpl(get()) }

    // repository
    single<TestRepository> { TestRepositoryImpl(get()) }

    // use case
    factory<InitializeMiddlewareUC> { InitializeMiddlewareUCImpl(get()) }
    factory<GetTestCommandResultUC> { GetTestCommandResultUCImpl(get()) }
    factory<GetAllCommandsUC> { GetAllCommandsUCImpl() }
    factory<ExecuteCommandUC> { ExecuteCommandUCImpl(get()) }
}
