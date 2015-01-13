package cn.flyaudio.weather.data;



import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;



/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:16:45
 */
public class YahooWeatherApplication extends Application {
	
	private boolean isServiceRunning = true;
	public static  Boolean isCNLanguage = true;
	public static  Boolean  isINfoFromYahoo = false;
	private static  Context mContext;
	@Override
	public void onCreate() {
		mContext =  getApplicationContext();
		getlanguage();
		// TODO Auto-generated method stub
		super.onCreate();
	}

	public static void getlanguage() {
		
		Resources resources = mContext.getResources();
		Configuration config = resources.getConfiguration();
		   String mlang = config.locale.toString();
		   Log.d("zzz","mlang = "+mlang);
		 
		   if(!mlang.endsWith("zh_CN")){
			   isCNLanguage = false;
		   }else{
			   isCNLanguage =true;
		   }
	}

	public boolean isServiceRunning() {
		return isServiceRunning;
	}

	public void setServiceRunning(boolean isServiceRunning) {
		this.isServiceRunning = isServiceRunning;
	}


}