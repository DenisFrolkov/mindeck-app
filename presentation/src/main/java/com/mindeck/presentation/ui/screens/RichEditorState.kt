package com.mindeck.presentation.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState

internal val BlockquoteParagraphStyle = ParagraphStyle(
    textIndent = TextIndent(firstLine = 16.sp, restLine = 16.sp),
)

@Stable
class RichEditorState(
    val questionState: RichTextState,
    val answerState: RichTextState,
) {
    var isQuestionFocused by mutableStateOf(false)
        internal set
    var isAnswerFocused by mutableStateOf(false)
        internal set

    val activeState: RichTextState?
        get() = when {
            isQuestionFocused -> questionState
            isAnswerFocused -> answerState
            else -> null
        }

    val isAnyFocused: Boolean
        get() = isQuestionFocused || isAnswerFocused
}

fun RichTextState.hasFormatting(): Boolean {
    val range = selection
    return if (!range.collapsed) {
        annotatedString.spanStyles.any { it.start < range.max && it.end > range.min } ||
            isCodeSpan || isUnorderedList || isOrderedList
    } else {
        currentSpanStyle != SpanStyle() || isCodeSpan || isUnorderedList || isOrderedList
    }
}

fun RichTextState.clearAllFormatting() {
    val range = selection
    if (!range.collapsed) {
        annotatedString.spanStyles
            .filter { it.start < range.max && it.end > range.min }
            .map { it.item }
            .distinct()
            .forEach { removeSpanStyle(it, range) }
        removeCodeSpan()
    } else {
        clearSpanStyles()
        clearRichSpans()
    }
    if (isUnorderedList) removeUnorderedList()
    if (isOrderedList) removeOrderedList()
}

@Composable
fun rememberRichEditorState() = remember {
    RichEditorState(
        questionState = RichTextState(),
        answerState = RichTextState(),
    )
}

@Composable
fun rememberImeToolbarAlpha(isVisible: Boolean): Float {
    val density = LocalDensity.current
    val imeHeight = WindowInsets.ime.getBottom(density)
    var imeMaxHeight by remember { mutableIntStateOf(0) }
    if (imeHeight > imeMaxHeight) imeMaxHeight = imeHeight
    return if (imeMaxHeight > 0 && isVisible) {
        (imeHeight / imeMaxHeight.toFloat()).coerceIn(0f, 1f)
    } else {
        0f
    }
}
