package com.chesire.passpusher.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.security.InvalidParameterException

private const val PWPUSH_URL = "https://pwpush.com/p.json"

/**
 * Implementation of the [PasswordAPI] to interact with `pwpush.com`.
 */
class PasswordPusher(private val client: OkHttpClient) : PasswordAPI {
    override suspend fun sendPassword(
        password: String,
        expiryDays: Int,
        expiryViews: Int
    ): PasswordAPI.SendPasswordResult {
        if (password.isBlank()) throw InvalidParameterException("Password cannot be a blank string")

        val body = makeRequestString(password, expiryDays, expiryViews).toRequestBody()
        val request = Request.Builder()
            .url(PWPUSH_URL)
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        return try {
            client.newCall(request)
                .execute()
                .use { response ->
                    if (response.isSuccessful) {
                        val bodyString = response.body?.string()
                        if (bodyString == null) {
                            PasswordAPI.SendPasswordResult(response.code, null)
                        } else {
                            PasswordAPI.SendPasswordResult(
                                response.code,
                                makePushedModel(bodyString)
                            )
                        }
                    } else {
                        PasswordAPI.SendPasswordResult(response.code, null)
                    }
                }
        } catch (exception: IOException) {
            PasswordAPI.SendPasswordResult(HttpURLConnection.HTTP_INTERNAL_ERROR, null)
        }
    }

    private fun makeRequestString(password: String, expiryDays: Int, expiryViews: Int): String {
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

    private fun makePushedModel(jsonBody: String): PushedModel? = try {
        val jsonObject = JSONObject(jsonBody)
        val token = jsonObject.get("url_token") as String
        PushedModel(token)
    } catch (exception: JSONException) {
        null
    }
}
