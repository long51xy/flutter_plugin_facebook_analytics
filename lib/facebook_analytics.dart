import 'package:flutter/services.dart';

class FacebookAnalytics {
  static const MethodChannel _channel =
      const MethodChannel('facebook_analytics');

  void init({bool isDebugEnabled, bool autoLogAppEventsEnabled}) async {
    Map<String, dynamic> options = {};
    if (isDebugEnabled != null) {
      options["isDebugEnabled"] = isDebugEnabled;
    }
    if (autoLogAppEventsEnabled != null) {
      options["autoLogAppEventsEnabled"] = autoLogAppEventsEnabled;
    }
    await _channel.invokeMethod('init', options);
  }

  void logEvent(String eventName, {double valueToSum, Map<String, dynamic> parameters}) async {
    Map<String, dynamic> options = {};
    options["eventName"] = eventName;
    if (valueToSum != null) {
      options["valueToSum"] = valueToSum;
    }
    if (parameters != null) {
      options["parameters"] = parameters;
    }
    await _channel.invokeMethod('logEvent', options);
  }
}
