package com.draccoapp.poker.di

import android.content.Context
import android.content.SharedPreferences
import com.draccoapp.poker.repository.AuthRepository
import com.draccoapp.poker.repository.TournamentRepository
import com.draccoapp.poker.repository.UserRepository
import com.draccoapp.poker.api.service.AuthService
import com.draccoapp.poker.api.service.TournamentService
import com.draccoapp.poker.api.service.UserService
import com.draccoapp.poker.utils.Constants
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.viewModel.AuthViewModel
import com.draccoapp.poker.viewModel.TournamentViewModel
import com.draccoapp.poker.viewModel.UserViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 30 * 1000

val netWorkModule = module {
    single<OkHttpClient> {
        OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .header("accept", "application/json")
                .header(
                    "Authorization",
                    "Bearer ${
                        get<Preferences>(Preferences::class).getToken()
                    }"
                )
                .build()
            chain.proceed(newRequest)
        }.connectTimeout(
            CONNECTION_TIMEOUT.toLong(),
            TimeUnit.MINUTES
        ).readTimeout(1, TimeUnit.MINUTES).build()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

}
val databaseModule = module {

    single {
        Preferences(androidContext())
    }
}
val serviceModule = module{
    single {
        get<Retrofit>(Retrofit::class).create(AuthService::class.java)
    }
    single {
        get<Retrofit>(Retrofit::class).create(UserService::class.java)
    }
    single {
        get<Retrofit>(Retrofit::class).create(TournamentService::class.java)
    }
}
val repositoryModule = module{

    single<CoroutineDispatcher> { Dispatchers.Default }

    single {
        AuthRepository(get(), get())
    }
    single {
        UserRepository(get(), get())
    }
    single {
        TournamentRepository(get(), get())
    }
}
val viewModelModule = module{
    viewModel {
        AuthViewModel(get(), get())
    }
    viewModel {
        UserViewModel(get(), get(), get())
    }
    viewModel {
        TournamentViewModel(get(), get())
    }
}


val listModules = listOf(netWorkModule, databaseModule, serviceModule, repositoryModule, viewModelModule)