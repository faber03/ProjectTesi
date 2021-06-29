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
        while(config.lifetimeSec == -1 ? true : (Instant.now()).isBefore(endTime)) {

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

        echo("lifetime of " + config.lifetimeSec + " sec has expired, you need to manually restart...");
        sleepInfinity();
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

    private static void sleepInfinity() throws InterruptedException {
        while (true) {
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
