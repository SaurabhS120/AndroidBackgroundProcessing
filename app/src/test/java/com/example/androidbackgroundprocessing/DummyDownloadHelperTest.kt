package com.example.androidbackgroundprocessing

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DummyDownloadHelperTest{
    @Test
    fun `Dummy file download test`(){
        val mockDummyDownloadHelper = MockDummyDownloadHelper()
        runBlocking {
            val result:List<Int> = mockDummyDownloadHelper.download().toList()
            val expectedResult:List<Int> = (-1..100).toList()
            assertEquals(expectedResult,result)
        }
    }
}
class MockDummyDownloadHelper: DummyDownloadHelper() {
    override suspend fun dummyDelay() {
        // Delay removed by overriding this function
    }

}