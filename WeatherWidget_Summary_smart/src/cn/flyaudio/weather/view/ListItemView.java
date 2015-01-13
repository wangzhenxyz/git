package cn.flyaudio.weather.view;

import cn.flyaudio.Weather.R;
import cn.flyaudio.weather.activity.WeatherDetailsActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;
import cn.flyaudio.weather.data.YahooWeatherApplication;
/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:20:33
 */
public class ListItemView extends View {

	private String mWeek = null;
	private String mDate = null;
	private Bitmap mWeatherIcon = null;
	private String mHightTemp = null;
	private String mLowTemp = null;
	private String mWeather = null;
	private int textsize = 22;

	public ListItemView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public ListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (WeatherDetailsActivity.widthPixels == 1024)
			textsize = 30;
		else
			textsize = 22;
		if (mWeek == null)
			return;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextAlign(Align.CENTER);
		Paint paint3 = new Paint();
		paint3.setAntiAlias(true);
		paint3.setTextAlign(Align.LEFT);
		paint3.setColor(Color.parseColor("#FFFFFF"));
		paint3.setTextSize(textsize);

		Paint paintlowtemp = new Paint();
		paintlowtemp.setAntiAlias(true);
		paintlowtemp.setColor(Color.parseColor("#FFFFFF"));
		paintlowtemp.setTextSize(textsize);
		paintlowtemp.setTextAlign(Align.RIGHT);

		Paint paint1 = new Paint();
		paint1.setAntiAlias(true);
		paint1.setColor(Color.parseColor("#03aefd"));
		paint1.setTextSize(textsize);
		paint1.setTextAlign(Align.CENTER);
		
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setColor(Color.parseColor("#ff4e00"));
		paint2.setTextSize(textsize);
		paint2.setTextAlign(Align.CENTER);
		Paint paint4 = new Paint();
		paint4.setAntiAlias(true);
		paint4.setColor(Color.parseColor("#FFFFFF"));
		paint4.setTextSize(textsize);
		paint4.setTextAlign(Align.CENTER);
		Paint paint6 = new Paint();
		paint6.setAntiAlias(true);
		paint6.setTextAlign(Align.CENTER);
		paint6.setColor(Color.parseColor("#FFFFFF"));
		paint6.setTextSize(textsize);

		Paint paint5 = new Paint();
		paint5.setAntiAlias(true);
		paint5.setColor(Color.parseColor("#F9F900"));
		paint5.setTextSize(textsize);
		paint5.setTextAlign(Align.CENTER);
		
		if (mWeek.equals(getResources().getString(R.string.Saturday)))
			paint = paint1;
		else if (mWeek.equals(getResources().getString(R.string.Sunday)))
			paint = paint2;
		else if (mWeek.equals(getResources().getString(R.string.Today)))
			paint = paint5;
		else
			paint = paint4;

		canvas.drawText(mWeek, 80, 100, paint);
		canvas.drawText(mDate, 80, 50, paint4);
		canvas.drawBitmap(mWeatherIcon, 165, 23, paint);
		canvas.drawText(mLowTemp, 345, 50, paintlowtemp);
		canvas.drawText(mHightTemp, 350, 50, paint3);
		// canvas.drawText(mWeather, 300, 100, paint6);

			if (YahooWeatherApplication.isCNLanguage == true) {
				if (mWeather.length() <= 5) {
					canvas.drawText(mWeather, 335, 100, paint6);
				} else{
					FontMetrics fm = paint6.getFontMetrics();

					float baseline = fm.descent - fm.ascent;
					float x = 335;

					float y = baseline + 50;

					// 文本自动换行
					String[] texts = autoSplit(mWeather, paint6, 120);

					for (String text : texts) {
						canvas.drawText(text, x, y, paint6); // 坐标以控件左上角为原点
						y += baseline + fm.leading; // 添加字体行间距		
				}
				}
		
		} else {
			if (mWeather.length() <= 12) {
				canvas.drawText(mWeather, 335, 100, paint4);
			} else {
				paint6.setTextSize(21.8f);
				FontMetrics fm = paint6.getFontMetrics();
				float baseline = fm.descent - fm.ascent;
				float x = 335;
				float y = baseline + 60;
				// 文本自动换行
				String[] texts = autoSplit(mWeather, paint6, 150);
				
				for (String text : texts) {
					canvas.drawText(text, x, y, paint6); // 坐标以控件左上角为原点
					y += baseline ; // 添加字体行间距
				}
			}
		}

	}


	public void setInfo(String week, String date, String lowTemp,
			String highttemp, Bitmap weatherIcon, String weather) {
		mWeek = week;
		mDate = date;
		mHightTemp = highttemp;
		mLowTemp = lowTemp;
		mWeatherIcon = weatherIcon;
		mWeather = weather;
		invalidate();
	}

	private String[] autoSplit(String content, Paint p, float width) {
		int length = content.length();
		float textWidth = p.measureText(content);
		if (textWidth <= width) {
			return new String[] { content };
		}

		int start = 0, end = 1, i = 0;
		int lines = (int) Math.ceil(textWidth / width); // 计算行数
		String[] lineTexts = new String[lines];
		while (start < length) {
			if (p.measureText(content, start, end) > width) { // 文本宽度超出控件宽度时
				lineTexts[i++] = (String) content.subSequence(start, end);
				start = end;
			}
			if (end == length) { // 不足一行的文本
				lineTexts[i] = (String) content.subSequence(start, end);
				break;
			}
			end += 1;
		}
		return lineTexts;
	}

}
