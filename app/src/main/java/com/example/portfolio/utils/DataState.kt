package com.example.portfolio.utils

import android.net.Network


/*sealed class DataState<out T> (){
    data class Error(var exception:Exception): DataState<Nothing>()
    data class Success<T>(val datas: List<T>): DataState<T>()
    data class SuccessSingleData<T>(val data:T?): DataState<T>()
    object Loading: DataState<Nothing>()
}*/

sealed class DataState<out T> (val data:T? = null, val message:String? = null, val errorType: NetworkError?= null){
    class Loading<T>(data:T?= null): DataState<T>(data)
    class Success<T>(data: T?): DataState<T>(data)
    class Error<T>(message: String, data: T? = null, errorCode:Int? = null, errorType:NetworkError? = null): DataState<T>(data, message, errorType)
}



sealed interface NetworkError{
    //502 Bad gateway server - no internet error
    object InternetConnectionError:NetworkError
    //400
    object BadRequest:NetworkError
    //401
    object UnauthorizedError:NetworkError
    //404
    object PageNotFoundError:NetworkError
    //500
    object ServerSideError:NetworkError
}


sealed class Resource<T>(val data:T? = null, val message:String? = null){
    class Loading<T>(data:T?= null): Resource<T>(data)
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
}




