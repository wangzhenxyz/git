package cn.flyaudio.Weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import cn.flyaudio.weather.activity.WeatherDetailsActivity;
import cn.flyaudio.weather.data.YahooWeatherApplication;
import cn.flyaudio.weather.service.YahooService;
import cn.flyaudio.weather.util.Constant;
import cn.flyaudio.weather.util.UtilsTools;

/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:22:20
 */

public class WeatherWidget extends AppWidgetProvider {
	private final static String TAG = Constant.TAG;
	private final static Boolean DEBUG_FLAG = Constant.DEBUG_FLAG;

	public static String now_date;
	public static String sh_date;
	public static SharedPreferences shared;
	public View viewRoots;
	public RemoteViews views;

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		if (DEBUG_FLAG)
			Log.d(TAG,
					"[WeatherWidget] onReceive() action = "
							+ intent.getAction());
		if (DEBUG_FLAG)
			Log.d(TAG,
					"[WeatherWidget] onReceive() intent.current = "
							+ intent.getIntExtra("current", -1));
		if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			Log.d(TAG, "xyzandroid.net.conn.CONNECTIVITY_CHANGE");
			try {
				context.stopService(new Intent(context, YahooService.class));
			} catch (Exception e) {
				// TODO: handle exception
				if (DEBUG_FLAG)
					Log.d(TAG,
							"[WeatherWidget] onReceive() Exception == "
									+ e.toString());

			}

