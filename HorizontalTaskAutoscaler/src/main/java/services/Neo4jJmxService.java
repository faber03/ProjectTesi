package services;

import services.mbeans.*;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.HashMap;

import static javax.management.JMX.newMBeanProxy;

public class Neo4jJmxService implements INeo4jJmxService{

    private final String NEO4j_JMX_USERNAME = "monitor";
    private final String NEO4j_JMX_PASSWORD = "Neo4j";
    //private final String NEO4j_JMX_HOST = "localhost:3637";
    private final String NEO4j_JMX_HOST = "localhost:32766";

    private MBeanServerConnection mbsc;
    private JvmOldGenerationMBean jvmOldGenMbeanProxy;

    public Neo4jJmxService(String neo4jJmxHost, String neo4jJmxUsername, String neo4jJmxPassword) throws IOException {
        HashMap environment = new HashMap();
        String[]  credentials = new String[] { neo4jJmxUsername != null ? neo4jJmxUsername : NEO4j_JMX_USERNAME, neo4jJmxPassword != null ? neo4jJmxPassword : NEO4j_JMX_PASSWORD};
        environment.put (JMXConnector.CREDENTIALS, credentials);
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + (neo4jJmxHost != null ? neo4jJmxHost : NEO4j_JMX_HOST) + "/jmxrmi");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, environment);
        this.mbsc = jmxc.getMBeanServerConnection();
    }

    public long getJvmOldGenerationValue() throws MalformedObjectNameException {
        ObjectName mbeanName = new ObjectName("java.lang:type=MemoryPool,name=PS Old Gen");
        if(jvmOldGenMbeanProxy == null)
            this.jvmOldGenMbeanProxy = newMBeanProxy(mbsc, mbeanName, JvmOldGenerationMBean.class, true);

        return (long)this.jvmOldGenMbeanProxy.getUsage().get("used");
    }

}
