
package org.zhaoqian.jaxb.adpater;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class GenericDateTimeAdapter extends XmlAdapter<String, Date> {

	@Override
	public String marshal(Date value) {

		if ( value == null )
		{
			return null;
		}
		Calendar cal = new GregorianCalendar();
		cal.setTime(value);
		return javax.xml.bind.DatatypeConverter.printDateTime(cal);
	}

	@Override
	public Date unmarshal(String value) {

		return javax.xml.bind.DatatypeConverter.parseDate(value).getTime();
	}
}
