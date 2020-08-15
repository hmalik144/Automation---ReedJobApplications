package api.network

import Constants.Companion.REED_KEYWORDS
import Constants.Companion.REED_MINIMUM_SALARY
import api.network.responses.ReedJobObject
import com.example.h_mal.androiddevelopertechtest_incrowdsports.data.network.ReedApi
import com.example.h_mal.androiddevelopertechtest_incrowdsports.data.network.SafeApiRequest
import kotlinx.coroutines.runBlocking

class NetworkRequests : SafeApiRequest(){

    val reedApi by lazy { ReedApi() }

    fun getSearchApi() : List<ReedJobObject>?{
        try {
            // get results from Api
            val response =  runBlocking {
                apiRequest { reedApi.getReedData(REED_KEYWORDS, REED_MINIMUM_SALARY) }
            }

            // check we have results
            response.results?.let {
                return it
            }
        }catch (e : Exception){
            e.printStackTrace()
        }
        return null
    }
}