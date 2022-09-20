package com.example.portfolio.utils


/*sealed class DataState<out T> (){
    data class Error(var exception:Exception): DataState<Nothing>()
    data class Success<T>(val datas: List<T>): DataState<T>()
    data class SuccessSingleData<T>(val data:T?): DataState<T>()
    object Loading: DataState<Nothing>()
}*/

sealed class DataState<out T> (val data:T? = null, val message:String? = null){
    class Loading<T>(data:T?= null): DataState<T>(data)
    class Success<T>(data: T?): DataState<T>(data)
    class Error<T>(message: String, data: T? = null): DataState<T>(data, message), CommonError
}

sealed interface CommonError{
    object NetworkFailed:CommonError
    object InternetConnection:CommonError
    object ExceededLimitedRequests: CommonError
}

sealed class Resource<T>(val data:T? = null, val message:String? = null){
    class Loading<T>(data:T?= null): Resource<T>(data)
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
}




