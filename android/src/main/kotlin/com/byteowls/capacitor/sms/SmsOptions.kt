package com.byteowls.capacitor.sms

import org.json.JSONObject

data class SmsOptions(val numbers: List<String>, val text: String) {
    companion object {
        fun from(data: JSONObject): SmsOptions {
            val values = data.optJSONArray("numbers")
                ?: throw SmsException(SmsError.NO_NUMBERS)
            val numbers = buildList {
                for (index in 0 until values.length()) {
                    val number = values.optString(index, "")
                    if (number.isNotEmpty()) add(number)
                }
            }
            if (numbers.isEmpty()) throw SmsException(SmsError.NO_NUMBERS)

            val text = data.optString("text", "")
            if (text.isEmpty()) throw SmsException(SmsError.NO_TEXT)
            return SmsOptions(numbers, text)
        }
    }
}

enum class SmsError(val code: String) {
    NO_NUMBERS("ERR_NO_NUMBERS"),
    NO_TEXT("ERR_NO_TEXT"),
    SERVICE_NOT_FOUND("ERR_SERVICE_NOTFOUND"),
    SEND_CANCELLED("SEND_CANCELLED")
}

class SmsException(val error: SmsError) : IllegalArgumentException(error.code)
