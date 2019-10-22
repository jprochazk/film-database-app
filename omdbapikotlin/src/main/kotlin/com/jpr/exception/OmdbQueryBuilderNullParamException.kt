package com.jpr.exception

class OmdbQueryBuilderNullParamException(val param: String) : Exception("Parameter $param is required")