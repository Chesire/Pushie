package com.chesire.pushie.datasource.pwpush.remote

import com.chesire.pushie.common.ApiError
import com.chesire.pushie.datastore.PreferenceStore
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject

/**
 * Interacts with the `pwpush.com` API.
 */
class PusherApi @Inject constructor(
    private val client: OkHttpClient,
    private val preferenceStore: PreferenceStore
) {

    /**
     * Sends the password to the API.
     */
    suspend fun sendPassword(
        password: String,
        expiryDays: Int,
        expiryViews: Int
    ): Result<PushedModel, ApiError> {
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
            Err(ApiError(throwable = ex))
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

    private fun parseResponse(response: Response): Result<PushedModel, ApiError> {
        return if (response.isSuccessful) {
            val bodyString = response.body?.string()
            if (bodyString == null) {
                Err(ApiError(code = response.code))
            } else {
                try {
                    Ok(createPushedModel(bodyString))
                } catch (ex: JSONException) {
                    Err(ApiError(throwable = ex))
                }
            }
        } else {
            Err(ApiError(code = response.code))
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
