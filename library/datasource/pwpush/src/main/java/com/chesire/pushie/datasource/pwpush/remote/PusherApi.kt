package com.chesire.pushie.datasource.pwpush.remote

import com.chesire.pushie.datastore.PreferenceStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Interacts with the `pwpush.com` API.
 */
class PusherApi(
    private val client: OkHttpClient,
    private val preferenceStore: PreferenceStore
) {

    /**
     * Sends the password to the API, returning an [ApiResult] as a response.
     */
    suspend fun sendPassword(
        password: String,
        expiryDays: Int,
        expiryViews: Int
    ): ApiResult {
        check(password.isNotBlank()) { "Password cannot be a blank string" }

        val body = createRequestString(password, expiryDays, expiryViews)
        val request = createRequest(body)

        return try {
            withContext(Dispatchers.IO) {
                client.newCall(request)
                    .execute()
                    .use { parseResponse(it) }
            }
        } catch (ex: IOException) {
            ApiResult.ExceptionalError(ex)
        }
    }

    private fun createRequestString(
        password: String,
        expiryDays: Int,
        expiryViews: Int
    ): String {
        return JSONObject(
            mapOf(
                "password" to JSONObject(
                    mapOf(
                        "payload" to password,
                        "expire_after_days" to expiryDays,
                        "expire_after_views" to expiryViews
                    )
                )
            )
        ).toString()
    }

    private fun createRequest(body: String) =
        Request.Builder()
            .url("${preferenceStore.pushieUrl}.json")
            .addHeader("Content-Type", "application/json")
            .post(body.toRequestBody())
            .build()

    private fun parseResponse(response: Response): ApiResult {
        return if (response.isSuccessful) {
            val bodyString = response.body?.string()
            if (bodyString == null) {
                ApiResult.Error(response.code)
            } else {
                try {
                    ApiResult.Success(createPushedModel(bodyString))
                } catch (ex: JSONException) {
                    ApiResult.ExceptionalError(ex)
                }
            }
        } else {
            ApiResult.Error(response.code)
        }
    }

    @Throws(JSONException::class)
    private fun createPushedModel(jsonBody: String): PushedModel {
        return with(JSONObject(jsonBody)) {
            PushedModel(
                getString("created_at"),
                createPasswordUrl(getString("url_token"))
            )
        }
    }

    private fun createPasswordUrl(token: String) = "${preferenceStore.pushieUrl}/$token"
}

sealed class ApiResult {
    data class Success(val model: PushedModel) : ApiResult()
    data class Error(val code: Int) : ApiResult()
    data class ExceptionalError(val throwable: Throwable?) : ApiResult()
}
