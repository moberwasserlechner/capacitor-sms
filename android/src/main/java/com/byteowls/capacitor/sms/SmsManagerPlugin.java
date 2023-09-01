package com.byteowls.capacitor.sms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import com.getcapacitor.JSArray;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import org.json.JSONException;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.annotation.ActivityCallback;
import android.content.ActivityNotFoundException;

import java.util.List;

@CapacitorPlugin(
    name = "SmsManager"
)
public class SmsManagerPlugin extends Plugin {
    private static final String ERR_SERVICE_NOTFOUND = "ERR_SERVICE_NOTFOUND";
    private static final String ERR_NO_NUMBERS = "ERR_NO_NUMBERS";
    private static final String ERR_NO_TEXT = "ERR_NO_TEXT";
    private static final String SEND_CANCELLED = "SEND_CANCELLED";

    public SmsManagerPlugin() {}

    @PluginMethod()
    public void send(final PluginCall call) {
        sendSms(call);
    }

    private void sendSms(final PluginCall call) {
        JSArray numberArray = call.getArray("numbers");
        List<String> recipientNumbers = null;
        try {
            recipientNumbers = numberArray.toList();
        } catch (JSONException ignore) {}

        if (recipientNumbers == null || recipientNumbers.isEmpty()) {
            call.reject(ERR_NO_NUMBERS);
            return;
        }
        String text = ConfigUtils.getCallParam(String.class, call, "text");
        if (text == null || text.length() == 0) {
            call.reject(ERR_NO_TEXT);
            return;
        }


        String separator = ";";
        if (android.os.Build.MANUFACTURER.equalsIgnoreCase("Samsung")) {
            // See http://stackoverflow.com/questions/18974898/send-sms-through-intent-to-multiple-phone-numbers/18975676#18975676
            separator = ",";
        }
        String phoneNumber = getJoinedNumbers(recipientNumbers, separator);

        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.putExtra("sms_body", text);
        // See http://stackoverflow.com/questions/7242190/sending-sms-using-intent-does-not-add-recipients-on-some-devices
        smsIntent.putExtra("address", phoneNumber);
        smsIntent.setData(Uri.parse("smsto:" + Uri.encode(phoneNumber)));

        try {
            startActivityForResult(call, smsIntent, "onSmsRequestResult");
        } catch (ActivityNotFoundException e) {
            call.reject(ERR_SERVICE_NOTFOUND);
        }
    }

    @ActivityCallback
    private void onSmsRequestResult(PluginCall call, ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_CANCELED) {
            call.reject(SEND_CANCELLED);
        } else {
            call.resolve();
        }
    }

    private String getJoinedNumbers(List<String>numbers, String separator) {
        StringBuilder joined = new StringBuilder();
        for (int i = 0; i < numbers.size(); i++) {
            if (i > 0) {
                joined.append(separator);
            }
            joined.append(numbers.get(i));
        }
        return joined.toString();
    }

}
