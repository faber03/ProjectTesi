package autoscaler;

public class AutoscalerConfiguration {

    private final String NEO4j_JMX_HOST = "localhost:32766";
    private final String NEO4j_JMX_USERNAME = "monitor";
    private final String NEO4j_JMX_PASSWORD = "Neo4j";
    private final String KAFKA_CONNECT_HOSTNAME = "connect-neo4j-tesi-delucia.router.default.svc.cluster.local";
    private final String KAFKA_CONNECTOR_NAME = "neo4j-sink";
    private final long NEO4J_OLDGEN_MAX = 110000000;
    private final long NEO4J_OLDGEN_MIN = 60000000;
    private final long STABILIZATION_DELAY_MSEC = 30000;
    private final long METRIC_READING_DELAY_MSEC = 5000;
    private final int SCALING_STEP = 1;
    private final int SCALING_MAX = 7;
    private final int SCALING_MIN = 1;
    private final int LIFETIME_SEC = 3200;
    private final String LOG_PATH = "log.txt";

    public String neo4jJmxHost;
    public String neo4jJmxUsername;
    public String neo4jJmxPassword;
    public long neo4jOldGenMaxValue;
    public long neo4jOldGenMinValue;
    public long stabilizationDelayMsec;
    public long metricReadingDelayMsec;
    public int scalingStep;
    public int scalingMax;
    public int scalingMin;
    public long lifetimeSec;
    public String kafkaConnectHost;
    public String kafkaConnectorName;
    public String logPath;

    public AutoscalerConfiguration()
    {
        this.neo4jJmxHost = System.getenv("NEO4J_JMX_HOST");
        if(this.neo4jJmxHost == null)
            this.neo4jJmxHost =NEO4j_JMX_HOST;

        this.neo4jJmxUsername = System.getenv("NEO4J_JMX_USERNAME");
        if(this.neo4jJmxUsername == null)
            this.neo4jJmxUsername =NEO4j_JMX_USERNAME;

        this.neo4jJmxPassword = System.getenv("NEO4J_JMX_PASSWORD");
        if(this.neo4jJmxPassword == null)
            this.neo4jJmxPassword =NEO4j_JMX_PASSWORD;

        this.kafkaConnectHost = System.getenv("KAFKA_CONNECT_HOST");
        if(this.kafkaConnectHost == null)
            this.kafkaConnectHost =KAFKA_CONNECT_HOSTNAME;

        this.kafkaConnectorName = System.getenv("KAFKA_CONNECTOR_NAME");
        if(this.kafkaConnectorName == null)
            this.kafkaConnectorName =KAFKA_CONNECTOR_NAME;

        this.logPath = System.getenv("LOG_PATH");
        if(this.logPath == null)
            this.logPath =LOG_PATH;

        var oldGenMax = System.getenv("NEO4J_OLDGEN_MAX");
        try
        {
            this.neo4jOldGenMaxValue = oldGenMax != null ? Long.parseLong(oldGenMax) : NEO4J_OLDGEN_MAX;
        }
        catch (NumberFormatException e)
        {
            this.neo4jOldGenMaxValue = NEO4J_OLDGEN_MAX;
        }

        var oldGenMin = System.getenv("NEO4J_OLDGEN_MIN");
        try
        {
            this.neo4jOldGenMinValue = oldGenMin != null ? Long.parseLong(oldGenMin) : NEO4J_OLDGEN_MIN;
        }
        catch (NumberFormatException e)
        {
            this.neo4jOldGenMinValue = NEO4J_OLDGEN_MIN;
        }

        var stabDelay = System.getenv("STABILIZATION_DELAY_MSEC");
        try
        {
            this.stabilizationDelayMsec = stabDelay != null ? Long.parseLong(stabDelay) : STABILIZATION_DELAY_MSEC;
        }
        catch (NumberFormatException e)
        {
            this.stabilizationDelayMsec = STABILIZATION_DELAY_MSEC;
        }

        var readDelay = System.getenv("METRIC_READING_DELAY_MSEC");
        try
        {
            this.metricReadingDelayMsec = readDelay != null ? Long.parseLong(readDelay) : METRIC_READING_DELAY_MSEC;
        }
        catch (NumberFormatException e)
        {
            this.metricReadingDelayMsec = METRIC_READING_DELAY_MSEC;
        }

        var scalStep = System.getenv("SCALING_STEP");
        try
        {
            this.scalingStep = scalStep != null ? Integer.parseInt(scalStep) : SCALING_STEP;
        }
        catch (NumberFormatException e)
        {
            this.scalingStep = SCALING_STEP;
        }

        var scalMax = System.getenv("SCALING_MAX");
        try
        {
            this.scalingMax = scalMax != null ? Integer.parseInt(scalMax) : SCALING_MAX;
        }
        catch (NumberFormatException e)
        {
            this.scalingMax = SCALING_MAX;
        }

        var scalMin = System.getenv("SCALING_MIN");
        try
        {
            this.scalingMin = scalMin != null ? Integer.parseInt(scalMin) : SCALING_MIN;
        }
        catch (NumberFormatException e)
        {
            this.scalingMin = SCALING_MIN;
        }

        var lifetime = System.getenv("LIFETIME_SEC");
        try
        {
            this.lifetimeSec = lifetime != null ? Integer.parseInt(lifetime) : LIFETIME_SEC;
        }
        catch (NumberFormatException e)
        {
            this.lifetimeSec = LIFETIME_SEC;
        }
    }
}
