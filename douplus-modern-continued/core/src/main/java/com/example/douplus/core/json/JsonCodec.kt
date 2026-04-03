package com.example.douplus.core.json

import com.example.douplus.core.result.AppResult
import com.example.douplus.core.result.FailReason
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class JsonCodec(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        encodeDefaults = true
    }
) {
    inline fun <reified T> encode(value: T): String = json.encodeToString(value)

    inline fun <reified T> encodeOrNull(value: T): String? {
        return runCatching { encode(value) }.getOrNull()
    }

    inline fun <reified T> encodeSafe(value: T): AppResult<String> {
        return AppResult.runCatchingApp(FailReason.ParseError("encode:${T::class.java.simpleName}")) {
            encode(value)
        }
    }

    inline fun <reified T> decode(text: String): T = json.decodeFromString(text)

    inline fun <reified T> decodeOrNull(text: String): T? {
        return runCatching { decode<T>(text) }.getOrNull()
    }

    inline fun <reified T> decodeSafe(text: String): AppResult<T> {
        return AppResult.runCatchingApp(FailReason.ParseError("decode:${T::class.java.simpleName}")) {
            decode(text)
        }
    }
}
