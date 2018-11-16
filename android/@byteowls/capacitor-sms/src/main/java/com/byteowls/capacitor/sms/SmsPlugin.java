package com.byteowls.capacitor.sms;

import android.content.Intent;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import org.json.JSONException;
import org.json.JSONObject;

@NativePlugin(requestCodes = { SmsPlugin.REQUEST_CODE }, name = "Sms")
public class SmsPlugin extends Plugin {

    static final int REQUEST_CODE = 2200;

    public SmsPlugin() {}

    @PluginMethod()
    public void send(final PluginCall call) {
//        String appId = getCallString(call, PARAM_APP_ID);
//        String androidAppId = getCallString(call, PARAM_ANDROID_APP_ID);
//        if (androidAppId != null && !androidAppId.isEmpty()) {
//            appId = androidAppId;
//        }
//
//        if (appId == null || appId.length() == 0) {
//            call.reject("Option '"+PARAM_APP_ID+"' or '"+PARAM_ANDROID_APP_ID+"' is required!");
//            return;
//        }
//
//        String baseUrl = getCallString(call, PARAM_AUTHORIZATION_BASE_URL);
//        if (baseUrl == null || baseUrl.length() == 0) {
//            call.reject("Option '"+PARAM_AUTHORIZATION_BASE_URL+"' is required!");
//            return;
//        }
//        String accessTokenEndpoint = getCallString(call, PARAM_ACCESS_TOKEN_ENDPOINT); // placeholder
//        if (accessTokenEndpoint == null || accessTokenEndpoint.length() == 0) {
//            call.reject("Option '"+PARAM_ACCESS_TOKEN_ENDPOINT+"' is required!");
//            return;
//        }
//        String customScheme = getCallString(call, PARAM_ANDROID_CUSTOM_SCHEME);
//        if (customScheme == null || customScheme.length() == 0) {
//            call.reject("Option '"+ PARAM_ANDROID_CUSTOM_SCHEME +"' is required!");
//            return;
//        }
//
//        String responseType = getCallString(call, PARAM_RESPONSE_TYPE);
//        String androidResponseType = getCallString(call, PARAM_ANDROID_RESPONSE_TYPE);
//        if (androidResponseType != null && !androidResponseType.isEmpty()) {
//            responseType = androidResponseType;
//        }
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode) {
            final PluginCall savedCall = getSavedCall();
            if (savedCall == null) {
                return;
            }

            savedCall.reject("No authToken retrieved!");
        }
    }


    private String getCallString(PluginCall call, String key) {
        return getCallString(call, key, null);
    }

    private String getCallString(PluginCall call, String key, String defaultValue) {
        String k = getDeepestKey(key);
        try {
            JSONObject o = getDeepestObject(call.getData(), key);

            String value = o.getString(k);
            if (value == null) {
                return defaultValue;
            }
            return value;
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

    private JSObject getDeepestObject(JSObject o, String key) throws JSONException {
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
