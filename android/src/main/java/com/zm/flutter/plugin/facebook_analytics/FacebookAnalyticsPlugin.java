package com.zm.flutter.plugin.facebook_analytics;

import androidx.annotation.NonNull;

import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FacebookAnalyticsPlugin */
public class FacebookAnalyticsPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  private FacebookAnalyticsDelegate facebookAnalyticsDelegate;

  private ActivityPluginBinding pluginBinding;

  private static String CHANNEL_NAME = "facebook_analytics";

  public FacebookAnalyticsPlugin() {
  }

  /**
   * Plugin registration for Flutter version < 1.12
   */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL_NAME);
    channel.setMethodCallHandler(new FacebookAnalyticsPlugin(registrar));
  }

  /**
   * Constructor for Flutter version < 1.12
   *
   * @param registrar
   */
  private FacebookAnalyticsPlugin(Registrar registrar) {
    this.facebookAnalyticsDelegate = new FacebookAnalyticsDelegate(registrar.activity());
    registrar.addActivityResultListener(facebookAnalyticsDelegate);
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), CHANNEL_NAME);
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
//    if (call.method.equals("getPlatformVersion")) {
//      result.success("Android " + android.os.Build.VERSION.RELEASE);
//    } else {
//      result.notImplemented();
//    }

    switch (call.method) {
      case "init":
        facebookAnalyticsDelegate.init((Map<String, Object>) call.arguments, result);
        break;
      case "logEvent":
        facebookAnalyticsDelegate.logEvent((Map<String, Object>) call.arguments, result);
        break;
      default:
        result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.facebookAnalyticsDelegate = new FacebookAnalyticsDelegate(binding.getActivity());
    this.pluginBinding = binding;
    binding.addActivityResultListener(facebookAnalyticsDelegate);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {
    pluginBinding.removeActivityResultListener(facebookAnalyticsDelegate);
    pluginBinding = null;
  }
}
