package com.example.androidbackgroundprocessing

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DummyDownloadHelper{
    fun download(): Flow<Int> = flow {
        for (progress: Int in -1..100) {
            emit(progress)
            delay(1000L)
        }
    }
}