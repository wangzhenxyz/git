package cn.flyaudio.weather.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cn.flyaudio.weather.objectInfo.FullWeatherInfo;

/**
 * @author:zengyuke
 * @company:flyaudio
 * @version:1.0
 * @createdDate:2014-5-5下午3:19:02
 */
public class SaxToolsForYahoo extends DefaultHandler {
	private FullWeatherInfo mForecastyweathers;

	private int i = 0;

	public SaxToolsForYahoo(FullWeatherInfo mForecastyweathers) {
		this.mForecastyweathers = mForecastyweathers;

	}

	@Override
	public void startDocument() throws SAXException {

	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (qName.equals("yweather:forecast")) {
			mForecastyweathers.yweathers[i]
					.setCode(attributes.getValue("code"));
			mForecastyweathers.yweathers[i]
					.setDate(attributes.getValue("date"));
			mForecastyweathers.yweathers[i].setDay(attributes.getValue("day"));
			mForecastyweathers.yweathers[i]
					.setHigh(attributes.getValue("high"));
			mForecastyweathers.yweathers[i].setLow(attributes.getValue("low"));
			mForecastyweathers.yweathers[i]
					.setText(attributes.getValue("text"));
			i++;
		}
		if (qName.equals("yweather:condition")) {
			mForecastyweathers.setCondition_code(attributes.getValue("code"));
			mForecastyweathers.setCondition_text(attributes.getValue("text"));
			mForecastyweathers.setCondition_date(attributes.getValue("date"));
			mForecastyweathers.setCondition_temp(attributes.getValue("temp"));
		}
		if (qName.equals("yweather:wind")) {
			mForecastyweathers.setWindchill(attributes.getValue("chill"));
			mForecastyweathers.setFeelslike(attributes.getValue("chill"));
			mForecastyweathers.setWinddirection(attributes
					.getValue("direction"));
			mForecastyweathers.setWindspeed(attributes.getValue("speed"));
		}
		if (qName.equals("yweather:atmosphere")) {

			mForecastyweathers.setHumidity(attributes.getValue("humidity"));
			mForecastyweathers.setVisibility(attributes.getValue("visibility"));
			mForecastyweathers.setPressure(attributes.getValue("pressure"));
		}
		if (qName.equals("yweather:astronomy")) {
			mForecastyweathers.setSunrise(attributes.getValue("sunrise"));
			mForecastyweathers.setSunset(attributes.getValue("sunset"));
		}

		
		mForecastyweathers.setDataflag(true);
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {

	}

}
