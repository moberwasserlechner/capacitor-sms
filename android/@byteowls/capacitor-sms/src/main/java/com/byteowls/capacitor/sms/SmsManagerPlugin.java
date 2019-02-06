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
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import org.json.JSONException;

import java.util.ArrayList;

@NativePlugin(requestCodes = { SmsManagerPlugin.SEND_REQUEST_CODE, SmsManagerPlugin.PERMISSION_REQUEST_CODE }, name = "SmsManager")
public class SmsManagerPlugin extends Plugin {

    static final int SEND_REQUEST_CODE = 2200;
    static final int PERMISSION_REQUEST_CODE = 2300;
    static final int INTENT_RESULT_CODE = 2311;
    private static final String PARAM_ANDROID_SUBSCRIPTION_ID = "android.subscriptionId";
    private static final String PARAM_ANDROID_OPEN_SMS_APP = "android.openSmsApp";

    public SmsManagerPlugin() {}

    @PluginMethod()
    public void send(final PluginCall call) {
        saveCall(call);
        sendSms(call);
    }

    @PluginMethod()
    public void hasPermission(final PluginCall call) {
        saveCall(call);
        hasPermissionForSms(call);
    }

    private void hasPermissionForSms(final PluginCall call) {
        boolean useSmsApp = ConfigUtils.getCallParam(Boolean.class, call, PARAM_ANDROID_OPEN_SMS_APP, false);
        if (!useSmsApp) {
            if (isPermissionGranted(PERMISSION_REQUEST_CODE, Manifest.permission.SEND_SMS)) {
                call.resolve();
            }
        } else {
            call.resolve();
        }

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

                boolean useSmsApp = ConfigUtils.getCallParam(Boolean.class, call, PARAM_ANDROID_OPEN_SMS_APP, false);
                if (!useSmsApp) {
                    if (isPermissionGranted(SEND_REQUEST_CODE, Manifest.permission.SEND_SMS)) {
                        if (hasPhoneFeature()) {
                            String subscriptionId = ConfigUtils.getCallParam(String.class, call, PARAM_ANDROID_SUBSCRIPTION_ID);
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
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.putExtra("sms_body", smsMessage.getText());
                    // See http://stackoverflow.com/questions/7242190/sending-sms-using-intent-does-not-add-recipients-on-some-devices
                    smsIntent.putExtra("address", phoneNumber);
                    smsIntent.setData(Uri.parse("smsto:" + Uri.encode(phoneNumber)));

                    if (smsIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivityForResult(call, smsIntent, INTENT_RESULT_CODE);
                    } else {
                        Log.e(getLogTag(), "@byteowls/capacitor-sms: No app for " + smsIntent.getAction());
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
        if (requestCode == SEND_REQUEST_CODE || requestCode == PERMISSION_REQUEST_CODE ) {
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

            if (PERMISSION_REQUEST_CODE == requestCode) {
                hasPermissionForSms(savedCall);
            } else {
                sendSms(savedCall);
            }
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
            String text = ConfigUtils.getCallParam(String.class, call, "text");
            return new SmsMessage(numberArray.<String>toList(), text);
        } catch (JSONException ignore) {}
        return null;
    }

    private boolean hasPhoneFeature() {
        return getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }



}
