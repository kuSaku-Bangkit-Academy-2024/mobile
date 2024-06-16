package com.capstone.kusaku.data.remote.retrofit

import com.capstone.kusaku.data.local.UserPreference
import com.capstone.kusaku.data.local.UserSession
import com.capstone.kusaku.data.remote.request.RefreshTokenRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(
    private val userPreference: UserPreference,
) : Interceptor {

    private val apiService: ApiService by lazy {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder().baseUrl("https://kusaku-api-axmcqgttuq-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()

        retrofit.create(ApiService::class.java)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (request.header("Authorization").isNullOrEmpty()) {
            val accessToken = runBlocking {
                userPreference.getSession().first().token
            }
            request = request.newBuilder().addHeader("Authorization", "Bearer $accessToken").build()
        }

        var response = chain.proceed(request)

        if (response.code == 401 || response.code == 403 || response.code == 500) {
            val newAccessToken = runBlocking {
                val refreshToken = userPreference.getSession().first().refreshToken
                refreshToken?.let {
                    refreshAccessToken(it)
                } ?: throw IllegalStateException("Refresh token is null")
            }

            response.close()

            request = request.newBuilder().removeHeader("Authorization")
                .addHeader("Authorization", "Bearer $newAccessToken").build()

            response = chain.proceed(request)
        }

        return response
    }

    private suspend fun refreshAccessToken(refreshToken: String): String {
        return try {
            val request = RefreshTokenRequest(refreshToken)
            val response = apiService.refreshToken(request)
            val newAccessToken = response.data.accessToken
            userPreference.saveSession(
                UserSession(newAccessToken, null, null, null, refreshToken)
            )
            newAccessToken
        } catch (e: HttpException) {
            e.printStackTrace()
            throw e
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}
