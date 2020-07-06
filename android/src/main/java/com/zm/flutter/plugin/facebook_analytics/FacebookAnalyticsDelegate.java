package com.zm.flutter.plugin.facebook_analytics;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;

import io.flutter.Log;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.MethodChannel.Result;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FacebookAnalyticsDelegate implements PluginRegistry.ActivityResultListener {
    private static final String ERROR_RESULT_CODE = "-1";

    private final Activity activity;
    private Result pendingResult;
    private AppEventsLogger logger;

    public FacebookAnalyticsDelegate(Activity activity) {
        this.activity = activity;
    }

    void init(Map<String, Object> arguments, Result result) {
        this.pendingResult = result;

        try {
            JSONObject options = new JSONObject(arguments);
            boolean isDebugEnabled = options.optBoolean("isDebugEnabled", false);
            boolean autoLogAppEventsEnabled = options.optBoolean("autoLogAppEventsEnabled", true);
            FacebookSdk.setAutoLogAppEventsEnabled(autoLogAppEventsEnabled);
            FacebookSdk.setIsDebugEnabled(isDebugEnabled);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
            Log.d("Facebook applicationId:", FacebookSdk.getApplicationId());
            if (FacebookSdk.isInitialized()) {
                Log.d("Facebook init:", "success");
            } else {
                Log.d("Facebook init:", "fail");
            }
            String keyHash = printKeyHash(activity);
            Log.d("Facebook keyHash:", keyHash);

            logger = AppEventsLogger.newLogger(getApplicationContext());
            result.success("Facebook Analytics init succeed");
        } catch (Exception e) {
            Log.e("Facebook Analytics init exception", e.getMessage());
            e.printStackTrace();
            result.error(ERROR_RESULT_CODE, "Facebook Analytics init exception", e);
        }
    }

    void logEvent(Map<String, Object> arguments, Result result) {
        this.pendingResult = result;
        try {
            JSONObject options = new JSONObject(arguments);
            String eventName = options.getString("eventName");
            JSONObject parameters = options.optJSONObject("parameters");
            Double valueToSum = options.optDouble("valueToSum");

            Bundle params = new Bundle();
            if (parameters != null) {
                Iterator iterator = parameters.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    String value = parameters.getString(key);
                    params.putString(key, value);
                }
            }

            if (!params.isEmpty() && !valueToSum.isNaN()) {
                logger.logEvent(eventName, valueToSum, params);
            } else if (!valueToSum.isNaN()) {
                logger.logEvent(eventName, valueToSum);
            } else if (!params.isEmpty()) {
                logger.logEvent(eventName, params);
            } else {
                logger.logEvent(eventName);
            }
            result.success("Facebook Analytics logEvent=[" + eventName + "] succeed");
        } catch (Exception e) {
            Log.e("Facebook Analytics logEvent exception", e.getMessage());
            e.printStackTrace();
            result.error(ERROR_RESULT_CODE, "Facebook Analytics logEvent exception", e);
        }
    }

    private static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
