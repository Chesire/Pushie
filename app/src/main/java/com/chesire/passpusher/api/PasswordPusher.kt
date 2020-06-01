package com.chesire.passpusher.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.security.InvalidParameterException

private const val PWPUSH_BASE = "https://pwpush.com/p"
private const val PWPUSH_ENDPOINT = "$PWPUSH_BASE.json"

/**
 * Implementation of the [PasswordAPI] to interact with `pwpush.com`.
 */
class PasswordPusher(private val client: OkHttpClient) : PasswordAPI {
    override fun createPasswordUrl(token: String) = "$PWPUSH_BASE/$token"

    override suspend fun sendPassword(
        password: String,
        expiryDays: Int,
        expiryViews: Int
    ): PasswordAPI.SendPasswordResult {
        if (password.isBlank()) throw InvalidParameterException("Password cannot be a blank string")

        val body = createRequestString(password, expiryDays, expiryViews)
        val request = createRequest(body)

        return try {
            withContext(Dispatchers.IO) {
                client.newCall(request)
                    .execute()
                    .use { parseResponse(it) }
            }
        } catch (exception: IOException) {
            createFailureResult()
        }
    }

    private fun parseResponse(response: Response): PasswordAPI.SendPasswordResult {
        return if (response.isSuccessful) {
            val bodyString = response.body?.string()
            if (bodyString == null) {
                createFailureResult(response.code)
            } else {
                PasswordAPI.SendPasswordResult(
                    response.code,
                    createPushedModel(bodyString)
                )
            }
        } else {
            createFailureResult(response.code)
        }
    }

    private fun createRequestString(password: String, expiryDays: Int, expiryViews: Int): String {
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

    private fun createRequest(body: String) = Request.Builder()
        .url(PWPUSH_ENDPOINT)
        .addHeader("Content-Type", "application/json")
        .post(body.toRequestBody())
        .build()

    private fun createFailureResult(
        statusCode: Int = HttpURLConnection.HTTP_INTERNAL_ERROR
    ): PasswordAPI.SendPasswordResult = PasswordAPI.SendPasswordResult(statusCode, null)

    private fun createPushedModel(jsonBody: String): PushedModel? = try {
        with(JSONObject(jsonBody)) {
            PushedModel(
                getInt("id"),
                getString("url_token"),
                getString("created_at")
            )
        }
    } catch (exception: JSONException) {
        null
    }
}
