import org.neo4j.driver.*;

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
        try ( Connector connector = new Connector( "bolt://neo4j:7687", "neo4j", "password" ) )
        {
            while(true)
                connector.printGreeting( "hello, world" );
        }
    }
}
