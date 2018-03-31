package com.flowergarden.dao.json;

import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;

public abstract class JsonDao<T> {

    private Class<T> clazz;

    private final MappedNamespaceConvention conv;

    public JsonDao() {
        conv = new MappedNamespaceConvention(new Configuration());
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public JsonDao(Configuration config) {
        this.conv = new MappedNamespaceConvention(config);
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void save(T jsonObj, Writer writer) {
        try {
            XMLStreamWriter xmlStreamWriter = new MappedXMLStreamWriter(conv, writer);
            Marshaller marshaller = getJAXBContext().createMarshaller();
            marshaller.marshal(jsonObj, xmlStreamWriter);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public T get(Reader reader) {
        try {
            Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

    }

    private JAXBContext getJAXBContext() {
        try {
            System.out.println(clazz);
            return JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
