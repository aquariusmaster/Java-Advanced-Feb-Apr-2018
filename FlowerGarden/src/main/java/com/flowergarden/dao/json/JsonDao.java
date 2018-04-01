package com.flowergarden.dao.json;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamReader;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;

public abstract class JsonDao<T> {

    private final Class<T> clazz;
    private JAXBContext context;

    private final MappedNamespaceConvention conv;

    public JsonDao() {
        conv = new MappedNamespaceConvention(new Configuration());
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public JsonDao(Configuration config) {
        this.conv = new MappedNamespaceConvention(config);
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void writeJson(T jsonObj, Writer writer) {
        try {
            XMLStreamWriter xmlStreamWriter = new MappedXMLStreamWriter(conv, writer);
            Marshaller marshaller = getJAXBContext().createMarshaller();
            marshaller.marshal(jsonObj, xmlStreamWriter);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public T readJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            XMLStreamReader xmlStreamReader = new MappedXMLStreamReader(obj, conv);

            Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
            return (T) unmarshaller.unmarshal(xmlStreamReader);

        } catch (JAXBException | JSONException | XMLStreamException e) {
            throw new RuntimeException(e);
        }

    }

    public T readJson(Reader reader) {
        try {
            char[] arr = new char[8 * 1024];
            StringBuilder buffer = new StringBuilder();
            int numCharsRead;
            while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
                buffer.append(arr, 0, numCharsRead);
            }
            reader.close();
            String targetString = buffer.toString();
            return this.readJson(targetString);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private JAXBContext getJAXBContext() {
        try {
            if (context == null) {
                context = JAXBContext.newInstance(clazz);
            }
            return context;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
