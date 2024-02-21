package com.inetpsa.pims.spaceMiddleware.sample

import android.app.Application
import com.inetpsa.pims.spaceMiddleware.IMiddleware
import com.inetpsa.pims.spaceMiddleware.Middleware

class BaseApplication : Application() {

    private val middleware: IMiddleware by lazy { Middleware(applicationContext) }
    val middlewareFacade: MiddlewareFacade by lazy { MiddlewareFacade(middleware) }
}
