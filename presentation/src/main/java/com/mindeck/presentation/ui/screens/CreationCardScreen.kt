package com.mindeck.presentation.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatClear
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatStrikethrough
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Subscript
import androidx.compose.material.icons.filled.Superscript
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mindeck.domain.models.CardType
import com.mindeck.domain.models.Deck
import com.mindeck.presentation.R
import com.mindeck.presentation.state.CreateCardFormState
import com.mindeck.presentation.state.ModalState
import com.mindeck.presentation.state.UiState
import com.mindeck.presentation.ui.components.buttons.CustomButton
import com.mindeck.presentation.ui.components.dialog.ChooseModalWindow
import com.mindeck.presentation.ui.components.selector.SelectorRow
import com.mindeck.presentation.ui.components.textfields.CardInputField
import com.mindeck.presentation.ui.components.topBar.AppTopBar
import com.mindeck.presentation.ui.navigation.LocalNavigator
import com.mindeck.presentation.ui.theme.MindeckTheme
import com.mindeck.presentation.viewmodel.card.CreationCardNavigationEvent
import com.mindeck.presentation.viewmodel.card.CreationCardViewModel
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.BasicRichTextEditor

@Composable
fun CreationCardScreen(
    deckId: Int?,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current

    val context = LocalContext.current
    val viewModel = hiltViewModel<CreationCardViewModel>()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val deckState by viewModel.decksState.collectAsStateWithLifecycle()
    val createDeckState by viewModel.createDeckState.collectAsStateWithLifecycle()
    val createCardState by viewModel.createCardState.collectAsStateWithLifecycle()
    val modalState by viewModel.modalState.collectAsStateWithLifecycle()

    val imageLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri -> uri?.let { viewModel.saveImage(it.toString()) } }

    val audioLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent(),
    ) { uri -> uri?.let { viewModel.saveQuestionAudio(it.toString()) } }

    LaunchedEffect(deckId) {
        deckId?.let { viewModel.setDeckId(it) }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is CreationCardNavigationEvent.ShowToast -> {
                    @Suppress("LocalContextGetResourceValueCall")
                    Toast.makeText(context, context.getString(event.messageRes), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    CreationCardScreenContent(
        formState = formState,
        deckState = deckState,
        createDeckState = createDeckState,
        createCardState = createCardState,
        modalState = modalState,
        actions = CreationCardScreenActions(
            onNavigateBack = navigator::pop,
            onShowDeckModal = viewModel::showDeckModal,
            onShowTypeModal = viewModel::showTypeModal,
            onUpdateForm = viewModel::updateForm,
            onCreateCard = { q, a -> viewModel.createCard(q, a) },
            onHideModal = viewModel::hideModal,
            onSetDeckId = viewModel::setDeckId,
            onSetType = viewModel::setType,
            onCreateDeck = viewModel::createDeck,
            onPickImage = {
                imageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onPickImageFromUrl = viewModel::saveImageFromUrl,
            onPickAudio = {
                audioLauncher.launch("audio/*")
            },
        ),
        modifier = modifier,
    )
}

@Composable
internal fun CreationCardScreenContent(
    formState: CreateCardFormState,
    deckState: UiState<List<Deck>>,
    createDeckState: UiState<Unit>,
    createCardState: UiState<Unit>,
    modalState: ModalState,
    actions: CreationCardScreenActions,
    modifier: Modifier = Modifier,
) {
    val richEditorState = rememberRichEditorState()
    val toolbarAlpha = rememberImeToolbarAlpha(richEditorState.isAnyFocused)

    LaunchedEffect(createCardState) {
        if (createCardState is UiState.Success) {
            richEditorState.questionState.setHtml("")
            richEditorState.answerState.setHtml("")
        }
    }

    val isRichTextValid by remember {
        derivedStateOf {
            richEditorState.questionState.annotatedString.text.isNotBlank() &&
                richEditorState.answerState.annotatedString.text.isNotBlank()
        }
    }

    val cardTypes = listOf(
        Pair(stringResource(R.string.card_type_simple), CardType.SIMPLE),
        Pair(stringResource(R.string.card_type_complex), CardType.COMPLEX),
    )

    val selectedDeckName = remember(deckState, formState.selectedDeckId) {
        formState.selectedDeckId?.let { id ->
            (deckState as? UiState.Success)?.data?.find { it.deckId == id }?.deckName
        }
    }

    val selectedTypeName = formState.selectedType?.let { cardType ->
        when (cardType) {
            CardType.SIMPLE -> stringResource(R.string.card_type_simple)
            CardType.COMPLEX -> stringResource(R.string.card_type_complex)
        }
    } ?: stringResource(R.string.choose_type_title_dialog)

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .systemBarsPadding(),
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                AppTopBar(
                    onBackClick = actions.onNavigateBack,
                    modifier = Modifier.padding(horizontal = MindeckTheme.dimensions.paddingMd),
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = MindeckTheme.dimensions.paddingMd),
                ) {
                    SelectorRow(
                        label = stringResource(R.string.text_deck_dropdown_selector),
                        selectedText = selectedDeckName
                            ?: stringResource(R.string.choose_deck_title_dialog),
                        onClick = actions.onShowDeckModal,
                    )

                    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerSm))

                    SelectorRow(
                        label = stringResource(R.string.text_type_dropdown_selector),
                        selectedText = selectedTypeName,
                        onClick = actions.onShowTypeModal,
                    )

                    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerSm))

                    CardFormInputs(
                        formState = formState,
                        onValueChange = actions.onUpdateForm,
                        richEditorState = richEditorState,
                        onPickImage = actions.onPickImage,
                        onPickImageFromUrl = actions.onPickImageFromUrl,
                        onPickAudio = actions.onPickAudio,
                    )

                    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerLg))

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CreateCardButton(
                            text = stringResource(R.string.save_text),
                            isValid = formState.isValid() && isRichTextValid,
                            actionState = createCardState,
                            onSaveClick = {
                                actions.onCreateCard(
                                    richEditorState.questionState.toHtml(),
                                    richEditorState.answerState.toHtml(),
                                )
                            },
                        )
                    }
                }
            },
        )

        RichTextFormattingToolbar(
            richTextState = richEditorState.activeState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .imePadding()
                .alpha(toolbarAlpha),
        )
    }

    when (modalState) {
        is ModalState.DeckSelection -> {
            ChooseModalWindow(
                titleText = stringResource(R.string.choose_deck_title_dialog),
                items = (deckState as? UiState.Success)?.data?.map {
                    Pair(
                        it.deckName,
                        it.deckId,
                    )
                },
                actionState = createDeckState,
                onExitClick = actions.onHideModal,
                onSaveClick = actions.onSetDeckId,
                selectedId = formState.selectedDeckId,
                showAddIcon = true,
                createTitle = stringResource(R.string.create_item_dialog_text_input_name_deck),
                createButtonLabel = stringResource(R.string.save_text),
                createPlaceholder = stringResource(R.string.rename_item_dialog_text_input_title_deck),
                onCreateClick = actions.onCreateDeck,
            )
        }

        is ModalState.TypeSelection -> {
            ChooseModalWindow(
                titleText = stringResource(R.string.choose_type_title_dialog),
                items = cardTypes.map { Pair(it.first, it.second.stableId) },
                actionState = UiState.Idle,
                selectedId = formState.selectedType?.stableId,
                onExitClick = actions.onHideModal,
                onSaveClick = { id -> actions.onSetType(CardType.fromStableId(id)) },
            )
        }

        else -> Unit
    }
}

