package com.example.chat.auth.google

import android.util.Log
import com.auth0.android.jwt.DecodeException
import com.auth0.android.jwt.JWT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class GoogleUser(
    val sub: String?,
    val email: String?,
    val emailVerified: Boolean?,
    val fullName: String?,
    val givenName: String?,
    val familyName: String?,
    val picture: String?,
    val issuedAt: String?,
    val expirationTime: String?,
    val locale: String?
)

/**
 * Use this function to extract [GoogleUser] information from a token id that you're
 * getting from One-Tap Sign in. The token id is usually valid for only one hour after
 * a successful authentication.
 * @return This function returns a [GoogleUser] object, or null if an exception occurred,
 * due to invalid token decoding, or because your token id has expired.
 * */
fun getUserFromTokenId(tokenId: String): GoogleUser? {
    try {
        val jwt = JWT(tokenId)
        val locale = jwt.claims[Claims.LOCALE]?.asString()?.let { Locale(it) } ?: Locale.getDefault()
        val issuedAt = convertUnixTimestampToReadable(
            timestamp = jwt.claims[Claims.ISSUED_AT]?.asLong() ?: 0,
            locale = locale
        )
        val expirationTime = convertUnixTimestampToReadable(
            timestamp = jwt.claims[Claims.EXPIRATION_TIME]?.asLong() ?: 0,
            locale = locale
        )
        return GoogleUser(
            sub = jwt.claims[Claims.SUB]?.asString(),
            email = jwt.claims[Claims.EMAIL]?.asString(),
            emailVerified = jwt.claims[Claims.EMAIL_VERIFIED]?.asBoolean(),
            fullName = jwt.claims[Claims.FUll_NAME]?.asString(),
            givenName = jwt.claims[Claims.GIVEN_NAME]?.asString(),
            familyName = jwt.claims[Claims.FAMILY_NAME]?.asString(),
            picture = jwt.claims[Claims.PICTURE]?.asString(),
            issuedAt = issuedAt,
            expirationTime = expirationTime,
            locale = locale.toString()
        )
    } catch (e: Exception) {
        Log.e("OneTapSignIn", e.toString())
        return null
    } catch (e: DecodeException) {
        Log.e("OneTapSignIn", e.toString())
        return null
    }
}

fun convertUnixTimestampToReadable(timestamp: Long, locale: Locale): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", locale)
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date(timestamp * 1000))
}