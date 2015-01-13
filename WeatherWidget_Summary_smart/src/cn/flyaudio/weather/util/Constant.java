/**
 * 
 */
package cn.flyaudio.weather.util;


/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:18:46
 */
public class Constant {
	public static final String TAG ="weather";
	public static final Boolean DEBUG_FLAG =true;
	
	//For yahoo weather .........................begin
	// Action for updateUI 
	public static final String  ACTION_UPDATEUI="cn.flyaudio.action.UPDATEUI_";
	public static final String  ACTION_STOP_FRESH="cn.flyaudio.action.STOP_FRESH";
	public static final String  ACTION_START_FRESH="cn.flyaudio.action.START_FRESH";
	public static final String  ACTION_UPDATEUI_VIEWFLOW="cn.flyaudio.action.UPDATEUI_VIEWFLOW";
	
	
	
	// Action UPDATE_ADAPTER
	public static final String  ACTION_UPDATE_ADAPTER="cn.flyaudio.ACTION.UPDATE_ADAPTER";
	
	//Action ACTION_APPWIDGET_UPDATE
	public static final String  ACTION_APPWIDGET_UPDATE="android.appwidget.action.APPWIDGET_UPDATE";
	
	//
	public static final String  ACTION_UPDATE_ALL="com.android.flyaudio.weather.UPDATE_ALL";
	
	

	// default city code  "广州--2161838"
	public static final String  DEFAULT_CITYCODE="101280101";


		
	
	// Action for  LaunchHome
	public static final String  ACTION_HOME="android.intent.action.HOME";
	
	// For yahoo weather url and param
	public static final String YAHOO_WEATHER_URL="http://weather.yahooapis.com/forecastrss?w=";
	public static final String YAHOO_WEATHER_URL_PARAM = "&u=c";
	
	
	// uint of weather
	public static final String  UINT_PERCENT = "%";
	public static final String  UINT_DISTANCE = "km";
	public static final String  UINT_TEMP = "°";
	public static final String  UINT_SPEED = "km/h";
	
	//For yahoo weather .........................end
	
	
	// the amount of cities in  add_city_activity  GridView
	public static final Integer  AMOUNT_CITY = 12;
	
	public static final String PROPERTY_COLORTHEME ="persist.fly.colortheme";

	//httpclient for TIME_OUT_DELAY 4s
	public static final int GETCITY_TIME_OUT_DELAY = 3000;
	public static final int GETWEATHER_TIME_OUT_DELAY = 6000;
}
