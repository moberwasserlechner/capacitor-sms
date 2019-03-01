package com.byteowls.capacitor.sms;

import android.content.Intent;
import android.net.Uri;
import com.getcapacitor.JSArray;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import org.json.JSONException;

import java.util.List;

@NativePlugin(requestCodes = { SmsManagerPlugin.SMS_INTENT_REQUEST_CODE }, name = "SmsManager")
public class SmsManagerPlugin extends Plugin {

    static final int SMS_INTENT_REQUEST_CODE = 2311;
    private static final String ERR_SERVICE_NOTFOUND = "ERR_SERVICE_NOTFOUND";
    private static final String ERR_NO_NUMBERS = "ERR_NO_NUMBERS";
    private static final String ERR_NO_TEXT = "ERR_NO_TEXT";

    public SmsManagerPlugin() {}

    @PluginMethod()
    public void send(final PluginCall call) {
        saveCall(call);
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

        if (smsIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(call, smsIntent, SMS_INTENT_REQUEST_CODE);
        } else {
            call.reject(ERR_SERVICE_NOTFOUND);
        }

    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SMS_INTENT_REQUEST_CODE) {
            PluginCall savedCall = getSavedCall();
            savedCall.resolve();
        }
        super.handleOnActivityResult(requestCode, resultCode, data);
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
