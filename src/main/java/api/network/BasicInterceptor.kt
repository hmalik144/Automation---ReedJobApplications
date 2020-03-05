package api.network

import Constants.Companion.REED_API_KEY
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicInterceptor() : Interceptor{

    private val credentials = Credentials.basic(REED_API_KEY,"")


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request();
        val builder = request.newBuilder().header(
                "Authorization", credentials
        ).build()

        return chain.proceed(builder)
    }


}