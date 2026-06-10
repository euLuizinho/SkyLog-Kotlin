package br.com.fiap.skylog.di

import br.com.fiap.skylog.data.remote.NasaEonetApi
import br.com.fiap.skylog.data.repository.AlertaRepositoryImpl
import br.com.fiap.skylog.data.repository.PreferencesRepositoryImpl
import br.com.fiap.skylog.domain.repository.AlertaRepository
import br.com.fiap.skylog.domain.repository.PreferencesRepository
import br.com.fiap.skylog.domain.usecase.FiltrarAlertasUseCase
import br.com.fiap.skylog.domain.usecase.GetAlertasUseCase
import br.com.fiap.skylog.presentation.screens.alertas.AlertasViewModel
import br.com.fiap.skylog.presentation.screens.detalhe.DetalheAlertaViewModel
import br.com.fiap.skylog.presentation.screens.home.HomeViewModel
import br.com.fiap.skylog.presentation.screens.perfil.PerfilViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

val appModule = module {

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://eonet.gsfc.nasa.gov/api/v3/")
            .client(get())
            .addConverterFactory(
                get<Json>().asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    single<NasaEonetApi> {
        get<Retrofit>().create(NasaEonetApi::class.java)
    }

    single<PreferencesRepository> {
        PreferencesRepositoryImpl(androidContext())
    }

    single<AlertaRepository> {
        AlertaRepositoryImpl(api = get(), preferencesRepository = get())
    }

    factory { GetAlertasUseCase(alertaRepository = get(), preferencesRepository = get()) }
    factory { FiltrarAlertasUseCase() }

    viewModel { HomeViewModel(getAlertasUseCase = get(), preferencesRepository = get()) }
    viewModel { AlertasViewModel(getAlertasUseCase = get(), filtrarAlertasUseCase = get(), preferencesRepository = get()) }
    viewModel { DetalheAlertaViewModel(alertaRepository = get(), preferencesRepository = get()) }
    viewModel { PerfilViewModel(preferencesRepository = get()) }
}
