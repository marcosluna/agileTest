/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author marcos
 */
public class XMLAnalizer {

    private static String CHARSET = "UTF-8";
    private static Logger LOGGER = LoggerFactory.getLogger(XMLAnalizer.class);
    
    private Map attributes = null;

    public static void main(String[] args) {
        String targetElement = "make-everything-ok-button";//args[0];
        String baseURL = "https://agileengine.bitbucket.io/beKIvpUlPMtzhfAy/samples/sample-0-origin.html";//args[1]);
        String compareURL = "https://agileengine.bitbucket.io/beKIvpUlPMtzhfAy/samples/sample-1-evil-gemini.html";//args[2]);
        

        new XMLAnalizer(targetElement, baseURL, compareURL);

    }
    
    public XMLAnalizer(String target, String originalURL, String compareURL) {
        Optional<Element> element = findElementById(openURL(originalURL),target);
        if (element.isPresent()) {
            Elements list = element.get().parents();
        }
        
        Document dtarget = openURL(compareURL);

        getAttributes(element);
        showPath(element);
        
        //findElementInTarget(dtarget);

    }
    
    
    private void showPath(Optional<Element> ele) {
        if (ele.isPresent()) {
            String classtxt = ele.get().className().isBlank()?"":" class=\""+ ele.get().className()+ "\"";
            String idtxt = ele.get().id().isBlank()?"": "id=\""+ ele.get().id()+ "\"";
            System.out.println("Element <"+ele.get().normalName()+classtxt+idtxt+">");
            Elements parents_list = ele.get().parents();
            for (Element parent: parents_list) {
                classtxt = parent.className().isBlank()?"": " class=\""+ parent.className()+ "\"";
                idtxt = parent.id().isBlank()?"":" id=\""+ parent.id()+ "\"";
                System.out.println("has parent <"+parent.normalName()+classtxt+idtxt+">");
            }
        }
    }


    private void getAttributes(Optional<Element> ele) {
        attributes = new HashMap<String, String>();
        
        Optional<String> smap = 
        ele.map(attrs -> attrs.attributes().asList().stream().map(
                attr ->  attr.getKey() + " = " + attr.getValue()
            ).collect(Collectors.joining(" ,"))
        );
        System.out.println(smap);
        
        ele.get().attributes().asList().forEach(attribute -> attributes.put(attribute.getKey(), attribute.getValue()));
        
    }

    private Document openURL(String resource) {
       try {
       return Jsoup.connect(resource).get();
       } catch (IOException io) {
           return null;
       }
    }
    

    private Optional<Element> findElementById(Document doc, String targetElementId) {
        System.out.println("target is {"+targetElementId+"}");
        return Optional.of(doc.getElementById(targetElementId));

    }

}
