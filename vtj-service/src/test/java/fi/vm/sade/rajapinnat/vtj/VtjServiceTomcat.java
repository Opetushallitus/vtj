package fi.vm.sade.rajapinnat.vtj;

import fi.vm.sade.integrationtest.tomcat.EmbeddedTomcat;
import fi.vm.sade.integrationtest.tomcat.SharedTomcat;
import fi.vm.sade.integrationtest.util.PortChecker;
import fi.vm.sade.integrationtest.util.ProjectRootFinder;
import org.apache.catalina.LifecycleException;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;

public class VtjServiceTomcat extends EmbeddedTomcat {
    static final String VTJ_MODULE_ROOT = ProjectRootFinder.findProjectRoot() + "/vtj/vtj-service";
    static final String VTJ_CONTEXT_PATH = "/vtj-service";
    static final int DEFAULT_PORT = 8081;
    static final int DEFAULT_AJP_PORT = 8006;

    public final static void main(String... args) throws ServletException, LifecycleException {
        new VtjServiceTomcat(Integer.parseInt(System.getProperty("vtj-service.port", String.valueOf(DEFAULT_PORT))),
                Integer.parseInt(System.getProperty("vtj-service.port.ajp", String.valueOf(DEFAULT_AJP_PORT)))
        ).start().await();
    }

    public VtjServiceTomcat(int port, int ajpPort) {
        super(port, ajpPort, VTJ_MODULE_ROOT, VTJ_CONTEXT_PATH);
    }

}