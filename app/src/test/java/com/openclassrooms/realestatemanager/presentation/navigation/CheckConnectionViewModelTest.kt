package com.openclassrooms.realestatemanager.presentation.navigation

import com.openclassrooms.realestatemanager.domain.use_cases.CheckInternetConnectionUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class CheckConnectionViewModelTest{
    @MockK
    private lateinit var checkInternetConnectionUseCase: CheckInternetConnectionUseCase

    private lateinit var viewModel: CheckConnectionViewModel
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = CheckConnectionViewModel(checkInternetConnectionUseCase)
    }

    @Test
    fun `isInternetOn should return true when internet connection is available`() {
        every { checkInternetConnectionUseCase.invoke() } returns true

        val result = viewModel.isInternetOn()

        assertTrue(result)
    }

    @Test
    fun `isInternetOn should return false when internet connection is not available`() {
        every { checkInternetConnectionUseCase.invoke() } returns false

        val result = viewModel.isInternetOn()

        assertFalse(result)
    }
}