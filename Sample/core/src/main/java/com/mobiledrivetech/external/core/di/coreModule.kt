package com.mobiledrivetech.external.core.di

import com.mobiledrivetech.external.core.data.datasource.TestDataSource
import com.mobiledrivetech.external.core.data.repository.TestRepositoryImpl
import com.mobiledrivetech.external.core.domain.repository.TestRepository
import com.mobiledrivetech.external.core.domain.usecase.GetTestCommandResultUC
import com.mobiledrivetech.external.core.domain.usecase.GetTestCommandResultUCImpl
import com.mobiledrivetech.external.core.domain.usecase.InitializeMiddlewareUC
import com.mobiledrivetech.external.core.domain.usecase.InitializeMiddlewareUCImpl
import com.mobiledrivetech.external.core.framework.datasource.TestDataSourceImpl
import com.mobiledrivetech.external.core.providers.FacadeDataProvider
import com.mobiledrivetech.external.core.providers.FacadeDataProviderImp
import com.mobiledrivetech.external.middleware.IMiddleware
import com.mobiledrivetech.external.middleware.Middleware
import org.koin.dsl.module

val coreModule = module {
    //sdk
    single<IMiddleware> { Middleware() }

    //provider
    single<FacadeDataProvider> { FacadeDataProviderImp(get()) }

    //data source
    single<TestDataSource> { TestDataSourceImpl(get()) }

    //repository
    single<TestRepository> { TestRepositoryImpl(get()) }

    //use case
    factory<InitializeMiddlewareUC> { InitializeMiddlewareUCImpl(get()) }
    factory<GetTestCommandResultUC> { GetTestCommandResultUCImpl(get()) }
}