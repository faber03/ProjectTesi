###############
# INSTALLAZIONE
###############

1. scaricare apache-jmeter-5.4.1.zip

2. installare il plugin manager:

    https://jmeter-plugins.org/wiki/PluginInstall/

#################################
# INVIO MESSAGGIO SU CODA ARTEMIS
#################################

1. scaricare la libreria activemq.all e copiarla in <cartella jmeter>/lib/ext

    https://mvnrepository.com/artifact/org.apache.activemq/activemq-all/5.10.0

2. creare in Jmeter un ThreadGroup:

    tasto destro su "test plan" -> add -> threads -> thread group

3. aggiungere al ThreadGroup un JMS Sampler:

     tasto destro sul ThreadGroup ->  add -> Sampler -> JMS Point-to-Point

4. nella configurazione del JMS Sampler aggiungere:

    - QueueConnectionFactory: ConnectionFactory
    - JNDI name Request queue: Q.REQ
    - Number of samples to aggregate: 1
    - Message properties:
        - request_only
        - Use Request Message Id [X]
    - Content: <messaggio da accodare>
    - JNDI Properties -> add:
        name: queue.Q.REQ
        value: <nome della coda>
    - Provider URL: <indirizzo di artemis> (es. tcp://localhost:61616)

5. aggiungere un Listener per visualizzare l'esito delle richieste:

    tasto destro sul ThreadGroup -> add -> Listener -> View Results Tree

6. avviare il test...

#####################################################
# GESTIRE IL TRAFFICO CON IL THROUGHPUT SHAPING TIMER
#####################################################

Link guida:
https://www.blazemeter.com/blog/using-jmeters-throughput-shaping-timer-plugin

    -> Throughput Shaping Time:
        https://jmeter-plugins.org/wiki/ThroughputShapingTimer/

    -> Concurrency Thread Group:
       https://jmeter-plugins.org/wiki/ConcurrencyThreadGroup/
------------------------------------------------------

1. da option -> plugin manager ->  available plugins, cercare e installare:

       - Concurrency Thread Group
       - Throughput Shaping Timer
       - jpgc - Standard Set
       - Composite Timeline Graph

2. aggiungere il Concurrency Thread Group e il Throughput Shaping Timer al test plan:

    tasto destro su "test plan" -> add -> threads -> bzm - Concurrency Thread Group

    tasto destro su "test plan" -> add -> timer -> jp@gc - Throughput Shaping Timer

3. aggiungere e configurare un JMS Sampler sotto il Concurrency Thread Group

4. aggiungere un Listener di tipo "View Results Tree" al Thread Group per visualizzare l'esito delle richieste:

5. aggiungere Transactions per Second, Active Threads Over Time e Composite Graph al test plan:

    tasto destro su "test plan" -> add -> Listener -> jp@gc - Transactions per Second

    tasto destro su "test plan" -> add -> Listener -> jp@gc - Active Threads Over Time

    tasto destro su "test plan" -> add -> Listener -> jp@gc - Composite Graph

6. disegnare il throughput in modo tabellare sullo Shaping Timer

7. Sul Concurrency Thread Group configurare:

    - Target Concurrency: ${__tstFeedback(<nome dello shaping timer>,<threads min>,<threads min>>,<spare threads>)}

    - hold target rate time (min): <durata del test>

8. Avviare il test...

