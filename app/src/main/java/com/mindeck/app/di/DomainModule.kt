package com.mindeck.app.di

import com.mindeck.domain.repository.CardRepository
import com.mindeck.domain.repository.DeckRepository
import com.mindeck.domain.repository.FolderRepository
import com.mindeck.domain.usecases.cardUseCase.AddCardsToDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.CreateCardUseCase
import com.mindeck.domain.usecases.cardUseCase.DeleteCardUseCase
import com.mindeck.domain.usecases.cardUseCase.DeleteCardsFromDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.GetAllCardsByDeckIdUseCase
import com.mindeck.domain.usecases.cardUseCase.MoveCardsBetweenDeckUseCase
import com.mindeck.domain.usecases.cardUseCase.UpdateCardUseCase
import com.mindeck.domain.usecases.deckUseCases.AddDecksToFolderUseCase
import com.mindeck.domain.usecases.deckUseCases.CreateDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.DeleteDeckUseCase
import com.mindeck.domain.usecases.deckUseCases.DeleteDecksFromFolderUseCase
import com.mindeck.domain.usecases.deckUseCases.GetAllDecksByFolderIdUseCase
import com.mindeck.domain.usecases.deckUseCases.GetDeckByIdUseCase
import com.mindeck.domain.usecases.deckUseCases.MoveDecksBetweenFoldersUseCase
import com.mindeck.domain.usecases.deckUseCases.RenameDeckUseCase
import com.mindeck.domain.usecases.folderUseCases.CreateFolderUseCase
import com.mindeck.domain.usecases.folderUseCases.DeleteFolderUseCase
import com.mindeck.domain.usecases.folderUseCases.GetAllFoldersUseCase
import com.mindeck.domain.usecases.folderUseCases.GetFolderByIdUseCase
import com.mindeck.domain.usecases.folderUseCases.RenameFolderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule() {

    //FolderUseCase
    @Provides
    fun providesCreateFolderUseCase(repository: FolderRepository): CreateFolderUseCase {
        return CreateFolderUseCase(repository)
    }

    @Provides
    fun providesRenameFolderUseCase(repository: FolderRepository): RenameFolderUseCase {
        return RenameFolderUseCase(repository)
    }

    @Provides
    fun providesDeleteFolderUseCase(repository: FolderRepository): DeleteFolderUseCase {
        return DeleteFolderUseCase(repository)
    }

    @Provides
    fun providesGetFolderByIdUseCase(repository: FolderRepository): GetFolderByIdUseCase {
        return GetFolderByIdUseCase(repository)
    }

    @Provides
    fun providesGetAllFoldersUseCase(repository: FolderRepository): GetAllFoldersUseCase {
        return GetAllFoldersUseCase(repository)
    }

    //DeckUseCase
    @Provides
    fun providesCreateDeckUseCase(repository: DeckRepository): CreateDeckUseCase {
        return CreateDeckUseCase(repository)
    }

    @Provides
    fun providesRenameDeckUseCase(repository: DeckRepository): RenameDeckUseCase {
        return RenameDeckUseCase(repository)
    }

    @Provides
    fun providesDeleteDeckUseCase(repository: DeckRepository): DeleteDeckUseCase {
        return DeleteDeckUseCase(repository)
    }

    @Provides
    fun providesGetDeckByIdUseCase(repository: DeckRepository): GetDeckByIdUseCase {
        return GetDeckByIdUseCase(repository)
    }

    @Provides
    fun providesGetAllDecksByFolderIdUseCase(repository: DeckRepository): GetAllDecksByFolderIdUseCase {
        return GetAllDecksByFolderIdUseCase(repository)
    }

    @Provides
    fun providesAddDecksToFolderUseCase(repository: DeckRepository): AddDecksToFolderUseCase {
        return AddDecksToFolderUseCase(repository)
    }

    @Provides
    fun providesDeleteDecksFromFolderUseCase(repository: DeckRepository): DeleteDecksFromFolderUseCase {
        return DeleteDecksFromFolderUseCase(repository)
    }

    @Provides
    fun providesMoveDecksBetweenFoldersUseCase(repository: DeckRepository): MoveDecksBetweenFoldersUseCase {
        return MoveDecksBetweenFoldersUseCase(repository)
    }

    //CardUseCase
    @Provides
    fun providesCreateCardUseCase(repository: CardRepository): CreateCardUseCase {
        return CreateCardUseCase(repository)
    }

    @Provides
    fun providesUpdateCardUseCase(repository: CardRepository): UpdateCardUseCase {
        return UpdateCardUseCase(repository)
    }

    @Provides
    fun providesDeleteCardUseCase(repository: CardRepository): DeleteCardUseCase {
        return DeleteCardUseCase(repository)
    }

    @Provides
    fun providesGetAllCardsByDeckIdUseCase(repository: CardRepository): GetAllCardsByDeckIdUseCase {
        return GetAllCardsByDeckIdUseCase(repository)
    }

    @Provides
    fun providesAddCardsToDeckUseCase(repository: CardRepository): AddCardsToDeckUseCase {
        return AddCardsToDeckUseCase(repository)
    }

    @Provides
    fun providesDeleteCardsFromDeckUseCase(repository: CardRepository): DeleteCardsFromDeckUseCase {
        return DeleteCardsFromDeckUseCase(repository)
    }

    @Provides
    fun providesMoveCardsBetweenDeckUseCase(repository: CardRepository): MoveCardsBetweenDeckUseCase {
        return MoveCardsBetweenDeckUseCase(repository)
    }
}