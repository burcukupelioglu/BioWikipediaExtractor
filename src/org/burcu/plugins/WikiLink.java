/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.burcu.plugins;

import java.util.ArrayList;

/**
 *
 * @author Burcu
 */
public class WikiLink {
    public String from;
    public String to;

    WikiLink(String from, String to) {
        this.from = from;
        this.to = to;//To change body of generated methods, choose Tools | Templates.
    }
    
    static public boolean searchLink(ArrayList<WikiLink> links,WikiLink l){
        if (l.from == null || l.to == null)
            return false;
        
        for(WikiLink w:links){
            if((w.to.equals(l.to) && w.from.equals(l.from)) || (w.to.equals(l.from) && w.from.equals(l.to)))
                return true;
        }
        return false;
    }
}
