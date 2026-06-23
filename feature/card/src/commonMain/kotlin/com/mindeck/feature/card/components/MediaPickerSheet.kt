package com.mindeck.feature.card.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mindeck.core.ui.sheet.AddMediaBottomSheet
import com.mindeck.core.ui.sheet.MediaSheetContent
import com.mindeck.core.ui.sheet.MediaSource
import com.mindeck.feature.card.model.AudioSource
import com.mindeck.feature.card.model.LinkFieldState
import com.mindeck.feature.card.model.MediaSheet
import com.mindeck.feature.card.model.PhotoSource
import mindeck_app.core.ui.generated.resources.Res
import mindeck_app.core.ui.generated.resources.add_a_photo_icon
import mindeck_app.core.ui.generated.resources.chevron_right_icon
import mindeck_app.core.ui.generated.resources.folder_open_icon
import mindeck_app.core.ui.generated.resources.library_decks_icon
import mindeck_app.core.ui.generated.resources.library_music_icon
import mindeck_app.core.ui.generated.resources.link_icon
import mindeck_app.core.ui.generated.resources.mic_icon
import mindeck_app.feature.card.generated.resources.create_card_audio_link_confirm
import mindeck_app.feature.card.generated.resources.create_card_audio_link_label
import mindeck_app.feature.card.generated.resources.create_card_audio_link_placeholder
import mindeck_app.feature.card.generated.resources.create_card_audio_sheet_description
import mindeck_app.feature.card.generated.resources.create_card_audio_sheet_title
import mindeck_app.feature.card.generated.resources.create_card_audio_source_files_subtitle
import mindeck_app.feature.card.generated.resources.create_card_audio_source_files_title
import mindeck_app.feature.card.generated.resources.create_card_audio_source_record_subtitle
import mindeck_app.feature.card.generated.resources.create_card_audio_source_record_title
import mindeck_app.feature.card.generated.resources.create_card_audio_source_tts_subtitle
import mindeck_app.feature.card.generated.resources.create_card_audio_source_tts_title
import mindeck_app.feature.card.generated.resources.create_card_photo_link_confirm
import mindeck_app.feature.card.generated.resources.create_card_photo_link_label
import mindeck_app.feature.card.generated.resources.create_card_photo_link_placeholder
import mindeck_app.feature.card.generated.resources.create_card_photo_sheet_description
import mindeck_app.feature.card.generated.resources.create_card_photo_sheet_title
import mindeck_app.feature.card.generated.resources.create_card_photo_source_camera_subtitle
import mindeck_app.feature.card.generated.resources.create_card_photo_source_camera_title
import mindeck_app.feature.card.generated.resources.create_card_photo_source_files_subtitle
import mindeck_app.feature.card.generated.resources.create_card_photo_source_files_title
import mindeck_app.feature.card.generated.resources.create_card_photo_source_gallery_subtitle
import mindeck_app.feature.card.generated.resources.create_card_photo_source_gallery_title
import org.jetbrains.compose.resources.stringResource
import mindeck_app.feature.card.generated.resources.Res as CreateCardRes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MediaPickerSheet(
    sheet: MediaSheet,
    link: LinkFieldState,
    onDismiss: () -> Unit,
    onPickPhotoSource: (PhotoSource) -> Unit,
    onPickAudioSource: (AudioSource) -> Unit,
    modifier: Modifier = Modifier,
) {
    val content =
        when (sheet) {
            MediaSheet.PHOTO ->
                MediaSheetContent(
                    title = stringResource(CreateCardRes.string.create_card_photo_sheet_title),
                    description = stringResource(CreateCardRes.string.create_card_photo_sheet_description),
                    sources = photoSources(onPickPhotoSource),
                    linkLabel = stringResource(CreateCardRes.string.create_card_photo_link_label),
                    linkPlaceholder = stringResource(CreateCardRes.string.create_card_photo_link_placeholder),
                    confirmLabel = stringResource(CreateCardRes.string.create_card_photo_link_confirm),
                )
            MediaSheet.AUDIO ->
                MediaSheetContent(
                    title = stringResource(CreateCardRes.string.create_card_audio_sheet_title),
                    description = stringResource(CreateCardRes.string.create_card_audio_sheet_description),
                    sources = audioSources(onPickAudioSource),
                    linkLabel = stringResource(CreateCardRes.string.create_card_audio_link_label),
                    linkPlaceholder = stringResource(CreateCardRes.string.create_card_audio_link_placeholder),
                    confirmLabel = stringResource(CreateCardRes.string.create_card_audio_link_confirm),
                )
        }
    AddMediaBottomSheet(
        title = content.title,
        description = content.description,
        sources = content.sources,
        linkSectionLabel = content.linkLabel,
        linkValue = link.draft,
        onLinkValueChange = link.onChange,
        linkPlaceholder = content.linkPlaceholder,
        confirmLabel = content.confirmLabel,
        onConfirmLink = link.onConfirm,
        onDismiss = onDismiss,
        modifier = modifier,
        linkIcon = Res.drawable.link_icon,
        chevronIcon = Res.drawable.chevron_right_icon,
    )
}

@Composable
private fun photoSources(onPick: (PhotoSource) -> Unit): List<MediaSource> =
    listOf(
        MediaSource(
            icon = Res.drawable.add_a_photo_icon,
            title = stringResource(CreateCardRes.string.create_card_photo_source_camera_title),
            subtitle = stringResource(CreateCardRes.string.create_card_photo_source_camera_subtitle),
            onClick = { onPick(PhotoSource.CAMERA) },
        ),
        MediaSource(
            icon = Res.drawable.library_decks_icon,
            title = stringResource(CreateCardRes.string.create_card_photo_source_gallery_title),
            subtitle = stringResource(CreateCardRes.string.create_card_photo_source_gallery_subtitle),
            onClick = { onPick(PhotoSource.GALLERY) },
        ),
        MediaSource(
            icon = Res.drawable.folder_open_icon,
            title = stringResource(CreateCardRes.string.create_card_photo_source_files_title),
            subtitle = stringResource(CreateCardRes.string.create_card_photo_source_files_subtitle),
            onClick = { onPick(PhotoSource.FILES) },
        ),
    )

@Composable
private fun audioSources(onPick: (AudioSource) -> Unit): List<MediaSource> =
    listOf(
        MediaSource(
            icon = Res.drawable.mic_icon,
            title = stringResource(CreateCardRes.string.create_card_audio_source_record_title),
            subtitle = stringResource(CreateCardRes.string.create_card_audio_source_record_subtitle),
            onClick = { onPick(AudioSource.RECORD) },
        ),
        MediaSource(
            icon = Res.drawable.library_music_icon,
            title = stringResource(CreateCardRes.string.create_card_audio_source_tts_title),
            subtitle = stringResource(CreateCardRes.string.create_card_audio_source_tts_subtitle),
            onClick = { onPick(AudioSource.TTS) },
        ),
        MediaSource(
            icon = Res.drawable.folder_open_icon,
            title = stringResource(CreateCardRes.string.create_card_audio_source_files_title),
            subtitle = stringResource(CreateCardRes.string.create_card_audio_source_files_subtitle),
            onClick = { onPick(AudioSource.FILES) },
        ),
    )
