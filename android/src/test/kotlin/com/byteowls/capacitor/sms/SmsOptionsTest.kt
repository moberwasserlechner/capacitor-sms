package com.byteowls.capacitor.sms

import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class SmsOptionsTest {
    @Test
    fun `parses recipients and text`() {
        val options = SmsOptions.from(JSONObject("""{"numbers":["+431","+432"],"text":"Hello"}"""))
        assertEquals(listOf("+431", "+432"), options.numbers)
        assertEquals("Hello", options.text)
    }

    @Test
    fun `rejects missing recipients`() {
        val error = assertThrows(SmsException::class.java) {
            SmsOptions.from(JSONObject("""{"numbers":[],"text":"Hello"}"""))
        }
        assertEquals(SmsError.NO_NUMBERS, error.error)
    }

    @Test
    fun `rejects missing text`() {
        val error = assertThrows(SmsException::class.java) {
            SmsOptions.from(JSONObject("""{"numbers":["+431"]}"""))
        }
        assertEquals(SmsError.NO_TEXT, error.error)
    }
}
