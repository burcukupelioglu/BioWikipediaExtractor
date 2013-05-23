/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.burcu.plugins;

import javax.swing.JPanel;
import org.gephi.io.generator.spi.Generator;
import org.gephi.io.generator.spi.GeneratorUI;

/**
 *
 * @author Guest
 */
public class WikiUI implements GeneratorUI{
    private WikiPanel panel;
    
    @Override
    public JPanel getPanel() {
        if(panel == null){
            panel = new WikiPanel();
        }
        return panel;
    }

    @Override
    public void setup(Generator gnrtr) {
        if(panel == null){
            panel = new WikiPanel();
        }
    }

    @Override
    public void unsetup() {
       MyGenerator.depth = panel.getSliderVal();
       MyGenerator.urls = panel.getTextVal();
       panel = null;
    }
    
}
