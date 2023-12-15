package com.openclassrooms.realestatemanager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit rule for testing coroutines with a main dispatcher.
 *
 * This rule sets the main dispatcher to [Dispatchers.Unconfined] during the test's execution,
 * and resets it after the test is finished. It is useful for testing suspending functions and
 * coroutine-based code in a synchronous manner.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule : TestWatcher() {
    /**
     * Invoked when the test is starting.
     *
     * This method sets the main dispatcher to [Dispatchers.Unconfined].
     *
     * @param description The description of the test being run.
     */
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    /**
     * Invoked when the test has finished.
     *
     * This method resets the main dispatcher after the test execution.
     *
     * @param description The description of the test being run.
     */
    override fun finished(description: Description) {
        super.finished(description)
        // Reset main dispatcher after the test
        Dispatchers.resetMain()
    }
}