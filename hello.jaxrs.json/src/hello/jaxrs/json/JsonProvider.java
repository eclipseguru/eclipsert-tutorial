package hello.jaxrs.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * A provider that generates JSON using EclipseLink MOXy.
 * <p>
 * Based on
 * <em><a href="http://blog.bdoughan.com/2012/03/moxy-as-your-jax-rs-json-provider.html">MOXy as Your JAX-RS JSON Provider - Server Side</a></em>
 * blog.
 * </p>
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {

	@Context
	protected Providers providers;

	private Class<?> getDomainClass(final Type genericType) {
		if (genericType instanceof Class) {
			return (Class<?>) genericType;
		} else if (genericType instanceof ParameterizedType) {
			return (Class<?>) ((ParameterizedType) genericType).getActualTypeArguments()[0];
		} else {
			return null;
		}
	}

	private JAXBContext getJAXBContext(final Class<?> type, final MediaType mediaType) throws JAXBException {
		final ContextResolver<JAXBContext> resolver = providers.getContextResolver(JAXBContext.class, mediaType);
		JAXBContext jaxbContext;
		if ((null == resolver) || (null == (jaxbContext = resolver.getContext(type)))) {
			return JAXBContext.newInstance(type);
		} else {
			return jaxbContext;
		}
	}

	@Override
	public long getSize(final Object t, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isReadable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return true;
	}

	@Override
	public boolean isWriteable(final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType) {
		return true;
	}

	@Override
	public Object readFrom(final Class<Object> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, String> httpHeaders, final InputStream entityStream) throws IOException, WebApplicationException {
		try {
			final Class<?> domainClass = getDomainClass(genericType);
			final Unmarshaller u = getJAXBContext(domainClass, mediaType).createUnmarshaller();
			u.setProperty("eclipselink.media-type", mediaType.toString());
			u.setProperty("eclipselink.json.include-root", false);
			return u.unmarshal(new StreamSource(entityStream), domainClass).getValue();
		} catch (final JAXBException jaxbException) {
			throw new WebApplicationException(jaxbException);
		}
	}

	@Override
	public void writeTo(final Object object, final Class<?> type, final Type genericType, final Annotation[] annotations, final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream) throws IOException, WebApplicationException {
		try {
			final Class<?> domainClass = getDomainClass(genericType);
			final Marshaller m = getJAXBContext(domainClass, mediaType).createMarshaller();
			m.setProperty("eclipselink.media-type", mediaType.toString());
			m.setProperty("eclipselink.json.include-root", false);
			m.marshal(object, entityStream);
		} catch (final JAXBException jaxbException) {
			throw new WebApplicationException(jaxbException);
		}
	}
}