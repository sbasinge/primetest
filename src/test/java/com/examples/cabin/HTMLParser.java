package com.examples.cabin;

import java.io.InputStream;
import java.net.URL;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class HTMLParser {
	static final String request = "http://www.hockinghillsrentals.com/index.htm";

    public static void main(String[] argv) throws Exception {
        DOMParser parser = new DOMParser();
		URL url = new URL(request);
		InputStream stream = url.openStream();
		InputSource source = new InputSource();
		source.setByteStream(stream);
		parser.parse(source);
        print(parser.getDocument(), "");
    }
    public static void print(Node node, String indent) {
        System.out.println(indent+node.getNodeName()+", "+node.getNodeType()+", "+node.getNodeValue()+", "+HTMLParser.printAttrs(node.getAttributes(),indent));
        Node child = node.getFirstChild();
        while (child != null) {
            print(child, indent+" ");
            child = child.getNextSibling();
        }
    }
    private static String printAttrs(NamedNodeMap map, String indent) {
    	String retVal = "";
    	if (map != null) {
    		for (int i=0; i < map.getLength(); i++) {
    			Node temp = map.item(i);
    			retVal+= temp.getNodeName()+", "+temp.getNodeValue()+"\n";
    		}
    	}
    	return retVal;
    }
}