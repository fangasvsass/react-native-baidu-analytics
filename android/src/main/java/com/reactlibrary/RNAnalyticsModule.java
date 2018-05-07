
package com.reactlibrary;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

public class RNAnalyticsModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNAnalyticsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNAnalytics";
  }


  @ReactMethod
  public void start(ReadableMap options) {
    if (options == null) {
      StatService.start(this.reactContext);
    } else {
      StatService.setAppKey(options.getString("appKey"));

      String appChannel = options.getString("appChannel");
      if (appChannel != null && appChannel != "") {
        StatService.setAppChannel(this.reactContext, appChannel, true);
      } else {
        StatService.setAppChannel(this.reactContext, "", false);
      }

      StatService.setDebugOn(options.getBoolean("isDebug"));

      if (options.getBoolean("enableExceptionLog")) {
        StatService.setOn(this.reactContext, StatService.EXCEPTION_LOG);
      }

      StatService.setSessionTimeOut(options.getInt("sessionTimeOut"));
      StatService.setLogSenderDelayed(options.getInt("logSenderDelayed"));

      SendStrategyEnum sendStrategy = SendStrategyEnum.valueOf(options.getString("sendStrategy"));

      if (sendStrategy != null) {
        boolean onlyWifi = options.getBoolean("onlyWifi");
        int logSendInterval = options.getInt("logSendInterval");
        StatService.setSendLogStrategy(this.reactContext, sendStrategy, logSendInterval, onlyWifi);
      }
    }
  }

  @ReactMethod
  public void onPageStart(String name) {
    StatService.onPageStart(this.reactContext, name);
  }

  @ReactMethod
  public void onPageEnd(String name) {
    StatService.onPageEnd(this.reactContext, name);
  }

  @ReactMethod
  public void onPageResume() {
    StatService.onResume(this.reactContext);
  }

  @ReactMethod
  public void onPagePause() {
    StatService.onPause(this.reactContext);
  }

  @ReactMethod
  public void setDebugOn(Boolean isDebug) {
    StatService.setDebugOn(isDebug);
  }

  @ReactMethod
  public void onEvent(String eventId, String label) {
    StatService.onEvent(this.reactContext, eventId, label);
  }

  @ReactMethod
  public void onEventStart(String eventId, String label) {
    StatService.onEventStart(this.reactContext, eventId, label);
  }

  @ReactMethod
  public void onEventEnd(String eventId, String label) {
    StatService.onEventEnd(this.reactContext, eventId, label);
  }

  @ReactMethod
  public void onEventWithAttributes(String eventId, String label, ReadableMap attributes) {
    StatService.onEvent(this.reactContext, eventId, label, 1, toStringHashMap(attributes));
  }

  @ReactMethod
  public void onEventDuration(String eventId, String label, Integer milliseconds) {
    StatService.onEventDuration(this.reactContext, eventId, label, milliseconds.longValue());
  }

  private HashMap<String, String> toStringHashMap(ReadableMap source) {
    if (source == null) {
      return null;
    }
    HashMap<String, String> result = new HashMap<>();
    for (Map.Entry<String, Object> entry : source.toHashMap().entrySet()) {
      if (entry.getValue() instanceof String) {
        result.put(entry.getKey(), (String) entry.getValue());
      }
    }
    return result;
  }

  public String getChannelFromAPK() {
    try {
      PackageManager pm = reactContext.getPackageManager();
      ApplicationInfo appInfo = pm.getApplicationInfo(reactContext.getPackageName(), PackageManager.GET_META_DATA);
      if (appInfo.metaData != null) {
        return appInfo.metaData.getString("BaiduMobAd_CHANNEL");
      }
      return null;
    } catch (PackageManager.NameNotFoundException ignored) {
    }
    return null;
  }

  public void setSpChannel() {
    SharedPreferences sp = reactContext.
        getSharedPreferences("BaiduMobAd_CHANNEL", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    String channel = getChannelFromAPK();
    editor.putString("channel", channel);
    editor.commit();
  }

  public String getSpChannel() {
    SharedPreferences sp = reactContext.
        getSharedPreferences("BaiduMobAd_CHANNEL", Context.MODE_PRIVATE);
    String channel = sp.getString("channel", "");
    return channel;
  }

  @ReactMethod
  public void getChannel(Promise promise) {
    String channel = getSpChannel();
    if (TextUtils.isEmpty(channel)) {
      setSpChannel();
      channel = getSpChannel();
    }
    promise.resolve(channel);

  }

}

