
package org.zhaoqian.jaxb.adpater;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class SimpleDateAdapter extends XmlAdapter<String, Date> {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public String marshal(Date value) {

		if ( value == null )
		{
			return null;
		}

		return dateFormat.format(value);
	}

	@Override
	public Date unmarshal(String value) {

		return javax.xml.bind.DatatypeConverter.parseDate(value).getTime();
	}
}
