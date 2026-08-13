package com.byteowls.capacitor.sms

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResult
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.ActivityCallback
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "SmsManager")
class SmsManagerPlugin : Plugin() {
    @PluginMethod
    fun send(call: PluginCall) {
        val options = try {
            SmsOptions.from(call.data)
        } catch (error: SmsException) {
            call.reject(error.error.code)
            return
        }

        val recipients = SmsComposer.recipients(options.numbers, Build.MANUFACTURER)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            putExtra("sms_body", options.text)
            putExtra("address", recipients)
            data = Uri.parse("smsto:${Uri.encode(recipients)}")
        }

        try {
            startActivityForResult(call, intent, "onSmsRequestResult")
        } catch (_: ActivityNotFoundException) {
            call.reject(SmsError.SERVICE_NOT_FOUND.code)
        }
    }

    @ActivityCallback
    private fun onSmsRequestResult(call: PluginCall, result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_CANCELED) {
            call.reject(SmsError.SEND_CANCELLED.code)
        } else {
            call.resolve()
        }
    }
}
