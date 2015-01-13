package cn.flyaudio.weather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.media.audiofx.Equalizer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.flyaudio.Weather.R;

import cn.flyaudio.weather.data.YahooWeatherApplication;
import cn.flyaudio.weather.objectInfo.FullWeatherInfo;
import cn.flyaudio.weather.view.ListItemView;
import cn.flyaudio.weather.view.MoreDetailListItemView;

/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:19:09
 */
public class UtilsTools {
	private final static String TAG = Constant.TAG;
	private final static Boolean DEBUG_FLAG = Constant.DEBUG_FLAG;

	public static boolean isConnect(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			if (DEBUG_FLAG)
				Log.d(TAG, "[Utils] isConnect() == "
						+ connectivityManager.getActiveNetworkInfo()
								.isAvailable());
			return connectivityManager.getActiveNetworkInfo().isAvailable();
		} catch (Exception e) {
			return false;
		}
	}

	public static void setConvertViewWithShared(Context context, View v,
			String city) {
		SharedPreferences wShared = context.getSharedPreferences("weather", 0);
		int index = 0;

		FullWeatherInfo mForecastyweathers = null;
		for (int i = 1; i < 5; i++)
			if (city.equals(wShared.getString(String.valueOf(i), null)))
				index = i;

		switch (index) {
		case 1:
			mForecastyweathers = getWeatherInfoFormSharedPreferences(context,
					context.getSharedPreferences("FirstWeather", 0), 1);

			break;
		case 2:
			mForecastyweathers = getWeatherInfoFormSharedPreferences(context,
					context.getSharedPreferences("SecondWeather", 0), 2);
			break;
		case 3:
			mForecastyweathers = getWeatherInfoFormSharedPreferences(context,
					context.getSharedPreferences("ThirdWeather", 0), 3);
			break;
		case 4:
			mForecastyweathers = getWeatherInfoFormSharedPreferences(context,
					context.getSharedPreferences("FouthWeather", 0), 4);
			break;
		}

		setDataView(context, v, mForecastyweathers);
	}

	public static FullWeatherInfo getWeatherInfoFormSharedPreferences(
			Context context, SharedPreferences shared, int index) {
		FullWeatherInfo mForecastyweathers = new FullWeatherInfo();

		mForecastyweathers.setCondition_text(shared.getString("condition_text",
				""));
		mForecastyweathers.setCondition_code(shared.getString("condition_code",
				"-1"));
		mForecastyweathers.setCondition_temp(shared.getString("condition_temp",
				""));
		mForecastyweathers.setCondition_date(shared.getString("condition_date",
				""));
		mForecastyweathers.setHumidity(shared.getString("humidity", "--"));
		mForecastyweathers.setVisibility(shared.getString("visibility", "--"));

		mForecastyweathers
				.setWinddirection(shared.getString("direction", "-1"));
		mForecastyweathers.setWindspeed(shared.getString("speed", ""));
		mForecastyweathers.setSunrise(shared.getString("sunrise", "--"));
		mForecastyweathers.setSunset(shared.getString("sunset", "--"));
		mForecastyweathers.setDataflag(shared.getBoolean("dataflag", false));
		mForecastyweathers.setCity(shared.getString("city",
				getCityNameWithIndex(context, index)));
		mForecastyweathers.setCityPinyin(shared.getString("city",
				getCityPinyinNameWithIndex(context, index)));

		mForecastyweathers.setFeelslike(shared.getString("feelslike", "--"));
		// mForecastyweathers.setCityPinyin(shared.getString("city_en", ""));

		for (int i = 0; i < 5; i++) {
			mForecastyweathers.yweathers[i].setCode(shared.getString("code"
					+ (i + 1), "-1"));
			mForecastyweathers.yweathers[i].setText(shared.getString("text"
					+ (i + 1), ""));
			mForecastyweathers.yweathers[i].setDay(shared.getString("day"
					+ (i + 1), ""));
			mForecastyweathers.yweathers[i].setDate(shared.getString("date"
					+ (i + 1), ""));
			mForecastyweathers.yweathers[i].setLow(shared.getString("low"
					+ (i + 1), ""));
			mForecastyweathers.yweathers[i].setHigh(shared.getString("high"
					+ (i + 1), ""));
		}

		return mForecastyweathers;
	}

	public static void setDataView(Context context, View v,
			FullWeatherInfo mForecastyweathers) {

		setTextView((TextView) v.findViewById(R.id.weathercity),
				mForecastyweathers.getCity());
		setTextView((TextView) v.findViewById(R.id.weathercity_pinyin),
				mForecastyweathers.getCityPinyin());
		// setTextView((TextView) v.findViewById(R.id.week), getSystemWeek());
		setTextView((TextView) v.findViewById(R.id.date),
				getSystemDate(context.getString(R.string.timeformat)));

		if (mForecastyweathers.getDataflag()) {
			v.findViewById(R.id.curTemp_parent).setVisibility(View.VISIBLE);
			v.findViewById(R.id.weather_lowhigh).setVisibility(View.VISIBLE);

			setTextView((TextView) v.findViewById(R.id.curTemp),
					mForecastyweathers.getCondition_temp());
			if (isDay(mForecastyweathers.getSunrise(),
					mForecastyweathers.getSunset())) {

				setTextView(
						(TextView) v.findViewById(R.id.weathernum),
						getSmartWeatherByNum(context,
								mForecastyweathers.getCondition_code()));
			} else {

				setTextView(
						(TextView) v.findViewById(R.id.weathernum),
						getSmartWeatherByNum(context,
								mForecastyweathers.getCondition_code()));
			}

			setTextView((TextView) v.findViewById(R.id.weather_lowhigh),
					mForecastyweathers.yweathers[0].getLow() + " ~ "
							+ mForecastyweathers.yweathers[0].getHigh());

		}
		setTextView((TextView) v.findViewById(R.id.datafrom), context
				.getResources().getString(R.string.datafromyahoo));

		setTextView((TextView) v.findViewById(R.id.weatherdate),
				getPubDate(context, mForecastyweathers.getCondition_date()));
		ImageView img = (ImageView) v.findViewById(R.id.set);

		img.setImageBitmap(BitmapFactory.decodeResource(
				context.getResources(),
				parsesmartBgBycode(mForecastyweathers.getCondition_code(),
						mForecastyweathers.getSunrise(),
						mForecastyweathers.getSunset())));

		((ListItemView) v.findViewById(R.id.weather1)).setInfo(
				getWeekName(context, mForecastyweathers.yweathers[0].getDay(),
						0),
				getTimeShort(mForecastyweathers.yweathers[0].getDate(), 0,
						context),
				getNotNullValue(mForecastyweathers.yweathers[0].getLow())
						+ "°~",
				getNotNullValue(mForecastyweathers.yweathers[0].getHigh())
						+ "°",
				getWeatherIconl(context,
						mForecastyweathers.yweathers[0].getCode()),
				getSmartWeatherByNum(context,
						mForecastyweathers.yweathers[0].getCode()));

		((ListItemView) v.findViewById(R.id.weather2)).setInfo(
				getWeekName(context, mForecastyweathers.yweathers[1].getDay(),
						1),
				getTimeShort(mForecastyweathers.yweathers[1].getDate(), 1,
						context),
				getNotNullValue(mForecastyweathers.yweathers[1].getLow())
						+ "°~",
				getNotNullValue(mForecastyweathers.yweathers[1].getHigh())
						+ "°",
				getWeatherIconl(context,
						mForecastyweathers.yweathers[1].getCode())

				,
				getSmartWeatherByNum(context,
						mForecastyweathers.yweathers[1].getCode()));

		((ListItemView) v.findViewById(R.id.weather3)).setInfo(
				getWeekName(context, mForecastyweathers.yweathers[2].getDay(),
						2),
				getTimeShort(mForecastyweathers.yweathers[2].getDate(), 2,
						context),
				getNotNullValue(mForecastyweathers.yweathers[2].getLow())
						+ "°~",
				getNotNullValue(mForecastyweathers.yweathers[2].getHigh())
						+ "°",
				getWeatherIconl(context,
						mForecastyweathers.yweathers[2].getCode()),
				getSmartWeatherByNum(context,
						mForecastyweathers.yweathers[2].getCode()));

		((MoreDetailListItemView) v.findViewById(R.id.detail2)).setInfo(context
				.getString(R.string.windspeed),
				getsmartwindspeed(context, mForecastyweathers.getWindspeed()),
				BitmapFactory.decodeResource(context.getResources(),
						R.drawable.detail_wind));
		((MoreDetailListItemView) v.findViewById(R.id.detail3)).setInfo(
				context.getString(R.string.winddirection),
				getsmartwinddirection(context,
						mForecastyweathers.getWinddirection()), BitmapFactory
						.decodeResource(context.getResources(),
								R.drawable.detail_wind));
		((MoreDetailListItemView) v.findViewById(R.id.detail4)).setInfo(context
				.getString(R.string.sunrise),
				getNotNullValue(mForecastyweathers.getSunrise()), BitmapFactory
						.decodeResource(context.getResources(),
								R.drawable.detail_uvindex));
		((MoreDetailListItemView) v.findViewById(R.id.detail5)).setInfo(context
				.getString(R.string.sunset), getNotNullValue(mForecastyweathers
				.getSunset()), BitmapFactory.decodeResource(
				context.getResources(), R.drawable.detail_uvindex));

	}

	public static String getPubDate(Context context, String date) {
		if (YahooWeatherApplication.isINfoFromYahoo) {

			String timem = null;
			if (date != "") {
				if (date.contains("am")) {
					date = date.substring(0, date.indexOf("am"));
					timem = "am";
				}
				if (date.contains("pm")) {
					date = date.substring(0, date.indexOf("pm"));
					timem = "pm";
				}

				Date mDate = new Date(date);

				if (mDate != null) {

					SimpleDateFormat formatter = new SimpleDateFormat(
							"dd-MM-yyyy HH:mm");
					String dateString = formatter.format(mDate);
					if (dateString != null)
						return context.getString(R.string.updatetime,
								dateString, timem);
				}
			}

		} else {
			return context.getString(R.string.updatetime, date, "");
		}
		return "";
	}

	public static void setTextView(TextView textView, String text) {
		textView.setText(text);
	}

	public static String getSystemDate(String dateformat) {
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		Date date = new Date(System.currentTimeMillis());
		return format.format(date);
	}

	public static String getSystemWeek() {
		SimpleDateFormat format = new SimpleDateFormat("EEEE");
		Date date = new Date(System.currentTimeMillis());
		return format.format(date);
	}

	public static Bitmap getWeatherIconl(Context context, String pos) {

		Bitmap bm = BitmapFactory.decodeResource(
				context.getResources(),
				YahooWeatherApplication.isINfoFromYahoo ? UtilsTools
						.parseIconBycode(pos) : UtilsTools.parsesmartIcon(pos));
		int width = bm.getWidth();
		int height = bm.getHeight();
		int newWidth1 = 107;
		int newHeight1 = 90;
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;

	}

	public static String getCityNameWithIndex(Context context, int index) {
		SharedPreferences shared = context.getSharedPreferences("weather", 0);
		String city = shared.getString(String.valueOf(index), "");
		/*
		 * if (city.length() >= 7 && city.contains("-")) city =
		 * city.split("-")[1];
		 */
		return city;
	}

	public static String getCityPinyinNameWithIndex(Context context, int index) {
		SharedPreferences shared = context.getSharedPreferences("weather", 0);
		String city = shared.getString(String.valueOf(index * 10), "");
		return city;
	}

	/* 保存天气信息 */
	public static void saveForecastfweathers(Context context,
			SharedPreferences p, FullWeatherInfo mForecastyweathers) {
		SharedPreferences.Editor editor = p.edit();
		editor.putString("condition_text",
				mForecastyweathers.getCondition_text());
		editor.putString("condition_code",
				mForecastyweathers.getCondition_code());
		editor.putString("condition_temp",
				mForecastyweathers.getCondition_temp());
		editor.putString("condition_date",
				mForecastyweathers.getCondition_date());
		editor.putString("humidity", mForecastyweathers.getHumidity());
		editor.putString("visibility", mForecastyweathers.getVisibility());
		editor.putString("direction", mForecastyweathers.getWinddirection());
		editor.putString("speed", mForecastyweathers.getWindspeed());
		editor.putString("feelslike", mForecastyweathers.getFeelslike());
		editor.putString("sunrise", mForecastyweathers.getSunrise());
		editor.putString("sunset", mForecastyweathers.getSunset());
		editor.putBoolean("dataflag", mForecastyweathers.getDataflag());
		int index = 0;
		for (int i = 0; i < 5; i++) {
			index = i + 1;
			editor.putString("code" + index,
					mForecastyweathers.yweathers[i].getCode());
			editor.putString("text" + index,
					mForecastyweathers.yweathers[i].getText());
			editor.putString("day" + index,
					mForecastyweathers.yweathers[i].getDay());
			editor.putString("date" + index,
					mForecastyweathers.yweathers[i].getDate());
			editor.putString("low" + index,
					mForecastyweathers.yweathers[i].getLow());
			editor.putString("high" + index,
					mForecastyweathers.yweathers[i].getHigh());
		}
		editor.commit();
	}

	private static String getNotNullValue(String value) {
		if (value != null && value != "") {
			return value;
		}
		return "--";
	}

	private static String getsmartwindspeed(Context context, String value) {
		int sunnyNum = R.string.nullstring;
		if (value == null || value.equals("") || value.equals("-1"))
			return "";
		int codeValue = Integer.parseInt(value);
		switch (codeValue) {
		case 0:
			sunnyNum = R.string.smart_weather_wind_speed0;
			break;
		case 1:
			sunnyNum = R.string.smart_weather_wind_speed1;
			break;
		case 2:
			sunnyNum = R.string.smart_weather_wind_speed2;
			break;
		case 3:
			sunnyNum = R.string.smart_weather_wind_speed3;
			break;
		case 4:
			sunnyNum = R.string.smart_weather_wind_speed4;
			break;
		case 5:
			sunnyNum = R.string.smart_weather_wind_speed5;
			break;
		case 6:
			sunnyNum = R.string.smart_weather_wind_speed6;
			break;
		case 7:
			sunnyNum = R.string.smart_weather_wind_speed7;
			break;
		case 8:
			sunnyNum = R.string.smart_weather_wind_speed8;
			break;
		case 9:
			sunnyNum = R.string.smart_weather_wind_speed9;
			break;
		default:
			return "";
		}
		return context.getResources().getString(sunnyNum);
	}

	private static String getsmartwinddirection(Context context, String value) {
		int sunnyNum = R.string.nullstring;
		if (value == null || value.equals("") || value.equals("-1"))
			return "";
		int codeValue = Integer.parseInt(value);
		switch (codeValue) {
		case 0:
			sunnyNum = R.string.smart_weather_wind_direct0;
			break;
		case 1:
			sunnyNum = R.string.smart_weather_wind_direct1;
			break;
		case 2:
			sunnyNum = R.string.smart_weather_wind_direct2;
			break;
		case 3:
			sunnyNum = R.string.smart_weather_wind_direct3;
			break;
		case 4:
			sunnyNum = R.string.smart_weather_wind_direct4;
			break;
		case 5:
			sunnyNum = R.string.smart_weather_wind_direct5;
			break;
		case 6:
			sunnyNum = R.string.smart_weather_wind_direct6;
			break;
		case 7:
			sunnyNum = R.string.smart_weather_wind_direct7;
			break;
		case 8:
			sunnyNum = R.string.smart_weather_wind_direct8;
			break;
		case 9:
			sunnyNum = R.string.smart_weather_wind_direct9;
			break;
		default:
			return "";
		}
		return context.getResources().getString(sunnyNum);
	}

	/* 获取星期 字符串 */
	private static String getWeekName(Context context, String date, int index) {
		// TODO Auto-generated method stub
		String systemweek = new SimpleDateFormat("yyyy-MM-dd").format(new Date(
				System.currentTimeMillis()));
		if (DEBUG_FLAG)
			Log.d(TAG, "[Utils] getWeekName() == " + systemweek);
		// String s = "2008-08-08";
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			Date date2 = df.parse(systemweek);
			Log.d(TAG, "[Utils] getWeekName() date2== " + date2);
			if (date.equals(date2.toString().split(" ")[0]))
				return context.getResources().getString(R.string.Today);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date.equals("Mon")) {
			return context.getString(R.string.Monday);
		} else if (date.equals("Tue")) {
			return context.getString(R.string.Tuesday);
		} else if (date.equals("Wed")) {
			return context.getString(R.string.Wednesday);
		} else if (date.equals("Thu")) {
			return context.getString(R.string.Thursday);
		} else if (date.equals("Fri")) {
			return context.getString(R.string.Friday);
		} else if (date.equals("Sat")) {
			return context.getString(R.string.Saturday);
		} else if (date.equals("Sun")) {
			return context.getString(R.string.Sunday);
		}
		SimpleDateFormat format = new SimpleDateFormat(
				context.getString(R.string.weekformat));
		return format.format(new Date(System.currentTimeMillis() + index
				* (24 * 60 * 60 * 1000)));
	}

	/* 网络是否连接 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	public static String getTimeShort(String strDate, int index, Context context) {
		Date date;
		String dateString = "";
		SimpleDateFormat formatter = new SimpleDateFormat(
				context.getString(R.string.dateformat));
		try {
			if (strDate != "") {
				date = new Date(strDate);
				if (date == null)
					return "";
			} else {
				date = new Date(System.currentTimeMillis() + index
						* (24 * 60 * 60 * 1000));
			}
			dateString = formatter.format(date);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return dateString;
	}

	public static boolean isDay(String sunrise, String sunset) {
		Date date = new Date(System.currentTimeMillis());
		int hour = date.getHours();
		int minutes = date.getMinutes();
		Boolean isDay = true;
		if (sunrise == "--" && sunset == "--") {
			return isDay;
		} else if (sunrise == "--") {
			isDay = false;
			return isDay;
		}

		try {
			int hour_sunrise = Integer.parseInt(sunrise.substring(0,
					sunrise.indexOf(":")));
			int minutes_sunrise = Integer.parseInt(sunrise.substring(
					sunrise.indexOf(":") + 1, sunrise.indexOf(" ")));
			int hour_sunset = Integer.parseInt(sunset.substring(0,
					sunrise.indexOf(":"))) + 12;
			int minutes_sunset = Integer.parseInt(sunset.substring(
					sunrise.indexOf(":") + 1, sunrise.indexOf(" ")));

			if ((hour_sunrise < hour || (hour_sunrise == hour && minutes_sunrise <= minutes))
					&& ((hour_sunset > hour || (hour_sunset == hour && minutes_sunset >= minutes)))) {
				isDay = true;
			} else {
				isDay = false;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isDay;
	}

	/* 获取天气类型 解析代码chinese weather */
	public static String getSmartWeatherByNum(Context context, String strIcon) {
		int sunnyNum = R.string.weather_num_100;
		if (strIcon == null)
			return "";
		if ("00".equals(strIcon))
			sunnyNum = R.string.weather_num_00;// 晴天
		if ("01".equals(strIcon))
			sunnyNum = R.string.weather_num_01;// 多云
		if ("02".equals(strIcon))
			sunnyNum = R.string.weather_num_02;// 阴天
		if ("03".equals(strIcon))
			sunnyNum = R.string.weather_num_03;// 阵雨
		if ("04".equals(strIcon))
			sunnyNum = R.string.weather_num_04;// 雷阵雨
		if ("05".equals(strIcon))
			sunnyNum = R.string.weather_num_05;// 雷阵雨伴有冰雹
		if ("06".equals(strIcon))
			sunnyNum = R.string.weather_num_06;// 雨夹雪
		if ("07".equals(strIcon))
			sunnyNum = R.string.weather_num_07;// 小雨
		if ("08".equals(strIcon))
			sunnyNum = R.string.weather_num_08;// 中雨
		if ("09".equals(strIcon))
			sunnyNum = R.string.weather_num_09;// 大雨
		if ("10".equals(strIcon))
			sunnyNum = R.string.weather_num_10;// 暴雨
		if ("11".equals(strIcon))
			sunnyNum = R.string.weather_num_11;// 大暴雨
		if ("12".equals(strIcon))
			sunnyNum = R.string.weather_num_12;// 特大暴雨
		if ("13".equals(strIcon))
			sunnyNum = R.string.weather_num_13;// 阵雪
		if ("14".equals(strIcon))
			sunnyNum = R.string.weather_num_14;// 小雪
		if ("15".equals(strIcon))
			sunnyNum = R.string.weather_num_15;// 中雪
		if ("16".equals(strIcon))
			sunnyNum = R.string.weather_num_16;// 大雪
		if ("17".equals(strIcon))
			sunnyNum = R.string.weather_num_17;// 暴雪
		if ("18".equals(strIcon))
			sunnyNum = R.string.weather_num_18;// 雾
		if ("19".equals(strIcon))
			sunnyNum = R.string.weather_num_19;// 冻雨
		if ("20".equals(strIcon))
			sunnyNum = R.string.weather_num_20;// 沙尘暴
		if ("21".equals(strIcon))
			sunnyNum = R.string.weather_num_21;// 小雨-中雨
		if ("22".equals(strIcon))
			sunnyNum = R.string.weather_num_22;// 中雨-大雨
		if ("23".equals(strIcon))
			sunnyNum = R.string.weather_num_23;// 大雨-暴雨
		if ("24".equals(strIcon))
			sunnyNum = R.string.weather_num_24;// 暴雨-大暴雨
		if ("25".equals(strIcon))
			sunnyNum = R.string.weather_num_25;// 大暴雨-特大暴雨
		if ("26".equals(strIcon))
			sunnyNum = R.string.weather_num_26;// 小雪-中雪
		if ("27".equals(strIcon))
			sunnyNum = R.string.weather_num_27;// 中雪-大雪
		if ("28".equals(strIcon))
			sunnyNum = R.string.weather_num_28;// 大雪-暴雪
		if ("29".equals(strIcon))
			sunnyNum = R.string.weather_num_29;// 浮尘
		if ("30".equals(strIcon))
			sunnyNum = R.string.weather_num_30;// 扬沙
		if ("31".equals(strIcon))
			sunnyNum = R.string.weather_num_31;// 强沙尘暴
		if ("53".equals(strIcon))
			sunnyNum = R.string.weather_num_53;// 霾
		if ("99".equals(strIcon))
			sunnyNum = R.string.weather_num_99;// 无
		return context.getResources().getString(sunnyNum);
	}

	/* 获取天气小图片 解析天气代码 */// xia
	public static int parsesmartIcon(String strIcon) {// ///
		if (strIcon == null)
			return R.drawable.ds_sunny_day_night;
		if ("00".equals(strIcon))
			return R.drawable.ds_sunny_day_night;// 晴天
		if ("01".equals(strIcon))
			return R.drawable.ds_partly_cloudy_day;// 多云
		if ("02".equals(strIcon))
			return R.drawable.ds_cloud_day;// 阴天
		if ("03".equals(strIcon))
			return R.drawable.ds_showers_day_night;// 阵雨
		if ("04".equals(strIcon))
			return R.drawable.ds_thundershowers_day_night;// 雷阵雨
		if ("05".equals(strIcon))
			return R.drawable.ds_thundershowerwithhail_day_night;// 冰雹
		if ("06".equals(strIcon))
			return R.drawable.ds_sleet_day_night;// 雨夹雪
		if ("07".equals(strIcon))
			return R.drawable.ds_drizzle_day_night;// 小雨
		if ("08".equals(strIcon))
			return R.drawable.ds_moderaterain_day_night;// 中雨
		if ("09".equals(strIcon))
			return R.drawable.ds_severe_thunderstorms_day_night;// 大雨
		if ("10".equals(strIcon))
			return R.drawable.ds_thunderstorm_day_night;// 暴雨
		if ("11".equals(strIcon))
			return R.drawable.ds_severe_thunderstorms_day_night;// 大暴雨
		if ("12".equals(strIcon))
			return R.drawable.ds_tropical_storm_day_night;// 特大暴雨
		if ("13".equals(strIcon))
			return R.drawable.ds_snow_showers_day_night;// 阵雪
		if ("14".equals(strIcon))
			return R.drawable.ds_snow_flurries_day_night;// 小雪
		if ("15".equals(strIcon))
			return R.drawable.ds_snow_day_night;// 中雪
		if ("16".equals(strIcon))
			return R.drawable.ds_blowingsnow_day_night;// 大雪
		if ("17".equals(strIcon))
			return R.drawable.ds_heavy_snow_day_night;// 暴雪
		if ("18".equals(strIcon))
			return R.drawable.ds_smoky_day_night;// 雾
		if ("19".equals(strIcon))
			return R.drawable.ds_freezing_rain_day_night;// 冻雨
		if ("20".equals(strIcon))
			return R.drawable.ds_duststorm_day_night;// 沙尘暴
		if ("21".equals(strIcon))
			return R.drawable.ds_moderaterain_day_night;// 小雨-中雨
		if ("22".equals(strIcon))
			return R.drawable.ds_severe_thunderstorms_day_night;// 中雨-大雨
		if ("23".equals(strIcon))
			return R.drawable.ds_thunderstorm_day_night;// 大雨-暴雨
		if ("24".equals(strIcon))
			return R.drawable.ds_severe_thunderstorms_day_night;// 暴雨-大暴雨
		if ("25".equals(strIcon))
			return R.drawable.ds_tropical_storm_day_night;// 大暴雨-特大暴雨
		if ("26".equals(strIcon))
			return R.drawable.ds_snow_day_night;// 小雪-中雪
		if ("27".equals(strIcon))
			return R.drawable.ds_blowingsnow_day_night;// 中雪-大雪
		if ("28".equals(strIcon))
			return R.drawable.ds_heavy_snow_day_night;// 大雪-暴雪
		if ("29".equals(strIcon))
			return R.drawable.ds_dust_day_night;// 浮尘
		if ("30".equals(strIcon))
			return R.drawable.ds_sand_day_night;// 扬沙
		if ("31".equals(strIcon))
			return R.drawable.ds_sandstorm_day_night;// 强沙尘暴
		if ("53".equals(strIcon))
			return R.drawable.ds_haze_day_night;// 霾
		if ("99".equals(strIcon))
			return R.drawable.weather_na;
		;
		return R.drawable.weather_na;
	}

	// 20140308 换肤进度到这里。da
	public static int parsesmartBgBycode(String strIcon, String sunrise,
			String sunset) {
		if (strIcon == null)
			return R.drawable.weather_bg_sunny_d;
		if ("00".equals(strIcon))
			return R.drawable.weather_bg_sunny_d;// 晴天
		if ("01".equals(strIcon))
			return R.drawable.weather_bg_partly_cloudy_d;// 多云
		if ("02".equals(strIcon))
			return R.drawable.weather_bg_cloud_d;// 阴天
		if ("03".equals(strIcon))
			return R.drawable.weather_bg_showers_d;// 阵雨
		if ("04".equals(strIcon))
			return R.drawable.weather_bg_thundershowers_d;// 雷阵雨
		if ("05".equals(strIcon))
			return R.drawable.weather_bg_thundershowerwithhail;// 冰雹
		if ("06".equals(strIcon))
			return R.drawable.weather_bg_sleet_d;// 雨夹雪
		if ("07".equals(strIcon))
			return R.drawable.weather_bg_drizzle_d;// 小雨
		if ("08".equals(strIcon))
			return R.drawable.weather_bg_moderaterain;// 中雨
		if ("09".equals(strIcon))
			return R.drawable.weather_bg_severe_thunderstorms_d;// 大雨
		if ("10".equals(strIcon))
			return R.drawable.weather_bg_thunderstorms_d;// 暴雨
		if ("11".equals(strIcon))
			return R.drawable.weather_bg_severe_thunderstorms_d;// 大暴雨
		if ("12".equals(strIcon))
			return R.drawable.weather_bg_tropical_storm_d;// 特大暴雨
		if ("13".equals(strIcon))
			return R.drawable.weather_bg_snow_showers_d;// 阵雪
		if ("14".equals(strIcon))
			return R.drawable.weather_bg_snow_flurries_d;// 小雪
		if ("15".equals(strIcon))
			return R.drawable.weather_bg_snow_d;// 中雪
		if ("16".equals(strIcon))
			return R.drawable.weather_bg_blowingsnow_d;// 大雪
		if ("17".equals(strIcon))
			return R.drawable.weather_bg_heavy_snow_d;// 暴雪
		if ("18".equals(strIcon))
			return R.drawable.weather_bg_foggy_d;// 雾
		if ("19".equals(strIcon))
			return R.drawable.weather_bg_freezingrain_d;// 冻雨
		if ("20".equals(strIcon))
			return R.drawable.weather_bg_duststorm;// 沙尘暴
		if ("21".equals(strIcon))
			return R.drawable.weather_bg_moderaterain;// 小雨-中雨
		if ("22".equals(strIcon))
			return R.drawable.weather_bg_severe_thunderstorms_d;// 中雨-大雨
		if ("23".equals(strIcon))
			return R.drawable.weather_bg_thunderstorms_d;// 大雨-暴雨
		if ("24".equals(strIcon))
			return R.drawable.weather_bg_severe_thunderstorms_d;// 暴雨-大暴雨
		if ("25".equals(strIcon))
			return R.drawable.weather_bg_tropical_storm_d;// 大暴雨-特大暴雨
		if ("26".equals(strIcon))
			return R.drawable.weather_bg_snow_d;// 小雪-中雪
		if ("27".equals(strIcon))
			return R.drawable.weather_bg_blowingsnow_d;// 中雪-大雪
		if ("28".equals(strIcon))
			return R.drawable.weather_bg_heavy_snow_d;// 大雪-暴雪
		if ("29".equals(strIcon))
			return R.drawable.weather_bg_dust_d;// 浮尘
		if ("30".equals(strIcon))
			return R.drawable.weather_bg_sand;// 扬沙
		if ("31".equals(strIcon))
			return R.drawable.weather_bg_sandstorm;// 强沙尘暴
		if ("53".equals(strIcon))
			return R.drawable.weather_bg_haze_d;// 霾
		if ("99".equals(strIcon))
			return R.drawable.weather_bg_sunny_d;
		return R.drawable.weather_bg_sunny_d;
	}

	// widgetapp
	public static int parsesmartWidgetbgBycode(String strIcon, String sunrise,
			String sunset) {
		if (strIcon == null)
			return R.drawable.weather_widget_default_bg;
		if ("00".equals(strIcon))
			return R.drawable.weather_widget_default_bg;// 晴天
		if ("01".equals(strIcon))
			return R.drawable.weather_widget_partly_cloudy_d;// 多云
		if ("02".equals(strIcon))
			return R.drawable.weather_widget_cloudy_d;// 阴天
		if ("03".equals(strIcon))
			return R.drawable.weather_widget_showers_d;// 阵雨
		if ("04".equals(strIcon))
			return R.drawable.weather_widget_thundershowers_d;// 雷阵雨
		if ("05".equals(strIcon))
			return R.drawable.weather_widget_thundershowerwithhail;// 冰雹
		if ("06".equals(strIcon))
			return R.drawable.weather_widget_sleet_d;// 雨夹雪
		if ("07".equals(strIcon))
			return R.drawable.weather_widget_drizzle_d;// 小雨
		if ("08".equals(strIcon))
			return R.drawable.weather_widget_moderaterain;// 中雨
		if ("09".equals(strIcon))
			return R.drawable.weather_widget_heavyrain;// 大雨
		if ("10".equals(strIcon))
			return R.drawable.weather_widget_thunderstorms_d;// 暴雨
		if ("11".equals(strIcon))
			return R.drawable.weather_widget_severe_thunderstorms_d;// 大暴雨
		if ("12".equals(strIcon))
			return R.drawable.weather_widget_tropical_storm_d;// 特大暴雨
		if ("13".equals(strIcon))
			return R.drawable.weather_widget_snowflurry_d;// 阵雪
		if ("14".equals(strIcon))
			return R.drawable.weather_widget_lightsnow;// 小雪
		if ("15".equals(strIcon))
			return R.drawable.weather_widget_snow_d;// 中雪
		if ("16".equals(strIcon))
			return R.drawable.weather_widget_heavysnow_d;// 大雪
		if ("17".equals(strIcon))
			return R.drawable.weather_widget_snowstorm;// 暴雪
		if ("18".equals(strIcon))
			return R.drawable.weather_widget_foggy_d;// 雾
		if ("19".equals(strIcon))
			return R.drawable.weather_widget_freezing_rain;// 冻雨
		if ("20".equals(strIcon))
			return R.drawable.weather_widget_duststorm;// 沙尘暴
		if ("21".equals(strIcon))
			return R.drawable.weather_widget_moderaterain;// 小雨-中雨
		if ("22".equals(strIcon))
			return R.drawable.weather_widget_heavyrain;// 中雨-大雨
		if ("23".equals(strIcon))
			return R.drawable.weather_widget_thunderstorms_d;// 大雨-暴雨
		if ("24".equals(strIcon))
			return R.drawable.weather_widget_severe_thunderstorms_d;// 暴雨-大暴雨
		if ("25".equals(strIcon))
			return R.drawable.weather_widget_tropical_storm_d;// 大暴雨-特大暴雨
		if ("26".equals(strIcon))
			return R.drawable.weather_widget_snow_d;// 小雪-中雪
		if ("27".equals(strIcon))
			return R.drawable.weather_widget_heavysnow_d;// 中雪-大雪
		if ("28".equals(strIcon))
			return R.drawable.weather_widget_snowstorm;// 大雪-暴雪
		if ("29".equals(strIcon))
			return R.drawable.weather_widget_dust_d;// 浮尘
		if ("30".equals(strIcon))
			return R.drawable.weather_widget_sand;// 扬沙
		if ("31".equals(strIcon))
			return R.drawable.weather_widget_sandstorm;// 强沙尘暴
		if ("53".equals(strIcon))
			return R.drawable.weather_widget_haze_d;// 霾
		if ("99".equals(strIcon))
			return R.drawable.weather_widget_sunny_d;

		return R.drawable.weather_widget_sunny_d;
	}

	public static int parseIconBycode(String code) {
		if (code == null || code.equals("") || code.equals("-1"))
			return R.drawable.weather_na;
		int codeValue = Integer.parseInt(code);
		switch (codeValue) {
		case 0:
			return R.drawable.ds_tornado_day_night;
		case 1:
			return R.drawable.ds_tropical_storm_day_night;
		case 2:
			return R.drawable.ds_hurricane_day_night;
		case 3:
			return R.drawable.ds_severe_thunderstorms_day_night;
		case 4:
			return R.drawable.ds_thunderstorm_day_night;
		case 5:
			return R.drawable.ds_sleet_day_night;
		case 6:
			return R.drawable.ds_sleet_day_night;
		case 7:
			return R.drawable.ds_sleet_day_night;
		case 8:
			return R.drawable.ds_freezing_drizzle_day_night;
		case 9:
			return R.drawable.ds_drizzle_day_night;
		case 10:
			return R.drawable.ds_freezing_rain_day_night;
		case 11:
			return R.drawable.ds_showers_day_night;
		case 12:
			return R.drawable.ds_showers_day_night;
		case 13:
			return R.drawable.ds_snow_flurries_day_night;
		case 14:
			return R.drawable.ds_snow_flurries_day_night;
		case 15:
			return R.drawable.ds_blowingsnow_day_night;
		case 16:
			return R.drawable.ds_snow_day_night;
		case 17:
			return R.drawable.ds_hail_day_night;
		case 18:
			return R.drawable.ds_sleet_day_night;
		case 19:
			return R.drawable.ds_dust_day_night;
		case 20:
			return R.drawable.ds_foggy_day_night;
		case 21:
			return R.drawable.ds_haze_day_night;
		case 22:
			return R.drawable.ds_smoky_day_night;
		case 23:
			return R.drawable.ds_windy_day_night;
		case 24:
			return R.drawable.ds_windy_day_night;
		case 25:
			return R.drawable.ds_cold_day_night;
		case 26:
			return R.drawable.ds_cloud_night;
		case 27:
			return R.drawable.ds_cloud_night;
		case 28:
			return R.drawable.ds_cloud_day;
		case 29:
			return R.drawable.ds_partly_cloudy_night;
		case 30:
			return R.drawable.ds_partly_cloudy_day;
		case 31:
			return R.drawable.ds_fair_night;
		case 32:
			return R.drawable.ds_fair_day;
		case 33:
			return R.drawable.ds_sunny_day_night;
		case 34:
			return R.drawable.ds_sunny_day_night;
		case 35:
			return R.drawable.ds_mixed_rain_hail_day_night;
		case 36:
			return R.drawable.ds_hot_day_night;
		case 37:
			return R.drawable.ds_thundershowers_day_night;
		case 38:
			return R.drawable.ds_thundershowers_day_night;
		case 39:
			return R.drawable.ds_thundershowers_day_night;
		case 40:
			return R.drawable.ds_showers_day_night;
		case 41:
			return R.drawable.ds_heavy_snow_day_night;
		case 42:
			return R.drawable.ds_snow_showers_day_night;
		case 43:
			return R.drawable.ds_heavy_snow_day_night;
		case 44:
			return R.drawable.ds_partly_cloudy_day;
		case 45:
			return R.drawable.ds_thundershowers_day_night;
		case 46:
			return R.drawable.ds_snow_showers_day_night;
		case 47:
			return R.drawable.ds_thundershowers_day_night;
		default:
			return R.drawable.ds_sunny_day_night;
		}
	}

	public static Bitmap getBitmap(int color, BitmapDrawable bitmapDrawable1) {
		Bitmap bitmap1 = bitmapDrawable1.getBitmap();
		Bitmap mybaseBitmap = Bitmap.createBitmap(bitmap1.getWidth(),
				bitmap1.getHeight(), bitmap1.getConfig());
		Canvas canvas1 = new Canvas(mybaseBitmap);
		Paint paint1 = new Paint();
		paint1.setColor(color);
		canvas1.drawRect(0, 0, bitmap1.getWidth(), bitmap1.getHeight(), paint1);
		canvas1.drawBitmap(mybaseBitmap, 0, 0, paint1);

		Bitmap baseBitmap = Bitmap.createBitmap(bitmap1.getWidth(),
				bitmap1.getHeight(), bitmap1.getConfig());
		Canvas canvas = new Canvas(baseBitmap);
		Paint paint = new Paint();
		canvas.drawBitmap(mybaseBitmap, 0, 0, paint);
		// 显示图片交集下面那个图片
		paint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.DST_IN));
		canvas.drawBitmap(bitmap1, 0, 0, paint);
		paint.setXfermode(null);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return baseBitmap;
	}

}
