package com.example.nfc_sample_kotlin.di

import com.example.nfc_sample_kotlin.repository.ScanDataRepository
import com.example.nfc_sample_kotlin.repository.ScanDataRepositoryImpl
import com.example.nfc_sample_kotlin.repository.WriteDataRepository
import com.example.nfc_sample_kotlin.repository.WriteDataRepositoryImpl
import com.example.nfc_sample_kotlin.viewmodel.ActivityViewModel
import com.example.nfc_sample_kotlin.viewmodel.ScanFragmentViewModel
import com.example.nfc_sample_kotlin.viewmodel.WriteFragmentViewModel
import com.example.nfc_sample_kotlin.api.ParseNdefMessage
import com.example.nfc_sample_kotlin.api.ParseNdefMessageImpl
import com.example.nfc_sample_kotlin.api.WriteNdefMessage
import com.example.nfc_sample_kotlin.api.WriteNdefMessageImpl
import com.example.nfc_sample_kotlin.usecase.ScanDataUseCase
import com.example.nfc_sample_kotlin.usecase.WriteDataUseCases
import com.example.nfc_sample_kotlin.usecase.write.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


class DiModule {

    private val apiModule = module {
        single<ParseNdefMessage> { ParseNdefMessageImpl() }
        single<WriteNdefMessage> { WriteNdefMessageImpl() }
    }

    private val repositoryModule = module {
        factory<ScanDataRepository> { ScanDataRepositoryImpl(parseNdefMessage = get()) }
        factory<WriteDataRepository> { WriteDataRepositoryImpl(writeNdefMessage = get()) }
    }

    private val useCaseModule = module {
        factory { ScanDataUseCase(scanDataRepository = get()) }
        factory {
            WriteDataUseCases(
                SaveWriteDataUseCase(writeDataRepository = get()),
                DeleteWriteDataUseCase(writeDataRepository = get()),
                MoveWriteDataUseCase(writeDataRepository = get()),
                EditWriteDataUseCase(writeDataRepository = get()),
                WriteSavedDataUseCase(writeDataRepository = get())
            )
        }
    }

    private val viewModelModule = module {
        viewModel { ActivityViewModel() }
        viewModel { ScanFragmentViewModel(scanDataUseCase = get()) }
        viewModel { WriteFragmentViewModel(writeDataUseCases = get()) }
    }

    fun init() {
        startKoin {
            modules(
                listOf(
                    apiModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}