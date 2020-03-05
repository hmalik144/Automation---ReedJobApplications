package com.example.h_mal.androiddevelopertechtest_incrowdsports.data.network

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

abstract class SafeApiRequest {

    //abstract function to unwrap body from response of api call
    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        //get the reponse
        val response = call.invoke()
        if(response.isSuccessful){
            //response is successful so return the body
            return response.body()!!
        }else{
            //the response failed so throw an error

            val error = response.errorBody()?.string()

            val message = StringBuilder()
            error?.let{
                try{
                    message.append(JSONObject(it).getString("message"))
                }catch(e: JSONException){ }
                message.append("\n")
            }
            message.append("Error Code: ${response.code()}")

            throw IOException(message.toString())
        }
    }

}