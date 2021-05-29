package services;

import models.ConnectorConfiguration;

public interface IKafkaConnectNeo4jService {
    ConnectorConfiguration getConnectorConfiguration() throws Exception;
    void putConnectorConfiguration(ConnectorConfiguration configuration) throws Exception;
}
