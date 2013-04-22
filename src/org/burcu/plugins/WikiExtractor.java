/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.burcu.plugins;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.io.importer.api.EdgeDraft;
import org.gephi.io.importer.api.NodeDraft;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Burcu
 */
public class WikiExtractor {
    static int maxdepth = 3;
    
    static public void generate(ContainerLoader container) {
        ArrayList<WikiTodo> todo = new ArrayList<WikiTodo>();
        ArrayList<WikiLink> links = new ArrayList<WikiLink>();
        HashMap<String,NodeDraft> explored = new HashMap<String,NodeDraft>();
        
        WikiTodo init = new WikiTodo(null,"http://en.wikipedia.org/wiki/Aart_Kemink",0);
        todo.add(init);
        
        while(!todo.isEmpty()){
            WikiTodo t = todo.get(0);
            System.out.println(t.to + ":" + t.depth + " : " + (t.from==null?"":t.from));
            if(t.depth > maxdepth)
                break;
            
            if (explored.containsKey(t.to)){
                WikiLink a = new WikiLink(t.from,t.to);
                if(t.from != null && !WikiLink.searchLink(links, a)){
                    links.add(a);
                    EdgeDraft e = container.factory().newEdgeDraft();
                    e.setSource(explored.get(t.from));
                    e.setTarget(explored.get(t.to));
                    
                    container.addEdge(e);
                }
                todo.remove(0);
                continue;
            }
            
            Document doc;
            try {
                doc = Jsoup.connect(t.to).get();
            } catch (IOException ex) {
                todo.remove(0);
                //Logger.getLogger(WikiExtractor.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            
            if (doc.getElementById("persondata") == null) {
                todo.remove(0);
                continue;
            }
            
            NodeDraft n1 = container.factory().newNodeDraft();
            String labelstr = doc.getElementsByTag("title").html().toString();
            labelstr = labelstr.substring(0,labelstr.lastIndexOf("-"));
            n1.setLabel(labelstr);
            container.addNode(n1);
            
            explored.put(t.to, n1);
            Element maintext = doc.getElementById("mw-content-text");
            if (maintext == null){
                todo.remove(0);
                continue;
            }
            for(Element elnn:maintext.select("p:not(.navbox)")){
                for(Element el:elnn.select("a[href]")){
                    URL toUrl;
                    try {
                        toUrl = new URL(el.attr("abs:href"));
                    } catch (MalformedURLException ex) {
                        continue;
                    }
                    //String path = toUrl.getFile().substring(0, toUrl.getFile().lastIndexOf('/'));
                    String base = toUrl.getProtocol() + "://" + toUrl.getHost() + toUrl.getPath();

                    if(!base.startsWith("http://en.wikipedia.org/wiki/"))
                        continue;

                    WikiTodo wt = new WikiTodo(t.to,base,t.depth+1);
                    todo.add(wt);
                }
            }
            
            WikiLink a = new WikiLink(t.from, t.to);
            if (t.from != null && !WikiLink.searchLink(links, a)) {
                links.add(a);
                EdgeDraft e = container.factory().newEdgeDraft();
                e.setSource(explored.get(t.from));
                e.setTarget(explored.get(t.to));

                container.addEdge(e);
            }
            
            todo.remove(0);
        }
    }
}
