package cn.flyaudio.weather.service;

import cn.flyaudio.weather.data.YahooWeatherApplication;
import cn.flyaudio.weather.objectInfo.FullWeatherInfo;
import cn.flyaudio.weather.util.Constant;
import cn.flyaudio.weather.util.UtilsTools;
import cn.flyaudio.weather.webserviceImpl.GetWeatherByJsonParser;
import cn.flyaudio.weather.webserviceImpl.WebServiceYaHooImpl;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;

/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:18:36
 */
public class YahooService extends Service implements Runnable {
	private final String TAG = Constant.TAG;
	private final Boolean DEBUG_FLAG = Constant.DEBUG_FLAG;

	public static final int CompleteDownData = 1;
	private static boolean sThreadRunning = false;
	public static long updateTime = 0;
	private static long _1hours = 60 * 60 * 1000;
	private static long _1minute = 60 * 1000;
	private int mCityCount;
	private SharedPreferences preference, preference1, preference2,
			preference3, preference4;
	public static boolean backups[] = { false, false, false, false };

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		preference = getSharedPreferences("weather", MODE_PRIVATE);
		preference1 = getSharedPreferences("FirstWeather", MODE_PRIVATE);
		preference2 = getSharedPreferences("SecondWeather", MODE_PRIVATE);
		preference3 = getSharedPreferences("ThirdWeather", MODE_PRIVATE);
		preference4 = getSharedPreferences("FouthWeather", MODE_PRIVATE);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		if (DEBUG_FLAG)
			Log.d(TAG, "[WeatherService] onStart()  sThreadRunning == "
					+ sThreadRunning);

