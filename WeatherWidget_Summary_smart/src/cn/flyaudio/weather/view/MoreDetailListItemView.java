package cn.flyaudio.weather.view;

import cn.flyaudio.weather.activity.WeatherDetailsActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:20:33
 */
public class MoreDetailListItemView extends View {
	
	private String mDetailname = null;
	private Bitmap mDetailIcon = null;
	private String mDetailvalue = null;
	
	private int textsize = 22;
	
	public MoreDetailListItemView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);	
	}

	public MoreDetailListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if(WeatherDetailsActivity.widthPixels == 1024)
			textsize = 30;
		else 
			textsize = 22;
		
		if(mDetailname == null)
			return;
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		Paint paint1 = new Paint();
		paint1.setAntiAlias(true);
		paint1.setColor(Color.parseColor("#03aefd"));
		paint1.setTextSize(textsize);	
		
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setColor(Color.parseColor("#ff4e00"));
		paint2.setTextSize(textsize);	
		
		Paint paint3 = new Paint();
		paint3.setAntiAlias(true);
		paint3.setColor(Color.parseColor("#6c6b6b"));
		paint3.setTextSize(textsize);	
		
		Paint paint4 = new Paint();
		paint4.setAntiAlias(true);
		paint4.setColor(Color.parseColor("#FFFFFF"));
		paint4.setTextSize(textsize);
		paint4.setTextAlign(Align.RIGHT);
		
		
		if(WeatherDetailsActivity.widthPixels == 1024){
			canvas.drawText(mDetailname, 50,50, paint3);
			canvas.drawBitmap(mDetailIcon,0,15, paint);
			canvas.drawText(mDetailvalue, 375,50, paint4);
		
			
			
		}else{
			canvas.drawText(mDetailname, 0,30, paint);
			canvas.drawText(mDetailvalue, 82,30, paint3);
			canvas.drawBitmap(mDetailIcon, 150,-5, paint);	
		}

	}
	
	public void setInfo(String detailname,String detailvalue,  Bitmap detailIcon){
		mDetailname  = detailname;
		mDetailvalue = detailvalue;
		mDetailIcon  = detailIcon;
		invalidate();
	}
}
