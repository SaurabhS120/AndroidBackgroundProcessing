package com.example.androidbackgroundprocessing

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface DummyDownloadHelperInterface{
    fun download(): Flow<Int>
    suspend fun dummyDelay()
}