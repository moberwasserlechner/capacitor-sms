package com.byteowls.capacitor.sms;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.getcapacitor.JSArray;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import org.json.JSONException;

@NativePlugin(requestCodes = { SmsManagerPlugin.SMS_INTENT_REQUEST_CODE }, name = "SmsManager")
public class SmsManagerPlugin extends Plugin {

    static final int SMS_INTENT_REQUEST_CODE = 2311;
    private static final String ERR_INVALID_SMS = "ERR_INVALID_SMS";
    private static final String ERR_NO_SMS_APP = "ERR_NO_SMS_APP";

    public SmsManagerPlugin() {}

    @PluginMethod()
    public void send(final PluginCall call) {
        saveCall(call);
        sendSms(call);
    }

    private void sendSms(final PluginCall call) {
        SmsMessage smsMessage = getSmsMessage(call);
        if (smsMessage != null && smsMessage.getText() != null) {

            String separator = ";";
            if (android.os.Build.MANUFACTURER.equalsIgnoreCase("Samsung")) {
                // See http://stackoverflow.com/questions/18974898/send-sms-through-intent-to-multiple-phone-numbers/18975676#18975676
                separator = ",";
            }
            String phoneNumber = smsMessage.getJoinedNumbers(separator);

            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.putExtra("sms_body", smsMessage.getText());
            // See http://stackoverflow.com/questions/7242190/sending-sms-using-intent-does-not-add-recipients-on-some-devices
            smsIntent.putExtra("address", phoneNumber);
            smsIntent.setData(Uri.parse("smsto:" + Uri.encode(phoneNumber)));

            if (smsIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivityForResult(call, smsIntent, SMS_INTENT_REQUEST_CODE);
            } else {
                Log.e(getLogTag(), ERR_NO_SMS_APP + ": Intent action: " + smsIntent.getAction());
                call.reject(ERR_NO_SMS_APP);
            }
        } else {
            Log.e(getLogTag(), ERR_INVALID_SMS + ": Both number and text of a sms message are required!");
            call.reject(ERR_INVALID_SMS);
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

    private SmsMessage getSmsMessage(PluginCall call) {
        try {
            JSArray numberArray = call.getArray("numbers");
            String text = ConfigUtils.getCallParam(String.class, call, "text");
            return new SmsMessage(numberArray.<String>toList(), text);
        } catch (JSONException ignore) {}
        return null;
    }

}