@Composable
private fun CardFormInputs(
    formState: CreateCardFormState,
    onValueChange: (CreateCardFormState.() -> CreateCardFormState) -> Unit,
    richEditorState: RichEditorState,
    onPickImage: () -> Unit,
    onPickImageFromUrl: (String) -> Unit,
    onPickAudio: () -> Unit,
) {
    val questionState = richEditorState.questionState
    val answerState = richEditorState.answerState

    CardInputField(
        placeholder = stringResource(R.string.enter_name_for_card),
        value = formState.title,
        singleLine = true,
        onValueChange = { onValueChange { copy(title = it) } },
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.large,
            )
            .height(height = MindeckTheme.dimensions.dp46)
            .border(
                width = MindeckTheme.dimensions.dp0_25,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.large,
            )
            .padding(horizontal = MindeckTheme.dimensions.paddingSm),
    )

    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerMd))

    Text(
        text = stringResource(R.string.text_question_input_field),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.dp4))
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { richEditorState.isQuestionFocused = it.hasFocus }
            .heightIn(min = MindeckTheme.dimensions.dp46, max = MindeckTheme.dimensions.dp200)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(MindeckTheme.dimensions.dp6),
            )
            .border(
                MindeckTheme.dimensions.dp0_25,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(MindeckTheme.dimensions.dp6),
            )
            .padding(MindeckTheme.dimensions.paddingSm),
    ) {
        BasicRichTextEditor(
            state = questionState,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }

    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerMd))

    Text(
        text = stringResource(R.string.text_answer_input_field),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.dp4))
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { richEditorState.isAnswerFocused = it.hasFocus }
            .heightIn(min = MindeckTheme.dimensions.dp46, max = MindeckTheme.dimensions.dp200)
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(MindeckTheme.dimensions.dp6),
            )
            .border(
                MindeckTheme.dimensions.dp0_25,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(MindeckTheme.dimensions.dp6),
            )
            .padding(MindeckTheme.dimensions.paddingSm),
    ) {
        BasicRichTextEditor(
            state = answerState,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }

    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerMd))
    CardInputField(
        value = formState.tag,
        singleLine = true,
        placeholder = stringResource(R.string.text_tag_input_field),
        placeholderTextStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.outline),
        onValueChange = { onValueChange { copy(tag = it) } },
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.shapes.large,
            )
            .height(
                height = MindeckTheme.dimensions.dp46,
            )
            .border(
                MindeckTheme.dimensions.dp0_25,
                MaterialTheme.colorScheme.outlineVariant,
                MaterialTheme.shapes.large,
            )
            .padding(horizontal = MindeckTheme.dimensions.paddingSm),
    )

    Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerMd))

    if (formState.cardImagePath != null) {
        AsyncImage(
            model = java.io.File(formState.cardImagePath),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.large),
        )
        Spacer(modifier = Modifier.height(MindeckTheme.dimensions.spacerSm))
    }

    Row {
        IconButton(onClick = onPickImage) {
            Icon(
                imageVector = Icons.Default.AddPhotoAlternate,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }

        IconButton(onClick = { onPickImageFromUrl("https://picsum.photos/400") }) {
            Icon(
                imageVector = Icons.Default.AddPhotoAlternate,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }

    Row {
        IconButton(onClick = onPickAudio) {
            Icon(
                imageVector = Icons.Default.AddPhotoAlternate,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun CreateCardButton(
    text: String,
    isValid: Boolean,
    actionState: UiState<Unit>,
    onSaveClick: () -> Unit,
) {
    val isLoading = actionState is UiState.Loading
    val errorState = actionState as? UiState.Error

    val animatedButtonColor by animateColorAsState(
        targetValue = when {
            !isValid -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.primary
        },
        animationSpec = tween(DURATION_300),
        label = "buttonColor",
    )

    val animatedTextColor by animateColorAsState(
        targetValue = when {
            !isValid -> MaterialTheme.colorScheme.onSurfaceVariant
            isLoading -> MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
            else -> MaterialTheme.colorScheme.onPrimary
        },
        animationSpec = tween(DURATION_300),
        label = "textColor",
    )

    CustomButton(
        text = if (isLoading) stringResource(R.string.loading_text) else text,
        color = animatedButtonColor,
        textColor = animatedTextColor,
        onClick = { if (!isLoading && isValid) onSaveClick() },
        modifier = Modifier.size(
            height = MindeckTheme.dimensions.dp42,
            width = MindeckTheme.dimensions.dp140,
        ),
    )

    errorState?.let { error ->
        Text(
            text = stringResource(error.messageRes, *error.args.toTypedArray()),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = MindeckTheme.dimensions.dp4,
                ),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
private fun RichTextFormattingToolbar(
    richTextState: RichTextState?,
    modifier: Modifier = Modifier,
) {
    val isBold = richTextState?.currentSpanStyle?.fontWeight == FontWeight.Bold
    val isItalic = richTextState?.currentSpanStyle?.fontStyle == FontStyle.Italic
    val isUnderline = richTextState?.currentSpanStyle?.textDecoration == TextDecoration.Underline
    val isStrikethrough =
        richTextState?.currentSpanStyle?.textDecoration == TextDecoration.LineThrough
    val isCode = richTextState?.isCodeSpan ?: false
    val isBulletList = richTextState?.isUnorderedList ?: false
    val isNumberedList = richTextState?.isOrderedList ?: false
    val isSuperscript = richTextState?.currentSpanStyle?.baselineShift == BaselineShift.Superscript
    val isSubscript = richTextState?.currentSpanStyle?.baselineShift == BaselineShift.Subscript
    val currentHighlight = richTextState?.currentSpanStyle?.background ?: Color.Unspecified
    val currentTextColor = richTextState?.currentSpanStyle?.color ?: Color.Unspecified

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .horizontalScroll(rememberScrollState()),
    ) {
        FormatButton(Icons.Default.FormatBold, isBold) {
            richTextState?.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
        }
        FormatButton(Icons.Default.FormatItalic, isItalic) {
            richTextState?.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
        }
        FormatButton(Icons.Default.FormatUnderlined, isUnderline) {
            richTextState?.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
        }
        FormatButton(Icons.Default.FormatStrikethrough, isStrikethrough) {
            richTextState?.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
        }
        FormatButton(Icons.AutoMirrored.Filled.FormatListBulleted, isBulletList) {
            richTextState?.toggleUnorderedList()
        }
        FormatButton(Icons.Default.FormatListNumbered, isNumberedList) {
            richTextState?.toggleOrderedList()
        }
        FormatButton(Icons.Default.Code, isCode) {
            richTextState?.toggleCodeSpan()
        }

        ToolbarDivider()

        FormatButton(Icons.Default.Superscript, isSuperscript) {
            richTextState?.toggleSpanStyle(
                SpanStyle(
                    baselineShift = BaselineShift.Superscript,
                    fontSize = 0.75.em,
                ),
            )
        }
        FormatButton(Icons.Default.Subscript, isSubscript) {
            richTextState?.toggleSpanStyle(
                SpanStyle(
                    baselineShift = BaselineShift.Subscript,
                    fontSize = 0.75.em,
                ),
            )
        }

        ToolbarDivider()

        ColorFormatButton(ToolbarHighlightColor, currentHighlight == ToolbarHighlightColor) {
            richTextState?.toggleSpanStyle(SpanStyle(background = ToolbarHighlightColor))
        }

        ToolbarDivider()

        ToolbarTextColors.forEach { color ->
            ColorFormatButton(color, currentTextColor == color) {
                richTextState?.toggleSpanStyle(SpanStyle(color = color))
            }
        }

        ToolbarDivider()

        FormatButton(Icons.Default.FormatClear, richTextState?.hasFormatting() ?: false) {
            richTextState?.clearAllFormatting()
        }
    }
}

@Composable
private fun ToolbarDivider() {
    VerticalDivider(
        modifier = Modifier
            .height(MindeckTheme.dimensions.dp24)
            .padding(horizontal = MindeckTheme.dimensions.dp4),
        thickness = MindeckTheme.dimensions.dp0_25,
        color = MaterialTheme.colorScheme.outlineVariant,
    )
}

@Composable
private fun ColorFormatButton(
    color: Color,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(MindeckTheme.dimensions.dp40)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(MindeckTheme.dimensions.dp20)
                .clip(CircleShape)
                .background(color)
                .then(
                    if (isActive) {
                        Modifier.border(
                            MindeckTheme.dimensions.dp2,
                            MaterialTheme.colorScheme.onSurface,
                            CircleShape,
                        )
                    } else {
                        Modifier
                    },
                ),
        )
    }
}

@Composable
private fun FormatButton(
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        )
    }
}

private val ToolbarHighlightColor = Color(0x80FFFF00)
private val ToolbarTextColors = listOf(
    Color(0xFFE53935),
    Color(0xFF1E88E5),
    Color(0xFF43A047),
)

const val DURATION_300 = 300

data class CreationCardScreenActions(
    val onNavigateBack: () -> Unit,
    val onShowDeckModal: () -> Unit,
    val onShowTypeModal: () -> Unit,
    val onUpdateForm: (CreateCardFormState.() -> CreateCardFormState) -> Unit,
    val onCreateCard: (question: String, answer: String) -> Unit,
    val onHideModal: () -> Unit,
    val onSetDeckId: (Int) -> Unit,
    val onSetType: (CardType) -> Unit,
    val onCreateDeck: (String) -> Unit,
    val onPickImage: () -> Unit,
    val onPickImageFromUrl: (String) -> Unit,
    val onPickAudio: () -> Unit,
)
