package com.openclassrooms.realestatemanager.domain.use_cases

import com.openclassrooms.realestatemanager.domain.Connection.ConnectionCheckerRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CheckInternetConnectionUseCaseTest {
    @MockK
    lateinit var mockRepository: ConnectionCheckerRepository

    private lateinit var checkInternetConnectionUseCase: CheckInternetConnectionUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        checkInternetConnectionUseCase = CheckInternetConnectionUseCase(mockRepository)
    }

    @Test
    fun `invoke should call isInternetConnected on the repository`() = runBlocking {
        coEvery { checkInternetConnectionUseCase.invoke() } returns  true

        val isInternetConnected = checkInternetConnectionUseCase.invoke()

        coVerify { mockRepository.isInternetConnected() }
        TestCase.assertEquals(true, isInternetConnected)
    }
}