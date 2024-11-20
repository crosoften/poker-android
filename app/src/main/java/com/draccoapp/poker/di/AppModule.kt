package com.draccoapp.poker.di

import android.util.Log
import com.draccoapp.poker.api.service.AuthService
import com.draccoapp.poker.api.service.GlobalService
import com.draccoapp.poker.api.service.RegisterService
import com.draccoapp.poker.api.service.TournamentService
import com.draccoapp.poker.api.service.UserService
import com.draccoapp.poker.api.service.chatSocket.ChatSocketService
import com.draccoapp.poker.repository.AuthRepository
import com.draccoapp.poker.repository.CoachRepository
import com.draccoapp.poker.repository.GlobalRepository
import com.draccoapp.poker.repository.RegisterRepository
import com.draccoapp.poker.repository.TournamentRepository
import com.draccoapp.poker.repository.UserRepository
import com.draccoapp.poker.utils.Constants
import com.draccoapp.poker.utils.PokerApplication
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.viewModel.AuthViewModel
import com.draccoapp.poker.viewModel.CoachViewModel
import com.draccoapp.poker.viewModel.ContractViewModel
import com.draccoapp.poker.viewModel.HomeViewModel
import com.draccoapp.poker.viewModel.RegisterViewModel
import com.draccoapp.poker.viewModel.TournamentViewModel
import com.draccoapp.poker.viewModel.UserViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

private const val CONNECTION_TIMEOUT = 30 * 1000

val netWorkModule = module {
    single {
        Preferences(PokerApplication.instance) // Substitua isso pela forma correta de obter a instância de Preferences no seu caso
    }
    single<OkHttpClient> {
        val preferences: Preferences = get()
        getUnsafeOkHttpClient(preferences).newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
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
val serviceModule = module {
    single {
        get<Retrofit>(Retrofit::class).create(AuthService::class.java)
    }
    single {
        get<Retrofit>(Retrofit::class).create(UserService::class.java)
    }
    single {
        get<Retrofit>(Retrofit::class).create(TournamentService::class.java)
    }
    single {
        get<Retrofit>(Retrofit::class).create(RegisterService::class.java)
    }

    single {
        get<Retrofit>(Retrofit::class).create(GlobalService::class.java)
    }

    single<ChatSocketService> { ChatSocketService(get()) }
}
val repositoryModule = module {

    single<CoroutineDispatcher> { Dispatchers.Default }

    single {
        AuthRepository(get(), get())
    }
    single {
        UserRepository(get(), get())
    }
    single {
        TournamentRepository(get(), get(), get())
    }
    single {
        RegisterRepository(get())
    }
    single {
        GlobalRepository(get(), get())
    }
    single {
        CoachRepository(get())
    }
}
val viewModelModule = module {
    viewModel {
        AuthViewModel(get(), get())
    }
    viewModel {
        UserViewModel(get(), get(), get())
    }
    viewModel {
        TournamentViewModel(get(), get())
    }
    viewModel {
        RegisterViewModel(get())
    }
    viewModel {
        HomeViewModel(get())
    }
    viewModel {
        ContractViewModel(get())
    }
    viewModel {
        CoachViewModel(get())
    }
}


val listModules = listOf(netWorkModule, databaseModule, serviceModule, repositoryModule, viewModelModule)

fun getUnsafeOkHttpClient(preferences: Preferences): OkHttpClient {
    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

        override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}

        override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
    })

    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, java.security.SecureRandom())

    val sslSocketFactory = sslContext.socketFactory

    return OkHttpClient.Builder()
        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .header("accept", "application/json")
                .header(
                    "Authorization",
                    "Bearer ${preferences.getToken()}"
                )
                .build()
            chain.proceed(newRequest)
        }
        .connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .build()

    Log.i("TokenWill", "getUnsafeOkHttpClient:  usando o toke ${preferences.getToken()}")
}