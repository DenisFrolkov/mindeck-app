package com.mindeck.data.di

import com.mindeck.data.media.FileDownloaderImpl
import com.mindeck.data.media.MediaStorageImpl
import com.mindeck.domain.media.FileDownloader
import com.mindeck.domain.media.MediaStorage
import com.mindeck.domain.usecases.card.command.CreateCardUseCase
import com.mindeck.domain.usecases.card.command.DeleteCardUseCase
import com.mindeck.domain.usecases.card.command.UpdateCardReviewUseCase
import com.mindeck.domain.usecases.card.command.UpdateCardUseCase
import com.mindeck.domain.usecases.card.query.GetAllCardsUseCase
import com.mindeck.domain.usecases.card.query.GetCardByIdUseCase
import com.mindeck.domain.usecases.card.query.GetCardWithDeckByIdUseCase
import com.mindeck.domain.usecases.card.query.GetCardsRepetitionUseCase
import com.mindeck.domain.usecases.deck.command.CreateDeckUseCase
import com.mindeck.domain.usecases.deck.command.DeleteDeckUseCase
import com.mindeck.domain.usecases.deck.command.RenameDeckUseCase
import com.mindeck.domain.usecases.deck.query.GetAllDecksUseCase
import com.mindeck.domain.usecases.deck.query.GetDeckByIdUseCase
import com.mindeck.domain.usecases.deck.query.GetDecksWithStatsUseCase
import com.mindeck.domain.usecases.media.DownloadImageUseCase
import com.mindeck.domain.usecases.media.SaveImageUseCase
import io.ktor.client.HttpClient
import org.koin.dsl.module

val commonDataModule =
    module {
        single { HttpClient() }
        single<FileDownloader> { FileDownloaderImpl(get()) }
        single<MediaStorage> { MediaStorageImpl() }
        factory { DownloadImageUseCase(get()) }
        factory { SaveImageUseCase(get()) }

        factory { CreateCardUseCase(get()) }
        factory { DeleteCardUseCase(get()) }
        factory { UpdateCardUseCase(get()) }
        factory { UpdateCardReviewUseCase(get(), get()) }
        factory { GetAllCardsUseCase(get()) }
        factory { GetCardByIdUseCase(get()) }
        factory { GetCardWithDeckByIdUseCase(get()) }
        factory { GetCardsRepetitionUseCase(get(), get()) }
        factory { CreateDeckUseCase(get()) }
        factory { DeleteDeckUseCase(get()) }
        factory { RenameDeckUseCase(get()) }
        factory { GetAllDecksUseCase(get()) }
        factory { GetDeckByIdUseCase(get()) }
        factory { GetDecksWithStatsUseCase(get()) }
    }
