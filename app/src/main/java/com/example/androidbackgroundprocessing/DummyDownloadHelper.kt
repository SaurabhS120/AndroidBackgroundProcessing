package com.example.androidbackgroundprocessing

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

open class DummyDownloadHelper @Inject constructor():DummyDownloadHelperInterface{
    override fun download(): Flow<Int> = flow {
        for (progress: Int in -1..100) {
            emit(progress)
            dummyDelay()
        }
    }
    override suspend fun dummyDelay(){
        delay(1000L)
    }
}