			if (UtilsTools.isConnect(context)) {
				if (DEBUG_FLAG)
					Log.d(TAG, "[WeatherWidget] Utils.isConnect()");
				context.startService(new Intent(context, YahooService.class));
				if (DEBUG_FLAG)
					Log.d(TAG,
							"[WeatherWidget] sendBroadcast ACTION_START_FRESH ");
				context.sendBroadcast(new Intent(Constant.ACTION_START_FRESH));
			} else {
				context.sendBroadcast(new Intent(Constant.ACTION_STOP_FRESH));
				if (DEBUG_FLAG)
					Log.d(TAG,
							"[WeatherWidget] sendBroadcast ACTION_STOP_FRESH ");
			}
			Log.d(TAG, "xyzandroid.net.conn.CONNECTIVITY_CHANGE");
		} else if (intent.getAction().equals(
				"android.intent.action.BOOT_COMPLETED")
				|| intent.getAction().equals("cn.flyaudio.action.ACCON")) {// 时间判断
			Log.d(TAG, "xyzcn.flyaudio.action.ACCON");
			shared = context.getSharedPreferences(
					getCurrentWeather(context,
							intent.getIntExtra("current", -1)), 0);
			AppWidgetManager appwidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentname = new ComponentName(context,
					WeatherWidget.class);
			appwidgetManager.updateAppWidget(componentname,
					updateAppWidget(context, shared));
			Log.d(TAG, "xyzcn.flyaudio.action.ACCON");
		} else if (intent.getAction().equals(
				"android.appwidget.action.APPWIDGET_UPDATE")) {
			Log.d(TAG, "xyzandroid.appwidget.action.APPWIDGET_UPDATE");
			AppWidgetManager appwidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentname = new ComponentName(context,
					WeatherWidget.class);
			shared = context.getSharedPreferences(
					getCurrentWeather(context,
							intent.getIntExtra("current", -1)), 0);
			appwidgetManager.updateAppWidget(componentname,
					updateAppWidget(context, shared));
			Log.d(TAG, "xyzandroid.appwidget.action.APPWIDGET_UPDATE---end");
		} else if (intent.getAction().equals("android.intent.action.TIME_SET")) {
			Log.d(TAG, "xyzintent.action.TIME_SET");
			AppWidgetManager appwidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentname = new ComponentName(context,
					WeatherWidget.class);
			shared = context.getSharedPreferences(
					getCurrentWeather(context,
							intent.getIntExtra("current", -1)), 0);
			appwidgetManager.updateAppWidget(componentname,
					updateAppWidget(context, shared));
			Log.d(TAG, "xyzintent.action.TIME_SET");
		} else if (intent.getAction().equals("action.flyaudio.colortheme")) {
			Log.d(TAG, "xyzaction.flyaudio.colortheme");
			int rgb = intent.getIntExtra("rgb", -1);
			Log.d("rgb", "weather widget rgb=" + rgb);
			SharedPreferences shared = context.getSharedPreferences("weather",
					0);
			SharedPreferences.Editor editor = shared.edit();
			editor.putInt("weather_widget_color", rgb);
			editor.commit();

			AppWidgetManager appwidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentname = new ComponentName(context,
					WeatherWidget.class);
			shared = context.getSharedPreferences(
					getCurrentWeather(context,
							intent.getIntExtra("current", -1)), 0);
			appwidgetManager.updateAppWidget(componentname,
					updateAppWidget(context, shared));
			Log.d(TAG, "xyzaction.flyaudio.colortheme");
		} else if (intent.getAction().equals(
				"android.intent.action.LOCALE_CHANGED")) {
			Log.d(TAG, "xyznt.action.LOCALE_CHANGED");
			YahooWeatherApplication.getlanguage();
			Log.d(TAG, "xyznt.action.LOCALE_CHANGED");
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.d(TAG, "xyzonUpdate");
		SharedPreferences shared = context.getSharedPreferences("weather", 0);
		SharedPreferences.Editor editor = shared.edit();

		int currentCityNum = shared.getInt("current", 1);
		String currentCityName = "city" + String.valueOf(currentCityNum);
		String city = shared.getString(currentCityName, "");

		if (city.equals("")) {
			editor.putString("city1", Constant.DEFAULT_CITYCODE);
			editor.putString("1", context.getString(R.string.guangzhou));
			editor.putString("10", context.getString(R.string.guangzhou_pinyin));
			editor.putInt("current", 1);
			editor.commit();
		}
		context.startService(new Intent(context, YahooService.class));
		Log.d(TAG, "xyzonUpdate---end");
	}

	public RemoteViews updateAppWidget(Context context, SharedPreferences shared) {
		Log.d(TAG, "xyzupdateAppWidget");
		views = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);

		Intent detailIntent1 = new Intent(context, WeatherDetailsActivity.class);
		PendingIntent pending1 = PendingIntent.getActivity(context, 0,
				detailIntent1, 0);
		views.setOnClickPendingIntent(R.id.weatherwidget, pending1);

		if (shared.getBoolean("dataflag", false)) {
			views.setTextViewText(R.id.curt_temp_text,
					shared.getString("condition_temp", "--") + "°");
			views.setInt(
					R.id.weatherwidget,
					"setBackgroundResource",
					UtilsTools.parsesmartWidgetbgBycode(
							shared.getString("condition_code", "-1"),
							shared.getString("",
									shared.getString("sunrise", "--")),
							shared.getString("sunset", "--")));
		} else {
			views.setTextViewText(R.id.curt_temp_text, "");
			views.setInt(R.id.weatherwidget, "setBackgroundResource",
					R.drawable.weather_widget_default_bg);
		}
		SharedPreferences sharedCity = context.getSharedPreferences("weather",
				0);
		int currentCityNum = sharedCity.getInt("current", 1);
		String city = sharedCity.getString(String.valueOf(currentCityNum), "");

		if (city.length() >= 7 && city.contains("-"))
			city = city.split("-")[1];
		views.setTextViewText(R.id.city_text, city);

		Log.d(TAG, "xyzupdateAppWidget---end  "+city);
		return views;
	}

	public static String getCurrentWeather(Context c, int current) {
		if (current <= 0) {
			SharedPreferences shared = c.getSharedPreferences("weather", 0);
			current = shared.getInt("current", 1);
			if (DEBUG_FLAG)
				Log.d(TAG, "[WeatherWidget] getCurrentWeather()  current="
						+ current);
		}
		switch (current) {
		case 1: {
			return "FirstWeather";
		}
		case 2: {
			return "SecondWeather";
		}
		case 3: {
			return "ThirdWeather";
		}
		case 4: {
			return "FouthWeather";
		}
		default:
			return "FirstWeather";
		}
	}

	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	public int getPastTime(Context context, SharedPreferences shared) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		now_date = sDateFormat.format(new java.util.Date());
		// 20140221 diff同步机器时间显示
		if (shared != null) {
			if (DEBUG_FLAG)
				Log.d(TAG, "[WeatherWidget] getPastTime() shared!=null");

			sh_date = shared.getString("date_y", "无");
			if (sh_date.equals("无")) {
				return 0;
			}
		} else {
			if (DEBUG_FLAG)
				Log.d(TAG, "[WeatherWidget] getPastTime()  shared=null");

			return 0;
		}
		java.util.Date before = null;
		try {
			before = df.parse(now_date);
		} catch (ParseException e) {
			System.out.println("error--before = df.parse(now_date)");
			e.printStackTrace();
		}
		java.util.Date after = null;
		try {
			after = df.parse(sh_date);
		} catch (ParseException e) {
			System.out.println("error--after = df.parse(sh_date)");
			e.printStackTrace();
		}
		long l = before.getTime() - after.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		if (DEBUG_FLAG)
			Log.d(TAG, "[WeatherWidget] day == " + day);
		return (int) day;
	}

}
