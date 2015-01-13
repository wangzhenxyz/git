package cn.flyaudio.weather.adapter;

import java.util.ArrayList;

import cn.flyaudio.Weather.R;




import cn.flyaudio.weather.util.Constant;
import cn.flyaudio.weather.util.UtilsTools;

import android.app.Activity;

import android.content.Context;

import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;


/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:16:19
 */
public class ViewFlowAdapter extends BaseAdapter   {

	private final String TAG = Constant.TAG;
	private final Boolean DEBUG_FLAG = Constant.DEBUG_FLAG;
	private ArrayList<String> mCityList = new ArrayList<String>();
	private LayoutInflater mInflater;
	private SharedPreferences preference;
	private Context mContext;
	private Activity mActivity;
	private Animation mAnim;
	private Button mFreshButton1;

	public ViewFlowAdapter(Context context, Activity activity) {
		mContext = context;
		mActivity = activity;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		preference = context.getSharedPreferences("weather", 0);
		for (int i = 1; i <= 4; i++) {
			String city = preference.getString(String.valueOf(i), null);
			if (city != null)
				mCityList.add(city);
		}
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 4;// WeatherDetails.view_count-1
	}

	@Override
	public int getCount() {
		return mCityList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			
			convertView = mInflater.inflate(R.layout.viewflow_layout, null);
			convertView.findViewById(R.id.city_button).setOnClickListener(clickListener);						

			mAnim = AnimationUtils.loadAnimation(mContext, R.anim.weather_loading);
			mAnim.setInterpolator(new LinearInterpolator());
	
			UtilsTools.setConvertViewWithShared(mContext, convertView, mCityList.get(position));
		
		}
		return convertView;
	}

	public String getTitle(int position) {
		return mCityList.get(position);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			View m=(View)view.getParent().getParent().getParent();
			Animation mAnim_out = AnimationUtils.loadAnimation(mContext, R.anim.more_detail_weather_out);
			mAnim_out.setInterpolator(new LinearInterpolator());
			Animation mAnim_in = AnimationUtils.loadAnimation(mContext, R.anim.more_detail_weather_in);
			mAnim_in.setInterpolator(new LinearInterpolator());
			if(m.findViewById(R.id.fautureweather).getVisibility() == View.VISIBLE){
				m.findViewById(R.id.moretodaydetail).setVisibility(View.VISIBLE);
				m.findViewById(R.id.fautureweather).startAnimation(mAnim_out);
				m.findViewById(R.id.moretodaydetail).startAnimation(mAnim_in);
			    m.findViewById(R.id.fautureweather).setVisibility(View.GONE);
			    view.setBackgroundResource(R.drawable.moredetailbutton);
		}
			else{	
			m.findViewById(R.id.fautureweather).setVisibility(View.VISIBLE);	
			m.findViewById(R.id.moretodaydetail).startAnimation(mAnim_out);
			m.findViewById(R.id.fautureweather).startAnimation(mAnim_in);
			m.findViewById(R.id.moretodaydetail).setVisibility(View.GONE);
			view.setBackgroundResource(R.drawable.weather_style);		
			}
			
		}
	};

	
	

}
