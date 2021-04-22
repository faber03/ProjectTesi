//import com.google.gson.Gson;
//import com.google.gson.stream.JsonReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.api.core.SimpleString;
import org.apache.activemq.artemis.api.core.client.ActiveMQClient;
import org.apache.activemq.artemis.api.core.client.ClientSession;
import org.apache.activemq.artemis.api.core.client.ClientSessionFactory;
import org.apache.activemq.artemis.api.core.client.ServerLocator;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class QueueGenerator {

    public static void main(String[] args) throws Exception {

//        HashMap<String, Object> connectionParams = new HashMap<>();
//        connectionParams.put(TransportConstants.HOST_PROP_NAME, "localhost");
//        connectionParams.put(TransportConstants.PORT_PROP_NAME, Integer.toString(61616));
//
//        ServerLocator locator = ActiveMQClient.createServerLocatorWithoutHA(new TransportConfiguration(
//                NettyConnectorFactory.class.getName(), connectionParams));

        ServerLocator locator = ActiveMQClient.createServerLocator("tcp://172.18.10.147:30340");
        ClientSessionFactory factory =  locator.createSessionFactory();
        ClientSession session = factory.createSession(true, true);
        session.start();

        //var areaNames = new String[]{"Lyon", "Venissieux", "Vienne", "Meyzieu", "Caluire-et-Cuire", "Saint-Genis-Laval", "Saint-Quentin-Fallavier", "Genas", "Ecully", "Dardilly", "Sainte-Foy-les-Lyon", "Villefontaine", "Chassieu", "Tassin-la-Demi-Lune", "Charvieu-Chavagneux", "Miribel", "Saint-Laurent-de-Mure", "Oullins", "Montluel", "Heyrieux"};
        var areaNames = new String[]{"Lyon"};
        var NORTHBOUND_SUFFIX = "-Northbound";
        for(var areaName: areaNames)
            session.createQueue(new SimpleString(areaName + NORTHBOUND_SUFFIX), RoutingType.ANYCAST, new SimpleString(areaName + NORTHBOUND_SUFFIX), true);

//        ClientProducer producer = session.createProducer("Lyon-Northbound");
//        ClientMessage message = session.createMessage(true);
//        message.getBodyBuffer().writeString("{\"linkId\":-12500009347401,\"timestamp\":1615633113097,\"avgTravelTime\":4.621090871637517,\"sdTravelTime\":0.0,\"numVehicles\":1,\"aggPeriod\":{\"seconds\":179,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2018,\"month\":9,\"day\":6},\"time\":{\"hour\":0,\"minute\":18,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2018,\"month\":9,\"day\":6},\"time\":{\"hour\":0,\"minute\":20,\"second\":59,\"nano\":0}}}");

        //session.createQueue("Lyon-Northbound", "Lyon-Northbound", true);à

//        try {
//            session.createQueue(new QueueConfiguration("Lyon-Northbound").setAddress("Lyon-Northbound").setRoutingType(RoutingType.ANYCAST));
//        }catch(Exception e){}

//        producer.send(message);



//        ClientConsumer consumer = session.createConsumer("Lyon-Northbound");
//
//
//
//        ClientMessage msgReceived = consumer.receive();
//
//        var buffer = msgReceived.getBodyBuffer().byteBuf();
//        var builder = new StringBuilder();
//
//        while (buffer.isReadable()){
//            char c = (char) buffer.readByte();
//            if(!Character.isWhitespace(c)){
//                builder.append(c);
//                System.out.print(c);
//            }
//        }

        //var msg = builder.toString();

        //ClientMessage msgReceived2 = consumer.receive();

        //System.out.println("message = " + msg);

        //msg = "{\"linkId\":-12500009347401,\"timestamp\":1615633113097,\"avgTravelTime\":0.1849680004119873,\"sdTravelTime\":0.0,\"numVehicles\":1,\"aggPeriod\":{\"seconds\":179,\"nanos\":0},\"startTime\":{\"date\":{\"year\":2018,\"month\":9,\"day\":6},\"time\":{\"hour\":0,\"minute\":0,\"second\":0,\"nano\":0}},\"endTime\":{\"date\":{\"year\":2018,\"month\":9,\"day\":6},\"time\":{\"hour\":0,\"minute\":2,\"second\":59,\"nano\":0}}}";

//        Gson gson = new Gson();
//
//        msg = msg.substring(msg.indexOf('{'),msg.lastIndexOf('}')+1);
//
//        JsonReader reader = new JsonReader(new StringReader(msg));
//        reader.setLenient(true);
//
//        Sample user = gson.fromJson(reader, Sample.class);
        //Sample user = gson.fromJson(new InputStreamReader(new ByteArrayInputStream(StandardCharsets.UTF_16LE.encode(msg).array())), Sample.class);

        //msg = msg.replaceAll("[\\x00-\\x09\\x11\\x12\\x14-\\x1F\\x7F\\x0B\\x0C\\x0E-]", "");

        //Sample sample = new ObjectMapper().readValue(msg, Sample.class);

//        session.close();
    }
}

