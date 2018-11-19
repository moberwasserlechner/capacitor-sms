package com.byteowls.capacitor.sms;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@NativePlugin(requestCodes = { SmsManagerPlugin.REQUEST_CODE }, name = "SmsManager")
public class SmsManagerPlugin extends Plugin {

    static final int REQUEST_CODE = 2200;
    static final int INTENT_RESULT_CODE = 2311;
    private static final String PARAM_ANDROID_SUBSCRIPTION_ID = "android.subscriptionId";
    private static final String PARAM_ANDROID_OPEN_SMS_APP = "android.openSmsApp";

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

                boolean useSmsApp = getCallParam(Boolean.class, call, PARAM_ANDROID_OPEN_SMS_APP, false);
                if (!useSmsApp) {
                    if (isPermissionGranted(REQUEST_CODE, Manifest.permission.SEND_SMS)) {
                        if (hasPhoneFeature()) {
                            String subscriptionId = getCallParam(String.class, call, PARAM_ANDROID_SUBSCRIPTION_ID);
                            SmsManager manager;
                            if (subscriptionId == null || subscriptionId.isEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
                                manager = SmsManager.getDefault();
                            } else {
                                manager = SmsManager.getSmsManagerForSubscriptionId(Integer.valueOf(subscriptionId));
                            }

                            final ArrayList<String> textParts = manager.divideMessage(smsMessage.getText());

                            // by creating this broadcast receiver we can check whether or not the SMS was sent
                            final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                                int partsCount = textParts.size(); //number of parts to send

                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    String error = null;
                                    switch (getResultCode()) {
                                        case SmsManager.STATUS_ON_ICC_SENT:
                                        case Activity.RESULT_OK:
                                            break;
                                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                            error = "ERROR_GENERIC_FAILURE";
                                            break;
                                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                                            error = "ERROR_NO_SERVICE";
                                            break;
                                        case SmsManager.RESULT_ERROR_NULL_PDU:
                                            error = "ERROR_NULL_PDU";
                                            break;
                                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                                            error = "ERROR_RADIO_OFF";
                                            break;
                                    }
                                    // trigger the callback only when all the parts have been sent
                                    partsCount--;
                                    if (partsCount == 0) {
                                        if (error != null) {
                                            call.reject(error);
                                        } else {
                                            call.resolve();
                                        }
                                        getActivity().unregisterReceiver(this);
                                    }
                                }
                            };

                            // randomize the intent filter action to avoid using the same receiver
                            String intentFilterAction = "SMS_SENT" + java.util.UUID.randomUUID().toString();
                            getActivity().registerReceiver(broadcastReceiver, new IntentFilter(intentFilterAction));

                            PendingIntent sentIntent = PendingIntent.getBroadcast(getActivity(), 0, new Intent(intentFilterAction), 0);

                            try {

                                if (textParts.size() > 1) {
                                    ArrayList<PendingIntent> sentIntents = new ArrayList<>();
                                    for (int i = 0; i < textParts.size(); i++) {
                                        sentIntents.add(sentIntent);
                                    }
                                    manager.sendMultipartTextMessage(phoneNumber, null, textParts, sentIntents, null);

                                } else {
                                    manager.sendTextMessage(phoneNumber,
                                        null,
                                        textParts.get(0),
                                        sentIntent,
                                        null);
                                }
                            } catch (SecurityException e) {
                                Log.e(getLogTag(), "@byteowls/capacitor-sms: unexpected security exception", e);
                                call.reject("UNKNOWN_ERROR");
                            }
                        } else {
                            call.reject("TELEPHONY_NOT_SUPPORTED");
                        }
                    }
                } else {
                    Intent smsIntent = new Intent(Intent.ACTION_SEND);

                    // See http://stackoverflow.com/questions/7242190/sending-sms-using-intent-does-not-add-recipients-on-some-devices
                    smsIntent.setData(Uri.parse("smsto:" + phoneNumber));  // This ensures only SMS apps respond
                    smsIntent.putExtra("address", phoneNumber);
                    smsIntent.putExtra("sms_body", smsMessage.getText());
                    if (smsIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivityForResult(call, smsIntent, INTENT_RESULT_CODE);
                    } else {
                        String msg = "@byteowls/capacitor-sms: No app for " + smsIntent.getAction();
                        Log.e(getLogTag(), msg);
                        call.reject("NO_SMS_APP_FOUND");
                    }
                }
            } else {
                Log.e(getLogTag(), "Both number and text of a sms message are required!");
                call.reject("INVALID_SMS");
            }

    }

    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            final PluginCall savedCall = getSavedCall();
            if (savedCall == null) {
                return;
            }

            for (int i = 0; i < grantResults.length; i++) {
                int result = grantResults[i];
                String perm = permissions[i];
                if (result == PackageManager.PERMISSION_DENIED) {
                    Log.d(getLogTag(), "User denied sms permission!" + perm);
                    savedCall.reject("PERMISSION_DENIED");
                    return;
                }
            }
            sendSms(savedCall);
        }
    }

    private boolean isPermissionGranted(int permissionRequestCode, String permission) {
        if (hasPermission(permission)) {
            return true;
        } else {
            pluginRequestPermission(permission, permissionRequestCode);
            return false;
        }
    }

    private SmsMessage getSmsMessage(PluginCall call) {
        try {
            JSArray numberArray = call.getArray("numbers");
            String text = getCallParam(String.class, call, "text");
            return new SmsMessage(numberArray.<String>toList(), text);
        } catch (JSONException ignore) {}
        return null;
    }

    private boolean hasPhoneFeature() {
        return getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

//    private List<SmsMessage> getSmsMessages(PluginCall call) {
//        List<SmsMessage> result = new ArrayList<>();
//
//        JSArray messageArray = call.getArray(PARAM_MESSAGES);
//
//        try {
//            List<JSONObject> messagesJSON = messageArray.toList();
//            for (JSONObject jsonMessage : messagesJSON) {
//                JSObject jsObject = JSObject.fromJSONObject(jsonMessage);
//                SmsMessage smsMessage = new SmsMessage(jsObject.getString("number"), jsObject.getString("text"));
//                result.add(smsMessage);
//            }
//        } catch (JSONException e) {
//            call.error("Provided notification format is invalid");
//        }
//        return result;
//    }

    private <T> T getCallParam(Class<T> clazz, PluginCall call, String key) {
        return getCallParam(clazz, call, key, null);
    }

    private <T> T getCallParam(Class<T> clazz, PluginCall call, String key, T defaultValue) {
        String k = getDeepestKey(key);
        try {
            JSONObject o = getDeepestObject(call.getData(), key);

            Object value = null;
            if (clazz.isAssignableFrom(String.class)) {
                value = o.getString(k);
            } else if (clazz.isAssignableFrom(Boolean.class)) {
                value = o.getBoolean(k);
            } else if (clazz.isAssignableFrom(Double.class)) {
                value = o.getDouble(k);
            } else if (clazz.isAssignableFrom(Integer.class)) {
                value = o.getInt(k);
            } else if (clazz.isAssignableFrom(Float.class)) {
                Double doubleValue = o.getDouble(k);
                value = doubleValue.floatValue();
            } else if (clazz.isAssignableFrom(Integer.class)) {
                value = o.getInt(k);
            }
            if (value == null) {
                return defaultValue;
            }
            return (T) value;
        } catch (Exception ignore) {}
        return defaultValue;
    }

    private String getDeepestKey(String key) {
        String[] parts = key.split("\\.");
        if (parts.length > 0) {
            return parts[parts.length - 1];
        }
        return null;
    }

    private JSObject getDeepestObject(JSObject o, String key) {
        // Split on periods
        String[] parts = key.split("\\.");
        // Search until the second to last part of the key
        for (int i = 0; i < parts.length-1; i++) {
            String k = parts[i];
            o = o.getJSObject(k);
        }
        return o;
    }

}
