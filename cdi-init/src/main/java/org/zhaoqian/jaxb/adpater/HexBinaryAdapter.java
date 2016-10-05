
package org.zhaoqian.jaxb.adpater;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class HexBinaryAdapter extends XmlAdapter<String, byte[]> {

	@Override
	public String marshal(byte[] value) throws Exception {

		if ( value == null )
		{
			return null;
		}
		return javax.xml.bind.DatatypeConverter.printHexBinary(value);
	}

	@Override
	public byte[] unmarshal(String value) throws Exception {

		if ( value == null )
		{
			return null;
		}
		return javax.xml.bind.DatatypeConverter.parseHexBinary(value);
	}
}
