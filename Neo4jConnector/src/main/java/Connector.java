import org.neo4j.driver.*;

import static java.lang.Thread.sleep;
import static org.neo4j.driver.Values.parameters;

public class Connector implements AutoCloseable{

    private final Driver driver;

    public Connector( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
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

    public static void main( String... args ) throws Exception
    {
        String neo4jUsername = System.getenv("NEO4J_USERNAME");
        String neo4jPassword = System.getenv("NEO4J_PASSWORD");
        String neo4jHost = System.getenv("NEO4J_HOST");

        try ( Connector connector = new Connector(
                "bolt://" + (neo4jHost != null ? neo4jHost : "localhost:7687"),
                neo4jUsername != null ? neo4jUsername : "neo4j",
                neo4jPassword != null ? neo4jPassword : "admin" ) )
        {
            while(true)
                connector.printGreeting( "hello, world" );
        }
    }
}
