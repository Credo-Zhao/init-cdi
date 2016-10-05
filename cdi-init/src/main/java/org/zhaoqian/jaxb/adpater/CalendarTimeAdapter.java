
package org.zhaoqian.jaxb.adpater;

import java.util.Calendar;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CalendarTimeAdapter extends XmlAdapter<String, Calendar> {

	@Override
	public String marshal(Calendar value) throws Exception {

		if ( value == null )
		{
			return null;
		}
		return javax.xml.bind.DatatypeConverter.printTime(value);
	}

	@Override
	public Calendar unmarshal(String value) throws Exception {

		return javax.xml.bind.DatatypeConverter.parseTime(value);
	}
}
