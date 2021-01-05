import org.neo4j.driver.*;
import org.neo4j.driver.Record;

import java.net.InetAddress;

import static java.lang.Thread.sleep;
import static org.neo4j.driver.Values.parameters;

public class Connector implements AutoCloseable{

    private final Driver driver;
    private final String dbName;
    //private final Session session;

    public Connector( String uri, String dbName, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
        //session = driver.session(SessionConfig.forDatabase("TestDatabase"));
        this.dbName = dbName;
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    boolean result1 = tx.run( "MATCH (a:Greeting) " +
                                    "WHERE a.message = $message " +
                                    "RETURN a.message",
                            parameters( "message", message ) ).hasNext();

                    String res = null;
                    if(!result1) {

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Result result = tx.run("CREATE (a:Greeting) " +
                                        "SET a.message = $message " +
                                        "RETURN a.message + ', from node ' + id(a)",
                                parameters("message", message));
                        res =  result.single().get(0).asString();
                    }

                    return res;
                }
            } );

            if(greeting != null)
                System.out.println( greeting );
        }
    }

    public void createNode( final String name )
    {
        try (var session = driver.session(SessionConfig.forDatabase(dbName)))
        {
            var record = session.writeTransaction( new TransactionWork<Record>()
            {
                @Override
                public Record execute(Transaction tx )
                {
                    var result = tx.run( "MATCH (a:Node) " +
                                    "WHERE a.name = $name " +
                                    "RETURN id(a) as id, a.name, a.count",
                            parameters( "name", name ));

                    Record record;
                    if(result.hasNext())
                    {
                        record = result.single();

                        System.out.println("found node " + String.valueOf(record.get(0).asInt())+
                                ", name: " + record.get(1).asString() +
                                ", count: " + String.valueOf(record.get(2).asInt()));
                    }
                    else
                    {
                        record = tx.run("CREATE (a:Node) " +
                                        "SET a.name = $name, a.count = 0 " +
                                        "RETURN id(a) as id, a.name, a.count",
                                parameters("name", name)).single();

                        System.out.println("create node " + String.valueOf(record.get(0).asInt())+
                                ", name: " + record.get(1).asString() +
                                ", count: " + String.valueOf(record.get(2).asInt()));
                    }
                    return record;
                }
            });
        }
    }

    public void updateNodeCount(final String name)
    {
        try (var session = driver.session(SessionConfig.forDatabase(dbName)))
        {
            var record = session.writeTransaction( new TransactionWork<Record>()
            {
                @Override
                public Record execute( Transaction tx )
                {
                    var result = tx.run( "MATCH (a:Node) " +
                                    "WHERE a.name = $name " +
                                    "RETURN id(a) as id, a.name, a.count",
                            parameters( "name", name ));

                    Record record = null;

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(result.hasNext())
                    {
                        record = tx.run("MATCH (a:Node { name: $name }) " +
                                        "SET a.count = a.count + 1 "  +
                                        "RETURN id(a) as id, a.name, a.count",
                                parameters("name", name)).single();
                    }

                    return record;
                }
            } );

            if(record != null) {
                System.out.println("update node " + String.valueOf(record.get(0).asInt()) +
                        ", name: " + record.get(1).asString() +
                        ", count: " + String.valueOf(record.get(2).asInt()));
            }
        }
    }

    public static void main( String... args ) throws Exception
    {
        String neo4jUsername = System.getenv("NEO4J_USERNAME");
        String neo4jPassword = System.getenv("NEO4J_PASSWORD");
        String neo4jHost = System.getenv("NEO4J_HOST");
        String neo4jDbName = System.getenv("NEO4J_DBNAME");

        try(Connector connector = new Connector(
                "bolt://" + (neo4jHost != null ? neo4jHost : "localhost:7687"),
                neo4jDbName != null ? neo4jDbName: "neo4j",
                neo4jUsername != null ? neo4jUsername : "neo4j",
                neo4jPassword != null ? neo4jPassword : "admin"))
        {
            //long pid = ProcessHandle.current().pid();
            var hostname = InetAddress.getLocalHost().getHostName();

            var nodeName = hostname;
            connector.createNode(nodeName);

            while(true)
            {
                Thread.sleep(2000);
                connector.updateNodeCount(nodeName);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
