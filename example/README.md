# Facebook analytics Flutter SDK 

# author

zhaolong<zhaoyuen123@126.com>

# Integration Steps

## Step 1: Add Dependency


```yaml
  flutter_plugin_facebook_analytics: 0.0.1
```

<br/>

## Step 2: Add permissions (Android)

The Cashfree PG SDK requires that you add the INTERNET permission in your `Android Manifest` file.

```xml
<manifest ...>
    <uses-permission android:name="android.permission.INTERNET" />
<application ...>
```

## example

```dart
    final FacebookAnalytics _facebookAnalytics = FacebookAnalytics();
    _facebookAnalytics.init(isDebugEnabled: true, autoLogAppEventsEnabled: true);

    _facebookAnalytics.logEvent("test");

    _facebookAnalytics.logEvent("test2", parameters:{'a':'a', 'b':'b'});

    _facebookAnalytics.logEvent("test3", valueToSum: 0.6);
``` 
## doc
  analytics of the form to:
    `https://developers.facebook.com/docs/analytics/`