package com.byteowls.capacitor.sms

object SmsComposer {
    fun recipientSeparator(manufacturer: String): String =
        if (manufacturer.equals("Samsung", ignoreCase = true)) "," else ";"

    fun recipients(numbers: List<String>, manufacturer: String): String =
        numbers.joinToString(recipientSeparator(manufacturer))
}
