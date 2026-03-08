package com.mindeck.presentation.state

import androidx.annotation.StringRes
import com.mindeck.domain.exception.DomainError
import com.mindeck.presentation.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@StringRes
fun DomainError.toUserMessage(): Int = when (this) {
    is DomainError.DatabaseError -> R.string.error_database_try_again
    is DomainError.NameAlreadyExists -> R.string.error_name_already_exists
}

fun <T> Flow<T>.toUiState(): Flow<UiState<T>> = flow {
    try {
        collect { value ->
            emit(UiState.Success(value))
        }
    } catch (e: DomainError) {
        emit(UiState.Error(e.toUserMessage()))
    } catch (e: Exception) {
        emit(UiState.Error(R.string.error_something_went_wrong))
    }
}
