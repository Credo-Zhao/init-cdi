
package org.zhaoqian.jaxb;

import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.deltaspike.core.api.exclude.Exclude;

@Exclude
public final class MarshalUtil {

	public static String marshal(final Object jaxbElement) throws JAXBException {

		JAXBContext context = JAXBCache.instance().getJAXBContext(jaxbElement.getClass());
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

		StringWriter writer = new StringWriter();
		m.marshal(jaxbElement, writer);
		return writer.toString();
	}

	public static Object unmarshal(final InputStream input, final Class<?> clazz) throws JAXBException {

		JAXBContext context = JAXBCache.instance().getJAXBContext(clazz);
		Unmarshaller m = context.createUnmarshaller();
		return m.unmarshal(input);
	}
}
