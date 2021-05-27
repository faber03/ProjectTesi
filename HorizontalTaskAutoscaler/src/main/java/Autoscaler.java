import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import static javax.management.JMX.newMBeanProxy;

public class Autoscaler {

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void echo(String msg) {
        System.out.println(msg);
    }

    public static class ClientListener implements NotificationListener {
        public void handleNotification(Notification notification,
                                       Object handback) {
            echo("\nReceived notification:");
            echo("\tClassName: " + notification.getClass().getName());
            echo("\tSource: " + notification.getSource());
            echo("\tType: " + notification.getType());
            echo("\tMessage: " + notification.getMessage());
            if (notification instanceof AttributeChangeNotification) {
                AttributeChangeNotification acn =
                        (AttributeChangeNotification) notification;
                echo("\tAttributeName: " + acn.getAttributeName());
                echo("\tAttributeType: " + acn.getAttributeType());
                echo("\tNewValue: " + acn.getNewValue());
                echo("\tOldValue: " + acn.getOldValue());
            }
        }
    }

    public static void main(String[] args) throws Exception {

        String neo4jJmxHost = System.getenv("NEO4J_JMX_HOST");
        String neo4jJmxUsername = System.getenv("NEO4J_JMX_USERNAME");
        String neo4jJmxPassword = System.getenv("NEO4J_JMX_PASSWORD");

        echo("\nCreate an RMI connector client and " +
                "connect it to the RMI connector server");

        //  Provide credentials required by server for user authentication
        HashMap environment = new HashMap();
        String[]  credentials = new String[] { neo4jJmxUsername != null ? neo4jJmxUsername : "monitor",
                                               neo4jJmxPassword != null ? neo4jJmxPassword :"Neo4j"};
        environment.put (JMXConnector.CREDENTIALS, credentials);

        //JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + "compute3.unisannio.local:32766" + "/jmxrmi");
        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + (neo4jJmxHost != null ? neo4jJmxHost : "localhost:3637") + "/jmxrmi");
        //new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");

        JMXConnector jmxc = JMXConnectorFactory.connect(url, environment);

        // Get an MBeanServerConnection
        echo("\nGet an MBeanServerConnection");
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        // Get domains from MBeanServer
        echo("\nDomains:");
        String domains[] = mbsc.getDomains();
        Arrays.sort(domains);
        for (String domain : domains) {
            echo("\tDomain = " + domain);
        }

        // Get MBeanServer's default domain
        echo("\nMBeanServer default domain = " + mbsc.getDefaultDomain());

        // Get MBean count
        echo("\nMBean count = " + mbsc.getMBeanCount());

        // Query MBean names
        echo("\nQuery MBeanServer MBeans:");
        Set<ObjectName> names =
                new TreeSet<ObjectName>(mbsc.queryNames(null, null));
        for (ObjectName name : names) {
            echo("\tObjectName = " + name);
        }

        // Construct the ObjectName for the Hello MBean
        ObjectName mbeanName = new ObjectName("neo4j.metrics:name=neo4j.transaction.active");

        var mbeanProxy =
                newMBeanProxy(mbsc, mbeanName, TransactionActiveMBean.class, true);

        while(true) {
            sleep(2000);
            echo("\nTransactionActive = " + mbeanProxy.getNumber());
        }

    }
}
