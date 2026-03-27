package com.mindeck.presentation.state

import app.cash.turbine.test
import com.mindeck.domain.exception.DomainError
import com.mindeck.presentation.R
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FlowExtensionsTest {

    @Test
    fun toUiState_emitsSuccess_whenFlowEmitsValue() = runTest {
        flowOf("data").toUiState().test {
            assertEquals(UiState.Success("data"), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun toUiState_emitsError_whenFlowThrowsNameAlreadyExists() = runTest {
        flow<String> { throw DomainError.NameAlreadyExists() }.toUiState().test {
            assertEquals(UiState.Error(DomainError.NameAlreadyExists().toUserMessage()), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun toUiState_emitsError_whenFlowThrowsDatabaseError() = runTest {
        flow<String> { throw DomainError.DatabaseError() }.toUiState().test {
            assertEquals(UiState.Error(DomainError.DatabaseError().toUserMessage()), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun toUiState_emitsError_whenFlowThrowsException() = runTest {
        flow<String> { throw Exception() }.toUiState().test {
            assertEquals(UiState.Error(R.string.error_something_went_wrong), awaitItem())
            awaitComplete()
        }
    }
}
