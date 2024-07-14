package com.example.androidbackgroundprocessing

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

open class DummyDownloadHelper{
    fun download(): Flow<Int> = flow {
        for (progress: Int in -1..100) {
            emit(progress)
            dummyDelay()
        }
    }
    open suspend fun dummyDelay(){
        delay(1000L)
    }
}