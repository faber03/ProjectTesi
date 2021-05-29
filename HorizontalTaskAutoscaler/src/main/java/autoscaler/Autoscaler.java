package autoscaler;
import models.ConnectorConfiguration;
import services.IKafkaConnectNeo4jService;
import services.INeo4jJmxService;
import services.KafkaConnectNeo4jService;
import services.Neo4jJmxService;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Autoscaler {

    private static IKafkaConnectNeo4jService _kafkaConnectService;
    private static INeo4jJmxService _neo4jJmxService;
    private static ConnectorConfiguration connectorConfiguration;
    private static int currentNumberOfTasks;
    private static AutoscalerConfiguration config;
    private static AutoscalerLogger logger;
    private static Instant nextScalingTime = Instant.now();

    public static void main(String[] args) throws Exception {

        config = new AutoscalerConfiguration();

        logger = new AutoscalerLogger("C:\\Users\\agost\\Desktop\\logTest\\log.txt");
        //logger = new AutoscalerLogger(config.logPath);

        _kafkaConnectService = new KafkaConnectNeo4jService(config.kafkaConnectHost, config.kafkaConnectorName);
        _neo4jJmxService = new Neo4jJmxService(config.neo4jJmxHost, config.neo4jJmxUsername, config.neo4jJmxPassword);

        connectorConfiguration = _kafkaConnectService.getConnectorConfiguration();
        currentNumberOfTasks = Integer.parseInt(connectorConfiguration.config.get("tasks.max"));

        echo("Create an JMX connection to Neo4j");

        echo("running...");

        var endTime = (Instant.now()).plusSeconds(config.lifetimeSec);
        while(config.lifetimeSec == -1 ? true : (Instant.now()).isAfter(endTime)) {

            var oldGen = _neo4jJmxService.getJvmOldGenerationValue();
            logger.log(oldGen, config.neo4jOldGenMaxValue, config.neo4jOldGenMinValue, currentNumberOfTasks, config.scalingMax, config.scalingMin);

            echo("current old generation is " + oldGen + " B");

            if((Instant.now()).isAfter(nextScalingTime)) {
                if (oldGen > config.neo4jOldGenMaxValue) {
                    echo("old generation greather than " + config.neo4jOldGenMaxValue + " B, scaling down of " + config.scalingStep + "...");
                    scaleDown();
                } else if (oldGen < config.neo4jOldGenMinValue) {
                    echo("old generation lower than " + config.neo4jOldGenMinValue + " B, scaling up of " + config.scalingStep + "...");
                    scaleUp();
                } else
                    nextScalingTime = (Instant.now()).plusMillis(config.metricReadingDelayMsec);
            }

            sleep(config.metricReadingDelayMsec);
        }
    }

    private static void scaleDown() throws Exception {
        var nexNumberOfTasks = currentNumberOfTasks - config.scalingStep;
        nexNumberOfTasks = nexNumberOfTasks > config.scalingMin ? nexNumberOfTasks : config.scalingMin;

        if(nexNumberOfTasks <  currentNumberOfTasks) {
            scale(nexNumberOfTasks);
            nextScalingTime = (Instant.now()).plusMillis(config.stabilizationDelayMsec);
        }
        else {
            nextScalingTime = (Instant.now()).plusMillis(config.metricReadingDelayMsec);
        }
    }

    private static void scaleUp() throws Exception {
        var nexNumberOfTasks = currentNumberOfTasks + config.scalingStep;
        nexNumberOfTasks = nexNumberOfTasks < config.scalingMax ? nexNumberOfTasks : config.scalingMax;

        if(nexNumberOfTasks >  currentNumberOfTasks) {
            scale(nexNumberOfTasks);
            nextScalingTime = (Instant.now()).plusMillis(config.stabilizationDelayMsec);
        }
        else {
            nextScalingTime = (Instant.now()).plusMillis(config.metricReadingDelayMsec);
        }
    }

    private static void scale(int numberOfTasks) throws Exception {
        connectorConfiguration.config.replace("tasks.max", String.valueOf(numberOfTasks));
        _kafkaConnectService.putConnectorConfiguration(connectorConfiguration);
        currentNumberOfTasks = numberOfTasks;
        echo("now the number of tasks is " + currentNumberOfTasks + ", waiting " + config.stabilizationDelayMsec + " ms for stabilization before next scaling...");
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void echo(String msg) {
        System.out.println("\n" + "["+ java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + "] "+ msg);
    }
//    public static void main(String[] args) throws Exception {
//
//        String neo4jJmxHost = System.getenv("NEO4J_JMX_HOST");
//        String neo4jJmxUsername = System.getenv("NEO4J_JMX_USERNAME");
//        String neo4jJmxPassword = System.getenv("NEO4J_JMX_PASSWORD");
//
//        echo("\nCreate an RMI connector client and " +
//                "connect it to the RMI connector server");
//
//        //  Provide credentials required by server for user authentication
//        HashMap environment = new HashMap();
//        String[]  credentials = new String[] { neo4jJmxUsername != null ? neo4jJmxUsername : "monitor",
//                                               neo4jJmxPassword != null ? neo4jJmxPassword :"Neo4j"};
//        environment.put (JMXConnector.CREDENTIALS, credentials);
//
//        //JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + "localhost:32766" + "/jmxrmi");
//        //JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + "compute3.unisannio.local:32766" + "/jmxrmi");
//        JMXServiceURL url =
//                new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + (neo4jJmxHost != null ? neo4jJmxHost : "localhost:3637") + "/jmxrmi");
//        //new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
//
//        JMXConnector jmxc = JMXConnectorFactory.connect(url, environment);
//
//        // Get an MBeanServerConnection
//        echo("\nGet an MBeanServerConnection");
//        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
//
//        // Get domains from MBeanServer
//        echo("\nDomains:");
//        String domains[] = mbsc.getDomains();
//        Arrays.sort(domains);
//        for (String domain : domains) {
//            echo("\tDomain = " + domain);
//        }
//
//        // Get MBeanServer's default domain
//        echo("\nMBeanServer default domain = " + mbsc.getDefaultDomain());
//
//        // Get MBean count
//        echo("\nMBean count = " + mbsc.getMBeanCount());
//
//        // Query MBean names
//        echo("\nQuery MBeanServer MBeans:");
//        Set<ObjectName> names =
//                new TreeSet<ObjectName>(mbsc.queryNames(null, null));
//        for (ObjectName name : names) {
//            echo("\tObjectName = " + name);
//        }
//
//        // Construct the ObjectName for the Hello MBean
//        ObjectName mbeanName = new ObjectName("java.lang:type=MemoryPool,name=PS Old Gen");
//
//        var info = mbsc.getMBeanInfo(mbeanName);
//
//        var mbeanProxy =
//                newMBeanProxy(mbsc, mbeanName, JvmOldGeneration.class, true);
//
//
//        //var temp = mbeanProxy.getUsage().get("used");
//
//        while(true) {
//            sleep(2000);
//            echo("\nOld generation = " + mbeanProxy.getUsage().get("used"));
//        }
//
//    }
}
