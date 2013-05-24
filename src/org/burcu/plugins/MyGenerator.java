package org.burcu.plugins;

import org.gephi.io.generator.spi.*;
import org.gephi.io.importer.api.ContainerLoader;
import org.gephi.utils.progress.ProgressTicket;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = Generator.class)
public class MyGenerator implements Generator {
    protected ProgressTicket progress;
    protected boolean cancel = false;
    
    
    static String urls;
    static int depth;
    
    @Override
    public void generate(ContainerLoader cl) {
        String urls_filtered[] = urls.split(",");
        WikiExtractor.generate(cl,urls_filtered,depth);
    }

    @Override
    public String getName() {
        return "Bio Wikipedia Extractor"; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GeneratorUI getUI() {
        WikiUI frame = new WikiUI();
        return frame;
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