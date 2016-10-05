
package org.zhaoqian.jaxb.adpater;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ISO8601DateTimeAdapter extends XmlAdapter<String, Date>
{

	private static final String pattern = "yyyy-MM-dd HH:mm:ss XXX";

	@Override
	public String marshal(Date value) throws Exception
	{

		if ( value == null )
		{
			return null;
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

		return dateFormat.format(value);
	}

	@Override
	public Date unmarshal(String value) throws Exception
	{

		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

		return dateFormat.parse(value);
	}
}
