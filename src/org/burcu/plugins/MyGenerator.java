package org.burcu.plugins;

import org.gephi.io.generator.spi.*;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.utils.progress.ProgressTicket;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = Generator.class)
public class MyGenerator implements Generator {
    protected ProgressTicket progress;
    protected boolean cancel = false;
    
    @Override
    public void generate(ContainerLoader cl) {
        WikiExtractor.generate(cl);
    }

    @Override
    public String getName() {
        return "Wikipedia Extractor"; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GeneratorUI getUI() {
        return null; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cancel() {
        cancel = true;
        return true; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setProgressTicket(ProgressTicket pt) {
        this.progress = pt; //To change body of generated methods, choose Tools | Templates.
    }
    
}