		if (!sThreadRunning) {
			sThreadRunning = true;
			new Thread(this).start();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void run() {
		if (!preference.getString("city1", "无").equals("无") && !backups[0]) {
			mCityCount = 1;
			if(false){
				WebServiceYaHooImpl mWebServiceYaHooImpl = new WebServiceYaHooImpl();
				FullWeatherInfo mForecastyweathers = mWebServiceYaHooImpl
						.getWeatherInfo(preference.getString("city1", "无"));
				if (mForecastyweathers != null
						&& mForecastyweathers.getDataflag()!=null) {
					UtilsTools.saveForecastfweathers(YahooService.this,
							preference1, mForecastyweathers);
					backups[0] = true;
					notifyForUpdateUI(1);
				}
			}else{
				GetWeatherByJsonParser mGetWeatherByJsonParser = new GetWeatherByJsonParser(preference.getString("city1", "无"));
				FullWeatherInfo mFullWeatherInfo = mGetWeatherByJsonParser.getWeatherinfo(1);
						
				if (mFullWeatherInfo != null
						&& mFullWeatherInfo.getDataflag()!=null) {
					UtilsTools.saveForecastfweathers(YahooService.this,
							preference1, mFullWeatherInfo);
					backups[0] = true;
					notifyForUpdateUI(1);
			}
			}
		}
		
		
	
			if (!preference.getString("city2", "无").equals("无") && !backups[1]) {
				mCityCount = 2;
				if(false){
				WebServiceYaHooImpl mWebServiceYaHooImpl = new WebServiceYaHooImpl();
				FullWeatherInfo mForecastyweathers = mWebServiceYaHooImpl
						.getWeatherInfo(preference.getString("city2", "无"));
				if (mForecastyweathers != null
						&& mForecastyweathers.getDataflag()!=null) {
					UtilsTools.saveForecastfweathers(YahooService.this,
							preference2, mForecastyweathers);
					backups[1] = true;
					notifyForUpdateUI(2);
				}
		}else{
			GetWeatherByJsonParser mGetWeatherByJsonParser = new GetWeatherByJsonParser(preference.getString("city2", "无"));
			FullWeatherInfo mFullWeatherInfo = mGetWeatherByJsonParser.getWeatherinfo(2);
					
			if (mFullWeatherInfo != null
					&& mFullWeatherInfo.getDataflag()!=null) {
				UtilsTools.saveForecastfweathers(YahooService.this,
						preference2, mFullWeatherInfo);
				backups[1] = true;
				notifyForUpdateUI(2);
		}
		}
			}
		if (!preference.getString("city3", "无").equals("无") && !backups[2]) {
			mCityCount = 3;
			
			if(false){
				WebServiceYaHooImpl mWebServiceYaHooImpl = new WebServiceYaHooImpl();
				FullWeatherInfo mForecastyweathers = mWebServiceYaHooImpl
						.getWeatherInfo(preference.getString("city3", "无"));
				if (mForecastyweathers != null
						&& mForecastyweathers.getDataflag()!=null) {
					UtilsTools.saveForecastfweathers(YahooService.this,
							preference3, mForecastyweathers);
					backups[2] = true;
					notifyForUpdateUI(3);
				}
			}else{
				GetWeatherByJsonParser mGetWeatherByJsonParser = new GetWeatherByJsonParser(preference.getString("city3", "无"));
				FullWeatherInfo mFullWeatherInfo = mGetWeatherByJsonParser.getWeatherinfo(3);
						
				if (mFullWeatherInfo != null
						&& mFullWeatherInfo.getDataflag()!=null) {
					UtilsTools.saveForecastfweathers(YahooService.this,
							preference3, mFullWeatherInfo);
					backups[2] = true;
					notifyForUpdateUI(3);
			}
			}
			
		}
		if (!preference.getString("city4", "无").equals("无") && !backups[3]) {
			mCityCount = 4;
			if(false){
				WebServiceYaHooImpl mWebServiceYaHooImpl = new WebServiceYaHooImpl();
				FullWeatherInfo mForecastyweathers = mWebServiceYaHooImpl
						.getWeatherInfo(preference.getString("city4", "无"));
				if (mForecastyweathers != null
						&& mForecastyweathers.getDataflag()!=null) {
					UtilsTools.saveForecastfweathers(YahooService.this,
							preference4, mForecastyweathers);
					backups[3] = true;
					notifyForUpdateUI(4);
				}

			}else{
				GetWeatherByJsonParser mGetWeatherByJsonParser = new GetWeatherByJsonParser(preference.getString("city4", "无"));
				FullWeatherInfo mFullWeatherInfo = mGetWeatherByJsonParser.getWeatherinfo(4);
						
				if (mFullWeatherInfo != null
						&& mFullWeatherInfo.getDataflag()!=null) {
					UtilsTools.saveForecastfweathers(YahooService.this,
							preference4, mFullWeatherInfo);
					backups[3] = true;
					notifyForUpdateUI(4);
			}
			}
			
		}
		updateTime = getUpdateTime();
		Intent updateIntent = new Intent(Constant.ACTION_UPDATE_ALL);
		updateIntent.setClass(this, YahooService.class);
		PendingIntent pending = PendingIntent.getService(this, 0, updateIntent,
				0);
		AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Time time = new Time();
		long nowMillis = System.currentTimeMillis();
		time.set(nowMillis + updateTime);
		long updateTimes = time.toMillis(true);
		alarm.set(AlarmManager.RTC_WAKEUP, updateTimes, pending);
		sThreadRunning = false;
		stopSelf();
		if (DEBUG_FLAG)
			Log.d(TAG, "[WeatherService] run()  request next updateTime at"
					+ updateTime);

	}

	private void notifyForUpdateUI(int current) {
		this.sendBroadcast(new Intent(Constant.ACTION_APPWIDGET_UPDATE));
		Intent intent = new Intent(Constant.ACTION_UPDATEUI_VIEWFLOW);
		intent.putExtra("current", current);
		this.sendBroadcast(intent);
	}

	public long getUpdateTime() {
		long systemMillis = System.currentTimeMillis();
		if (UtilsTools.isConnect(YahooService.this)) {
			for (int i = 0; i < mCityCount; i++) {
				if (!backups[i]) {
					Log.d(TAG, "[WeatherService] getUpdateTime()  12s");
					return 12000;
				}
			}
		}
		
		((YahooWeatherApplication) getApplicationContext())
				.setServiceRunning(false);
		sendBroadcast(new Intent(Constant.ACTION_STOP_FRESH));
		Log.d(TAG, "[WeatherService] getUpdateTime()  sendBroadcast Constant.ACTION_STOP_FRESH");
		for (int i = 0; i < 4; i++) {
			backups[i] = false;
			if (DEBUG_FLAG)
				Log.d(TAG, "[WeatherService] getUpdateTime()  backup" + (i + 1)
						+ " = " + backups[i]);
		}
		if (DEBUG_FLAG)
			Log.d(TAG,
					"[WeatherService] " +
					"() _1hours - ((systemMillis) % (_1hours))="
							+ (_1hours - ((systemMillis) % (_1hours))));
        // udatetime in next 1hour_1minute 10:01 11:01
 		return _1hours - ((systemMillis) % (_1hours))+_1minute;
	}

	public static boolean isServiceRunning() {
		return updateTime == 12000;
	}

}