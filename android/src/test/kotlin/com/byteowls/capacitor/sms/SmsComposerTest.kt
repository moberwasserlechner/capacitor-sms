package com.byteowls.capacitor.sms

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SmsComposerTest {
    @Test
    fun `uses semicolon for regular Android devices`() {
        assertEquals("+431;+432", SmsComposer.recipients(listOf("+431", "+432"), "Google"))
    }

    @Test
    fun `uses comma for Samsung devices`() {
        assertEquals("+431,+432", SmsComposer.recipients(listOf("+431", "+432"), "samsung"))
    }
}
