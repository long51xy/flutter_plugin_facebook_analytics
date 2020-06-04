import 'package:flutter/material.dart';
import 'package:facebook_analytics/facebook_analytics.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  final FacebookAnalytics _facebookAnalytics = FacebookAnalytics();

  @override
  void initState() {
    super.initState();
    _facebookAnalytics.init(isDebugEnabled: true, autoLogAppEventsEnabled: true);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
         body: Center(
          child: SizedBox(
            height: 150,
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                RaisedButton(
                  onPressed: (){
                    _facebookAnalytics.logEvent("test");
                  },
                  child: Text("logEvent test"),
                ),
                RaisedButton(
                  onPressed: (){
                    _facebookAnalytics.logEvent("test2", parameters:{'a':'a', 'b':'b'});
                  },
                  child: Text("logEvent test2"),
                ),
                RaisedButton(
                  onPressed: (){
                    _facebookAnalytics.logEvent("test3", valueToSum: 0.6);
                  },
                  child: Text("logEvent test3"),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }

}
