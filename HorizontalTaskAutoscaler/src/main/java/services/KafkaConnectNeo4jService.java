package services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.ConnectorConfiguration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class KafkaConnectNeo4jService implements IKafkaConnectNeo4jService {

    private final String KAFKA_CONNECT_HOSTNAME = "connect-neo4j-tesi-delucia.router.default.svc.cluster.local";
    private final String CONNECTOR_NAME = "neo4j-sink";

    private String kafkaConnectHostname;
    private String connectorName;

    public KafkaConnectNeo4jService(String kafkaConnectHostname, String connectorName)
    {
        this.kafkaConnectHostname = kafkaConnectHostname != null ? kafkaConnectHostname : KAFKA_CONNECT_HOSTNAME;
        this.connectorName = connectorName != null ? connectorName : CONNECTOR_NAME;
    }

    public ConnectorConfiguration getConnectorConfiguration() throws Exception {
        URL urlForGetRequest = new URL("http://" + this.kafkaConnectHostname + "/connectors/" + this.connectorName);
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        int responseCode = conection.getResponseCode();

        ConnectorConfiguration result = null;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            var response = readResponse(conection.getInputStream());
            var objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            result = objectMapper.readValue(response, ConnectorConfiguration.class);
        } else {
            var message = readResponse(conection.getErrorStream());
            throw new Exception(message);
        }
        return result;
    }

    public void putConnectorConfiguration(ConnectorConfiguration configuration) throws Exception {

        URL obj = new URL("http://" + this.kafkaConnectHostname + "/connectors/" + connectorName + "/config");
        HttpURLConnection putConnection = (HttpURLConnection) obj.openConnection();
        putConnection.setRequestMethod("PUT");
        putConnection.setRequestProperty("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();
        String PUT_PARAMS = mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(configuration.config);

        putConnection.setDoOutput(true);
        OutputStream os = putConnection.getOutputStream();
        os.write(PUT_PARAMS.getBytes());
        os.flush();
        os.close();

        int responseCode = putConnection.getResponseCode();
        if(responseCode != HttpURLConnection.HTTP_OK)
        {
            var message = readResponse(putConnection.getErrorStream());
            throw new Exception(message);
        }
    }

    private String readResponse(InputStream inputStream) throws IOException {
        String readLine = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream));
        StringBuffer response = new StringBuffer();
        while ((readLine = in .readLine()) != null) {
            response.append(readLine);
        } in .close();
        return response.toString();
    }


}
