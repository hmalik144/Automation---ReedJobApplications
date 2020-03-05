package api.network

import Constants.Companion.REED_KEYWORDS
import Constants.Companion.REED_LOCATION
import Constants.Companion.REED_MINIMUM_SALARY
import api.network.responses.ReedJobObject
import com.example.h_mal.androiddevelopertechtest_incrowdsports.data.network.ReedApi
import com.example.h_mal.androiddevelopertechtest_incrowdsports.data.network.SafeApiRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NetworkRequests : SafeApiRequest(){

    val reedApi = ReedApi()

    fun getSearchApi() : List<ReedJobObject>?{
        try {
            val response =  runBlocking { apiRequest { reedApi.getGameData(REED_KEYWORDS, REED_LOCATION, REED_MINIMUM_SALARY) } }

            response.results?.let {
                return it
            }

        }catch (e : Exception){
            println("*** $e")
        }
        return null
    }
}