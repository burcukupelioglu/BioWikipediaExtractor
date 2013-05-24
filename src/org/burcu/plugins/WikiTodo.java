/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.burcu.plugins;

/**
 * This class contains urls to be explored.
 * 
 * @author Burcu
 * @version beta 0.1
 */
public class WikiTodo {
    public String from;
    public String to;
    public int depth;

    /**
     * 
     * @param from currently explored url
     * @param to url contained by @from
     * @param depth current depth 
     */ 
    
    WikiTodo(String from, String to, int depth) {
        this.from = from;
        this.to = to;
        this.depth = depth;
    }
     
}
