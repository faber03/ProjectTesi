package services;

import javax.management.MalformedObjectNameException;

public interface INeo4jJmxService {
    long getJvmOldGenerationValue()  throws MalformedObjectNameException;
}
