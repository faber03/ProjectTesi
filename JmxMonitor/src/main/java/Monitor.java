/*
 * Client.java - JMX client that interacts with the JMX agent. It gets
 * attributes and performs operations on the Hello MBean and the QueueSampler
 * MXBean example. It also listens for Hello MBean notifications.
 */

import java.io.IOException;
import java.util.*;
import javax.management.AttributeChangeNotification;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import static javax.management.JMX.*;


public class Monitor {

    /**
     * Inner class that will handle the notifications.
     */
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

    /* For simplicity, we declare "throws Exception".
       Real programs will usually want finer-grained exception handling. */
    public static void main(String[] args) throws Exception {
        // Create an RMI connector client and
        // connect it to the RMI connector server
        //
        String neo4jJmxHost = System.getenv("NEO4J_JMX_HOST");
        String neo4jJmxUsername = System.getenv("NEO4J_JMX_USERNAME");
        String neo4jJmxPassword = System.getenv("NEO4J_JMX_PASSWORD");

        echo("\nCreate an RMI connector client and " +
                "connect it to the RMI connector server");

        //  Provide credentials required by server for user authentication
        HashMap   environment = new HashMap();
        String[]  credentials = new String[] { neo4jJmxUsername != null ? neo4jJmxUsername : "monitor",
                                               neo4jJmxPassword != null ? neo4jJmxPassword :"password"};
        environment.put (JMXConnector.CREDENTIALS, credentials);

        JMXServiceURL url =
                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + (neo4jJmxHost != null ? neo4jJmxHost : "localhost:3637") + "/jmxrmi");
                //new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");

        JMXConnector jmxc = JMXConnectorFactory.connect(url, environment);

        // Create listener
        //
        ClientListener listener = new ClientListener();

        // Get an MBeanServerConnection
        //
        echo("\nGet an MBeanServerConnection");
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

        //waitForEnterPressed();

        // Get domains from MBeanServer
        //
        echo("\nDomains:");
        String domains[] = mbsc.getDomains();
        Arrays.sort(domains);
        for (String domain : domains) {
            echo("\tDomain = " + domain);
        }

        Thread.sleep(2000);
        //waitForEnterPressed();

        // Get MBeanServer's default domain
        //
        echo("\nMBeanServer default domain = " + mbsc.getDefaultDomain());

        // Get MBean count
        //
        echo("\nMBean count = " + mbsc.getMBeanCount());

        // Query MBean names
        //
        echo("\nQuery MBeanServer MBeans:");
        Set<ObjectName> names =
                new TreeSet<ObjectName>(mbsc.queryNames(null, null));
        for (ObjectName name : names) {
            echo("\tObjectName = " + name);
        }

        Thread.sleep(2000);
        //waitForEnterPressed();

        // ----------------------
        // Manage the Hello MBean
        // ----------------------

        echo("\n>>> Perform operations MBean <<<");

        // Construct the ObjectName for the Hello MBean
        //
        ObjectName mbeanName = new ObjectName("neo4j.metrics:name=neo4j.neo4j.transaction.active");

        // Create a dedicated proxy for the MBean instead of
        // going directly through the MBean server connection
        //
        var mbeanProxy =
                newMBeanProxy(mbsc, mbeanName, TransactionActiveMBean.class, true);

        while(true) {
            Thread.sleep(2000);
            echo("\nTransactionActive = " + mbeanProxy.getNumber());
        }
    }

    private static void echo(String msg) {
        System.out.println(msg);
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void waitForEnterPressed() {
        try {
            echo("\nPress <Enter> to continue...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
