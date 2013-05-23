/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.burcu.plugins;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    static int maxdepth = 0;//SLIDER
    
    static public void generate(ContainerLoader container,String urls[],int indepth) {
        maxdepth = indepth;
        
        
        ArrayList<WikiTodo> todo = new ArrayList<WikiTodo>();
        ArrayList<WikiLink> links = new ArrayList<WikiLink>();
        HashMap<String,NodeDraft> explored = new HashMap<String,NodeDraft>();
        List<String> explored_nonbio = new ArrayList<String>();
        
        for(String url : urls){
            WikiTodo init = new WikiTodo(null,url,0);//TEXTBOX
            todo.add(init);
        }
        
        while(!todo.isEmpty()){
            WikiTodo t = todo.get(0);
            System.out.println(t.to + ":" + t.depth + " : " + (t.from==null?"":t.from));
            if(t.depth > maxdepth)
                break;
            
            if (explored.containsKey(t.to)){
                WikiLink a = new WikiLink(t.from,t.to);
                if(t.from != null){// && !WikiLink.searchLink(links, a)){
                    
                    links.add(a);
                    EdgeDraft e = container.factory().newEdgeDraft();
                    e.setSource(explored.get(t.from));
                    e.setTarget(explored.get(t.to));
                    e.setWeight(1.0f);
                    container.addEdge(e);
                }
                todo.remove(0);
                continue;
            }
            
            if(explored_nonbio.contains(t.to)){
                todo.remove(0);
                continue;
            }
            
            Document doc;
            try {
                doc = Jsoup.connect(t.to).get();
                doc.select("div[style*=display:none]").remove();
                doc.select("span[style*=display:none]").remove();
            } catch (IOException ex){
                todo.remove(0);
                //Logger.getLogger(WikiExtractor.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            
            if (doc.getElementById("persondata") == null) {
                explored_nonbio.add(t.to);
                todo.remove(0);
                continue;
            }
            
            NodeDraft n1 = container.factory().newNodeDraft();
            String labelstr = doc.getElementsByTag("title").html().toString();
            labelstr = labelstr.substring(0,labelstr.lastIndexOf("-"));
            n1.setLabel(labelstr); 
            if(t.depth==0){
                n1.setColor(Color.CYAN);
            }else if(t.depth==maxdepth){
                n1.setColor(Color.ORANGE);
            }else{
                n1.setColor(Color.DARK_GRAY);
            }
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
                e.setWeight(1.0f);
                container.addEdge(e);
            }
            
            todo.remove(0);
        }
    }
}
