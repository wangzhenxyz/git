package cn.flyaudio.weather.activity;

import cn.flyaudio.Weather.R;

import cn.flyaudio.weather.adapter.ViewFlowAdapter;
import cn.flyaudio.weather.data.YahooWeatherApplication;
import cn.flyaudio.weather.service.YahooService;
import cn.flyaudio.weather.util.Constant;
import cn.flyaudio.weather.view.ViewFlow;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;


/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:15:49
 */
public class WeatherDetailsActivity extends Activity {
	private final String TAG = Constant.TAG;
	private final Boolean DEBUG_FLAG = Constant.DEBUG_FLAG;
	private ViewFlow mViewFlow;
	private Button mFreshButton1, mBackButton,mChooseCityButton,mFreshButton;
	private ViewFlowAdapter mViewFlowAdapter;
	private UpdateUIReceiver mUIReceiver;
	private Animation mAnim;
	private View weatherdetailview;
	public static int widthPixels = 0;
	private final int NOTIFYDATACHANGED = 0;
	private Toast mToast;
//	RGBReceiver mRGBReceiver = new RGBReceiver();
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mViewFlowAdapter.notifyDataSetChanged();
			if (DEBUG_FLAG)
				Log.d(TAG,
						"[WeatherDetailsActivity] Notify mViewFlowAdapter Changed...");
		};
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter filter = new IntentFilter("action.flyaudio.colortheme");
//		registerReceiver(mRGBReceiver, filter);
		if (DEBUG_FLAG)
			Log.d(TAG, "[WeatherDetailsActivity][onCreate]");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// get the DisplayMetrics widthPixels
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthPixels = dm.widthPixels;
		if (DEBUG_FLAG)
			Log.d(TAG,
					"[WeatherDetailsActivity][onCreate]getDisplayMetrics == "
							+ widthPixels);

		setContentView(R.layout.details_activity_layout);
		weatherdetailview =findViewById(R.id.weatherdetail);
		mViewFlow = (ViewFlow)findViewById(R.id.viewflow);
		mAnim = AnimationUtils.loadAnimation(this, R.anim.weather_loading);
		mAnim.setInterpolator(new LinearInterpolator());

		mFreshButton1 = (Button) findViewById(R.id.freshbutton);
	//	mFreshButton1.setOnClickListener(freshListener);
		
		mFreshButton = (Button) findViewById(R.id.fresh_button);
		mFreshButton.setOnClickListener(freshListener);

		mBackButton = (Button) findViewById(R.id.back_button1);
		mBackButton.setOnClickListener(backListener);

		mChooseCityButton = (Button) findViewById(R.id.choosecity_button);
		mChooseCityButton.setOnClickListener(choosecityListener);
		
		SharedPreferences shared = getSharedPreferences("weather", 0);
		mViewFlow = (ViewFlow) findViewById(R.id.viewflow);
		mViewFlowAdapter = new ViewFlowAdapter(this, this);
		
		
		
		int currentpage = shared.getInt("current", 0) == 0 ? 0 : shared.getInt(
				"current", 0) - 1;
		if (DEBUG_FLAG)
			Log.d(TAG, "[WeatherDetailsActivity]   currentpage---->"
					+ currentpage);
		
		for(int i=0;i<currentpage;i++){			
				if(shared.getString(String.valueOf(i+1), "").equals("")){
					currentpage = currentpage-1;
			}
		}
		
		mViewFlow.setAdapter(mViewFlowAdapter, currentpage);
		registerIntentFilter();

	}

	private void registerIntentFilter() {
		mUIReceiver = new UpdateUIReceiver();
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(Constant.ACTION_UPDATEUI_VIEWFLOW);
		iFilter.addAction(Constant.ACTION_UPDATEUI);
		iFilter.addAction(Constant.ACTION_START_FRESH);
		iFilter.addAction(Constant.ACTION_STOP_FRESH);
		registerReceiver(mUIReceiver, iFilter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		weatherdetailview.setBackgroundColor(Integer.parseInt(SystemProperties.get(Constant.PROPERTY_COLORTHEME,"-65536")));
		// weatherdetailview.setBackgroundColor(getSharedPreferences("weather", 0).getInt("weather_widget_color", 0x9900ff00));
       if (DEBUG_FLAG)
			Log.d(TAG, "[WeatherDetailsActivity][onResume]");

		if (((YahooWeatherApplication) getApplicationContext()).isServiceRunning()) {
			if (isNetworkAvailable(this)) {
				//mFreshButton1.setVisibility(View.VISIBLE);
				//mFreshButton1.startAnimation(mAnim);
			} else
			{
				//mFreshButton1.setVisibility(View.INVISIBLE);
				Toast.makeText(this,
						getResources().getString(R.string.neworkconnect),
						Toast.LENGTH_SHORT).show();
			}
		}

		// send Message ......NOTIFYDATACHANGED.....
		Message msg = mHandler.obtainMessage(NOTIFYDATACHANGED);
		mHandler.sendMessage(msg);
	}

	private class UpdateUIReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(Constant.ACTION_UPDATEUI_VIEWFLOW)) {
				Message msg = mHandler.obtainMessage(NOTIFYDATACHANGED);
				mHandler.sendMessage(msg);
			} else if (intent.getAction().equals(Constant.ACTION_STOP_FRESH)) {
				//mFreshButton1.setVisibility(View.INVISIBLE);
				mFreshButton1.clearAnimation();
			} else if (intent.getAction().equals(Constant.ACTION_START_FRESH)) {
				mFreshButton1.startAnimation(mAnim);
				//mFreshButton1.setVisibility(View.VISIBLE);
				
			}
		}
	}

	private View.OnClickListener backListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent mIntentHome = new Intent(Constant.ACTION_HOME);
			mIntentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				startActivity(mIntentHome);
			} catch (Exception e) {
			} finally {
				finish();
			}
		}
	};
	
	
	
	
	private View.OnClickListener choosecityListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			
			
			// TODO Auto-generated method stub
			Intent i = new Intent(WeatherDetailsActivity.this, CityEditPageActivity.class);
			WeatherDetailsActivity.this.startActivity(i);
			WeatherDetailsActivity.this.finish();
			
			
		}
	};

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	private View.OnClickListener freshListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			cancelToast();
			if (isNetworkAvailable(WeatherDetailsActivity.this)) {
				// TODO Auto-generated method stub
				if (!YahooService.isServiceRunning()) {
					Intent intent = new Intent(WeatherDetailsActivity.this,
							YahooService.class);
					startService(intent);
				}
				showToast(getString(R.string.showToast));		
				//mFreshButton1.setVisibility(View.VISIBLE);
				mFreshButton1.startAnimation(mAnim);
			} else
				showToast(getString(R.string.neworkconnect));
		}
	};
	
	public void showToast(String text) {      
		mToast = Toast.makeText(WeatherDetailsActivity.this, text, Toast.LENGTH_SHORT);  
		mToast.show();  
    }  
      
    public void cancelToast() {  
            if (mToast != null) {  
                mToast.cancel();  
            }  
        }  
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (DEBUG_FLAG)
			Log.d(TAG, "[WeatherDetailsActivity][onDestroy]");
		unregisterReceiver(mUIReceiver);
	}
	
	public void addcityOnclick(View v){
		
	}
	
	
//	
//	class RGBReceiver extends BroadcastReceiver{
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			Log.d("rgb","Weather Detail onReceive ");
//			int rgb = intent.getIntExtra("rgb", -1);
//			Log.d("rgb","Weather Detail rgb="+rgb);
//			weatherdetailview.setBackgroundColor(rgb);
//		}
//	}

}
