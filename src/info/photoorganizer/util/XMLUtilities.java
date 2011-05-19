package info.photoorganizer.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    private static Transformer _transformer = null;
    private static DocumentBuilder _documentBuilder = null;
    static
    {
        try
        {
            _documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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

    public static Document documentFromString(String str)
    {
        Document doc = null;
        if (null != _documentBuilder && str != null)
        {
            doc = documentFromReader(new StringReader(str));
        }
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
}
