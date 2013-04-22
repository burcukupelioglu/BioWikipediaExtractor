/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.burcu.plugins;

/**
 *
 * @author Burcu
 */
public class WikiTodo {
    public String from;
    public String to;
    public int depth;

    WikiTodo(String from, String to, int depth) {
        this.from = from;
        this.to = to;
        this.depth = depth;
    }
     
}
