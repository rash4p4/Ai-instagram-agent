package com.example.aireader

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

object ApiClient {
    private val client = OkHttpClient()

    fun summarize(url: String, apiKey: String, callback: (String) -> Unit) {
        val apiUrl =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-09-2025:generateContent?key=$apiKey"

        val userQuery = "Summarize the key information found at: $url"

        val payload = JSONObject().apply {
            put("contents", listOf(mapOf("parts" to listOf(mapOf("text" to userQuery)))))
            put("systemInstruction", mapOf("parts" to listOf(mapOf("text" to "Provide a short, factual summary."))))
        }

        val requestBody = RequestBody.create(
            MediaType.parse("application/json"),
            payload.toString()
        )

        val request = Request.Builder()
            .url(apiUrl)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val json = JSONObject(body)
                val candidate = json.optJSONArray("candidates")
                    ?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text", "No summary found")

                callback(candidate ?: "Empty response")
            }
        })
    }
}
