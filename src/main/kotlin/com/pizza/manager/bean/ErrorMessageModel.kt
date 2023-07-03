package com.pizza.manager.bean

data class ErrorMessageModel(val httpStatus: Int, val errorMessage: String, val errorCode: String)