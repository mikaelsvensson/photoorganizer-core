package info.photoorganizer.util;

import info.photoorganizer.database.DatabaseStorageException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtilities
{
    private static DocumentBuilder _documentBuilder = null;
    private static Transformer _transformer = null;
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    static
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            _documentBuilder = factory.newDocumentBuilder();
            _transformer = TransformerFactory.newInstance().newTransformer();
            // _transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        }
        catch (TransformerConfigurationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (TransformerFactoryConfigurationError e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ParserConfigurationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static Document createDocument(String rootElementName)
    {
        Document doc = _documentBuilder.newDocument();
        doc.appendChild(doc.createElement(rootElementName));
        return doc;
    }

    public static Document documentFromFile(File file)
    {
        Document doc = null;
        if (null != _documentBuilder && file != null)
        {
            try
            {
                doc = documentFromReader(new FileReader(file));
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return doc;
    }

    private static Document documentFromReader(Reader source)
    {
        Document doc = null;
        InputSource is = new InputSource(source);
        try
        {
            doc = _documentBuilder.parse(is);
        }
        catch (SAXException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return doc;
    }

    public static Document documentFromString(String str)
    {
        Document doc = null;
        if (null != _documentBuilder && str != null)
        {
            doc = documentFromReader(new StringReader(str));
        }
        return doc;
    }

    public static void documentToFile(Document doc, File file, Charset charset) throws IOException
    {
        StreamResult sr = new StreamResult(new OutputStreamWriter(new FileOutputStream(file), charset));
        DOMSource source = new DOMSource(doc);
        try
        {
            _transformer.transform(source, sr);
        }
        catch (TransformerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String documentToString(Document doc)
    {
        StreamResult sr = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        try
        {
            _transformer.transform(source, sr);
        }
        catch (TransformerException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sr.getWriter().toString();
    }
    
    public static boolean getBooleanAttribute(Element el, String attr)
    {
        return Boolean.parseBoolean(el.getAttribute(attr));
    }
    
    public static Double getDoubleAttribute(Element el, String attr, Double defaultValue) throws DatabaseStorageException
    {
        try
        {
            if (el.hasAttribute(attr))
            {
                return Double.valueOf(el.getAttribute(attr));
            }
            else
            {
                return defaultValue;
            }
        }
        catch (NumberFormatException e)
        {
            throw new DatabaseStorageException("Attribute value is not a valid double-precision value.", e);
        }
    }
    
    private static DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
    
    public static Date getDateAttribute(Element el, String attr, Date defaultValue) throws DatabaseStorageException
    {
        try
        {
            if (el.hasAttribute(attr))
            {
                return DatatypeConverter.parseDateTime(el.getAttribute(attr)).getTime();
            }
            else
            {
                return defaultValue;
            }
        }
        catch (IllegalArgumentException e)
        {
            throw new DatabaseStorageException("Attribute value is not a valid date.", e);
        }
    }
    
    public static Integer getIntegerAttribute(Element el, String attr, Integer defaultValue) throws DatabaseStorageException
    {
        try
        {
            if (el.hasAttribute(attr))
            {
                return Integer.valueOf(el.getAttribute(attr));
            }
            else
            {
                return defaultValue;
            }
        }
        catch (NumberFormatException e)
        {
            throw new DatabaseStorageException("Attribute value is not a valid integer value.", e);
        }
    }
    
    public static Long getLongAttribute(Element el, String attr, Long defaultValue) throws DatabaseStorageException
    {
        try
        {
            if (el.hasAttribute(attr))
            {
                return Long.valueOf(el.getAttribute(attr));
            }
            else
            {
                return defaultValue;
            }
        }
        catch (NumberFormatException e)
        {
            throw new DatabaseStorageException("Attribute value is not a valid long integer value.", e);
        }
    }
    
    public static Element getNamedChild(Element parent, String childElementName)
    {
        List<Element> children = getNamedChildren(parent, childElementName);
        if (children.size() == 1)
        {
            return children.get(0);
        }
        else
        {
            return null;
        }
    }
    
    public static List<Element> getNamedChildren(Element parent, String... childElementNames)
    {
        return getNamedChildren(null, parent, childElementNames);
    }
    public static List<Element> getNamedChildren(String namespaceURI, Element parent, String... childElementNames)
    {
        List<Element> res = new ArrayList<Element>();
        if (null != parent)
        {
            for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling())
            {
                if (child instanceof Element)
                {
                    for (String name : childElementNames)
                    {
                        if ((namespaceURI != null && name.equals(child.getLocalName()) && namespaceURI.equals(child.getNamespaceURI())) || 
                            (namespaceURI == null && name.equals(child.getNodeName())))
                        {
                            res.add((Element)child);
                            break;
                        }
                    }
                }
            }
        }
        return res;
    }
    
    /**
     * Wrapper for <code>getElementsByTagName</code> with the "quirk" that it
     * returns an array instead of a "live" NodeList (meaning that changes to
     * the DOM tree after returning the element list will not affect the element
     * list).
     * 
     * @param parent
     * @param decendantElementsNS
     * @param decentantElementsName
     * @return
     */
    public static List<Element> getNamedDecendants(Element parent, String decendantElementsNS, String... decentantElementsNames)
    {
        List<Element> res = new ArrayList<Element>();
        for (String decentantElementsName : decentantElementsNames)
        {
            NodeList elements = null;
            if (decendantElementsNS != null && decendantElementsNS.length() > 0)
            {
                elements = parent.getElementsByTagNameNS(decendantElementsNS, decentantElementsName);
            }
            else
            {
                elements = parent.getElementsByTagName(decentantElementsName);
            }
            
            for (int i=0; i < elements.getLength(); i++)
            {
                res.add((Element) elements.item(i));
            }
        }
        return res;
    }
    
    public static String getTextAttribute(Element el, String attr, String defaultValue)
    {
        if (el.hasAttribute(attr))
        {
            return el.getAttribute(attr);
        }
        else
        {
            return defaultValue;
        }
    }
    
    public static URI getURIAttribute(Element el, String attr, URI defaultValue)
    {
        if (el.hasAttribute(attr))
        {
            try
            {
                return new URI(el.getAttribute(attr));
            }
            catch (URISyntaxException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return defaultValue;
    }
    
//    public static UUID getUUIDAttribute(Element el, String attr)
//    {
//        try
//        {
//            return UUID.fromString(el.getAttribute(attr));
//        }
//        catch (IllegalArgumentException e)
//        {
//            return null;
//        }
//    }
//    
//    public static UUID[] getUUIDsAttribute(Element el, String attr)
//    {
//        String value = el.getAttribute(attr);
//        String[] ids = StringUtils.split(value, ' ');
//        UUID[] res = new UUID[ids.length];
//        int i=0;
//        for (String id : ids)
//        {
//            try
//            {
//                res[i++] = UUID.fromString(id);
//            }
//            catch (IllegalArgumentException e)
//            {
//            }
//        }
//        return res;
//    }
    
    public static void setBooleanAttribute(Element el, String attr, boolean value)
    {
        el.setAttribute(attr, Boolean.toString(value));
    }
    
    public static void setURIAttribute(Element el, String attr, URI uri)
    {
        el.setAttribute(attr, uri.toString());
    }
    
    public static void setTextAttribute(Element el, String attr, String value)
    {
        el.setAttribute(attr, value);
    }
    
    public static void setIntegerAttribute(Element el, String attr, Integer value)
    {
        if (null != value)
        {
            el.setAttribute(attr, value.toString());
        }
    }
    
    public static void setDoubleAttribute(Element el, String attr, Double value)
    {
        if (null != value)
        {
            el.setAttribute(attr, value.toString());
        }
    }
    
    public static void setDateAttribute(Element el, String attr, Date value)
    {
        if (null != value)
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(value);
            el.setAttribute(attr, DatatypeConverter.printDateTime(cal));
        }
    }
    
    public static void setLongAttribute(Element el, String attr, Long value)
    {
        if (null != value)
        {
            el.setAttribute(attr, value.toString());
        }
    }
    
//    public static void setUUIDAttribute(Element el, String attr, UUID value)
//    {
//        if (value != null)
//        {
//            el.setAttribute(attr, value.toString());
//        }
//        else
//        {
//            el.removeAttribute(attr);
//        }
//    }
//    
//    public static void setUUIDsAttribute(Element el, String attr, UUID[] values)
//    {
//        if (values != null && values.length > 0)
//        {
//            el.setAttribute(attr, StringUtils.join(values, ' '));
//        }
//        else
//        {
//            el.removeAttribute(attr);
//        }
//    }
    
    public static void appendChildren(Element el, Iterable<Element> children)
    {
        for (Element child : children)
        {
            el.appendChild(child);
        }
    }

    public static Iterable<Element> getChildElements(Element el)
    {
        LinkedList<Element> res = new LinkedList<Element>();
        NodeList childNodes = el.getChildNodes();
        for (int i=0; i < childNodes.getLength(); i++)
        {
            Node item = childNodes.item(i);
            if (item.getNodeType() == Node.ELEMENT_NODE)
            {
                res.add((Element) item);
            }
        }
        return res;
    }
}
