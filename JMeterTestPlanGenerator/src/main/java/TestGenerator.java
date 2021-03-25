import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TestGenerator {

    public static void main(String[] args) throws IOException {

        var file = new File("output/testplan.jmx");
        var br = new BufferedWriter(new FileWriter(file));

        var areaNames = new String[]{"Lyon", "Venissieux", "Vienne", "Meyzieu", "Caluire-et-Cuire", "Saint-Genis-Laval", "Saint-Quentin-Fallavier", "Genas", "Ecully", "Dardilly", "Sainte-Foy-les-Lyon", "Villefontaine", "Chassieu", "Tassin-la-Demi-Lune", "Charvieu-Chavagneux", "Miribel", "Saint-Laurent-de-Mure", "Oullins", "Montluel", "Heyrieux"};

        var testDurationSec = "33";
        var artemisServer = "tcp://172.18.10.147:30340";
        //var artemisServer = "tcp://localhost:61616";
        var csvFolderPath = "./samples/";

        var testPlan = "" +
                ////////
                //Header
                ////////
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<jmeterTestPlan version=\"1.2\" properties=\"5.0\" jmeter=\"5.4.1\">\n" +
                "  <hashTree>\n";

        var enabled = "true";
        testPlan = testPlan +

                //////////
                //TestPlan
                //////////
                "    <TestPlan guiclass=\"TestPlanGui\" testclass=\"TestPlan\" testname=\"TestPlan\" enabled=\"" + enabled + "\">\n" +
                "      <stringProp name=\"TestPlan.comments\"></stringProp>\n" +
                "      <boolProp name=\"TestPlan.functional_mode\">false</boolProp>\n" +
                "      <boolProp name=\"TestPlan.tearDown_on_shutdown\">true</boolProp>\n" +
                "      <boolProp name=\"TestPlan.serialize_threadgroups\">false</boolProp>\n" +
                "      <elementProp name=\"TestPlan.user_defined_variables\" elementType=\"Arguments\" guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">\n" +
                "        <collectionProp name=\"Arguments.arguments\"/>\n" +
                "      </elementProp>\n" +
                "      <stringProp name=\"TestPlan.user_define_classpath\"></stringProp>\n" +
                "    </TestPlan>" + "\n" +
                "    <hashTree>" + "\n";

                        for(var areaName : areaNames)
                        {
                            enabled = "true";
                            testPlan = testPlan +

                                    //////////////////////////
                                    //Concurrency Thread Group
                                    //////////////////////////
                                    "      <com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup guiclass=\"com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroupGui\" testclass=\"com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup\" testname=\"ConcurrencyThreadGroup_"+ areaName +"\" enabled=\"" + enabled + "\">\n" +
                                    "        <elementProp name=\"ThreadGroup.main_controller\" elementType=\"com.blazemeter.jmeter.control.VirtualUserController\"/>\n" +
                                    "        <stringProp name=\"ThreadGroup.on_sample_error\">continue</stringProp>\n" +
                                    "        <stringProp name=\"TargetLevel\">${__tstFeedback(shapingTimer,1,100,5)}</stringProp>\n" +
                                    "        <stringProp name=\"RampUp\"></stringProp>\n" +
                                    "        <stringProp name=\"Steps\"></stringProp>\n" +
                                    "        <stringProp name=\"Hold\">" + testDurationSec + "</stringProp>\n" +
                                    "        <stringProp name=\"LogFilename\"></stringProp>\n" +
                                    "        <stringProp name=\"Iterations\"></stringProp>\n" +
                                    "        <stringProp name=\"Unit\">M</stringProp>\n" +
                                    "      </com.blazemeter.jmeter.threads.concurrency.ConcurrencyThreadGroup>\n" +

                                    "      <hashTree>\n";  //-----------------------------------------START THREAD GROUP

                            enabled = "true";
                            testPlan = testPlan +

                                    /////////////
                                    //JMS Sampler
                                    /////////////

                                    "        <JMSSampler guiclass=\"JMSSamplerGui\" testclass=\"JMSSampler\" testname=\"Sampler_" + areaName + "\" enabled=\"" + enabled + "\">\n" +
                                    "          <stringProp name=\"JMSSampler.queueconnectionfactory\">ConnectionFactory</stringProp>\n" +
                                    "          <stringProp name=\"JMSSampler.SendQueue\">Q.REQ</stringProp>\n" +
                                    "          <stringProp name=\"JMSSampler.ReceiveQueue\"></stringProp>\n" +
                                    "          <intProp name=\"JMSSampler.communicationStyle\">0</intProp>\n" +
                                    "          <boolProp name=\"JMSSampler.isNonPersistent\">false</boolProp>\n" +
                                    "          <boolProp name=\"JMSSampler.useReqMsgIdAsCorrelId\">true</boolProp>\n" +
                                    "          <stringProp name=\"JMSSampler.timeout\">2000</stringProp>\n" +
                                    "          <stringProp name=\"HTTPSamper.xml_data\">{&quot;linkId&quot;:${linkId},&quot;timestamp&quot;:${timestamp},&quot;avgTravelTime&quot;:${avgTravelTime},&quot;sdTravelTime&quot;:${sdTravelTime},&quot;numVehicles&quot;:${numVehicles},&quot;aggPeriod&quot;:{&quot;seconds&quot;:${aggPeriod.seconds},&quot;nanos&quot;:${aggPeriod.nanos}},&quot;startTime&quot;:{&quot;date&quot;:{&quot;year&quot;:${startTime.date.year},&quot;month&quot;:${startTime.date.month},&quot;day&quot;:${startTime.date.day}},&quot;time&quot;:{&quot;hour&quot;:${startTime.time.hour},&quot;minute&quot;:${startTime.time.minute},&quot;second&quot;:${startTime.time.second},&quot;nano&quot;:${startTime.time.nano}}},&quot;endTime&quot;:{&quot;date&quot;:{&quot;year&quot;:${endTime.date.year},&quot;month&quot;:${endTime.date.month},&quot;day&quot;:${endTime.date.day}},&quot;time&quot;:{&quot;hour&quot;:${endTime.time.hour},&quot;minute&quot;:${endTime.time.minute},&quot;second&quot;:${endTime.time.second},&quot;nano&quot;:${endTime.time.nano}}}}</stringProp>\n" +
                                    "          <stringProp name=\"JMSSampler.initialContextFactory\">org.apache.activemq.jndi.ActiveMQInitialContextFactory</stringProp>\n" +
                                    "          <stringProp name=\"JMSSampler.contextProviderUrl\">" + artemisServer + "</stringProp>\n" +
                                    "          <elementProp name=\"JMSSampler.jndiProperties\" elementType=\"Arguments\" guiclass=\"ArgumentsPanel\" testclass=\"Arguments\" testname=\"User Defined Variables\" enabled=\"true\">\n" +
                                    "            <collectionProp name=\"Arguments.arguments\">\n" +
                                    "              <elementProp name=\"queue.Q.REQ\" elementType=\"Argument\">\n" +
                                    "                <stringProp name=\"Argument.name\">queue.Q.REQ</stringProp>\n" +
                                    "                <stringProp name=\"Argument.value\">" + areaName + "-Northbound</stringProp>\n" +
                                    "                <stringProp name=\"Argument.metadata\">=</stringProp>\n" +
                                    "              </elementProp>\n" +
                                    "            </collectionProp>\n" +
                                    "          </elementProp>\n" +
                                    "          <elementProp name=\"arguments\" elementType=\"JMSProperties\">\n" +
                                    "            <collectionProp name=\"JMSProperties.properties\"/>\n" +
                                    "          </elementProp>\n" +
                                    "        </JMSSampler>\n" +
                                    "        <hashTree/>\n";

                            enabled = "true";
                            testPlan = testPlan +

                                    /////////////
                                    //CSV DataSet
                                    /////////////

                                    "        <CSVDataSet guiclass=\"TestBeanGUI\" testclass=\"CSVDataSet\" testname=\"Csv_" + areaName + "\" enabled=\"" + enabled + "\">\n" +
                                    "          <stringProp name=\"delimiter\">;</stringProp>\n" +
                                    "          <stringProp name=\"fileEncoding\"></stringProp>\n" +
                                    "          <stringProp name=\"filename\">" + csvFolderPath + areaName + ".csv</stringProp>\n" +
                                    "          <boolProp name=\"ignoreFirstLine\">false</boolProp>\n" +
                                    "          <boolProp name=\"quotedData\">false</boolProp>\n" +
                                    "          <boolProp name=\"recycle\">true</boolProp>\n" +
                                    "          <stringProp name=\"shareMode\">shareMode.group</stringProp>\n" +
                                    "          <boolProp name=\"stopThread\">false</boolProp>\n" +
                                    "          <stringProp name=\"variableNames\"></stringProp>\n" +
                                    "        </CSVDataSet>\n" +
                                    "        <hashTree/>" + "\n";

                            enabled = "false";
                            testPlan = testPlan +

                                    ///////////////////
                                    //View Results Tree
                                    ///////////////////

                                    "        <ResultCollector guiclass=\"ViewResultsFullVisualizer\" testclass=\"ResultCollector\" testname=\"View Results Tree\" enabled=\"" + enabled + "\">\n" +
                                    "          <boolProp name=\"ResultCollector.error_logging\">false</boolProp>\n" +
                                    "          <objProp>\n" +
                                    "            <name>saveConfig</name>\n" +
                                    "            <value class=\"SampleSaveConfiguration\">\n" +
                                    "              <time>true</time>\n" +
                                    "              <latency>true</latency>\n" +
                                    "              <timestamp>true</timestamp>\n" +
                                    "              <success>true</success>\n" +
                                    "              <label>true</label>\n" +
                                    "              <code>true</code>\n" +
                                    "              <message>true</message>\n" +
                                    "              <threadName>true</threadName>\n" +
                                    "              <dataType>true</dataType>\n" +
                                    "              <encoding>false</encoding>\n" +
                                    "              <assertions>true</assertions>\n" +
                                    "              <subresults>true</subresults>\n" +
                                    "              <responseData>false</responseData>\n" +
                                    "              <samplerData>false</samplerData>\n" +
                                    "              <xml>false</xml>\n" +
                                    "              <fieldNames>true</fieldNames>\n" +
                                    "              <responseHeaders>false</responseHeaders>\n" +
                                    "              <requestHeaders>false</requestHeaders>\n" +
                                    "              <responseDataOnError>false</responseDataOnError>\n" +
                                    "              <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>\n" +
                                    "              <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                                    "              <bytes>true</bytes>\n" +
                                    "              <sentBytes>true</sentBytes>\n" +
                                    "              <url>true</url>\n" +
                                    "              <threadCounts>true</threadCounts>\n" +
                                    "              <idleTime>true</idleTime>\n" +
                                    "              <connectTime>true</connectTime>\n" +
                                    "            </value>\n" +
                                    "          </objProp>\n" +
                                    "          <stringProp name=\"filename\"></stringProp>\n" +
                                    "        </ResultCollector>\n" +
                                    "        <hashTree/>\n" +

                                    "      </hashTree>\n"; //-------------------------------------------END THREAD GROUP
                        }

                        enabled = "true";
                        testPlan = testPlan +

                                //////////////////////////
                                //Throughput Shaping Timer
                                //////////////////////////
                                "      <kg.apc.jmeter.timers.VariableThroughputTimer guiclass=\"kg.apc.jmeter.timers.VariableThroughputTimerGui\" testclass=\"kg.apc.jmeter.timers.VariableThroughputTimer\" testname=\"shapingTimer\" enabled=\"" + enabled + "\">\n" +
                                "        <collectionProp name=\"load_profile\">\n" +

                                "          <collectionProp name=\"176726940\">\n" +
                                "            <stringProp name=\"1567\">20</stringProp>\n" +
                                "            <stringProp name=\"1567\">20</stringProp>\n" +
                                "            <stringProp name=\"1515111\">900</stringProp>\n" +
                                "          </collectionProp>\n" +

                                "          <collectionProp name=\"176726940\">\n" +
                                "            <stringProp name=\"1567\">20</stringProp>\n" +
                                "            <stringProp name=\"1567\">20</stringProp>\n" +
                                "            <stringProp name=\"1515111\">900</stringProp>\n" +
                                "          </collectionProp>\n" +

                                "        </collectionProp>\n" +
                                "      </kg.apc.jmeter.timers.VariableThroughputTimer>\n" +
                                "      <hashTree/>\n" +
                                "      <kg.apc.jmeter.vizualizers.CorrectedResultCollector guiclass=\"kg.apc.jmeter.vizualizers.TransactionsPerSecondGui\" testclass=\"kg.apc.jmeter.vizualizers.CorrectedResultCollector\" testname=\"TransactionsPerSecond\" enabled=\"true\">\n" +
                                "        <boolProp name=\"ResultCollector.error_logging\">false</boolProp>\n" +
                                "        <objProp>\n" +
                                "          <name>saveConfig</name>\n" +
                                "          <value class=\"SampleSaveConfiguration\">\n" +
                                "            <time>true</time>\n" +
                                "            <latency>true</latency>\n" +
                                "            <timestamp>true</timestamp>\n" +
                                "            <success>true</success>\n" +
                                "            <label>true</label>\n" +
                                "            <code>true</code>\n" +
                                "            <message>true</message>\n" +
                                "            <threadName>true</threadName>\n" +
                                "            <dataType>true</dataType>\n" +
                                "            <encoding>false</encoding>\n" +
                                "            <assertions>true</assertions>\n" +
                                "            <subresults>true</subresults>\n" +
                                "            <responseData>false</responseData>\n" +
                                "            <samplerData>false</samplerData>\n" +
                                "            <xml>false</xml>\n" +
                                "            <fieldNames>true</fieldNames>\n" +
                                "            <responseHeaders>false</responseHeaders>\n" +
                                "            <requestHeaders>false</requestHeaders>\n" +
                                "            <responseDataOnError>false</responseDataOnError>\n" +
                                "            <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>\n" +
                                "            <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                                "            <bytes>true</bytes>\n" +
                                "            <sentBytes>true</sentBytes>\n" +
                                "            <url>true</url>\n" +
                                "            <threadCounts>true</threadCounts>\n" +
                                "            <idleTime>true</idleTime>\n" +
                                "            <connectTime>true</connectTime>\n" +
                                "          </value>\n" +
                                "        </objProp>\n" +
                                "        <stringProp name=\"filename\"></stringProp>\n" +
                                "        <longProp name=\"interval_grouping\">1000</longProp>\n" +
                                "        <boolProp name=\"graph_aggregated\">false</boolProp>\n" +
                                "        <stringProp name=\"include_sample_labels\"></stringProp>\n" +
                                "        <stringProp name=\"exclude_sample_labels\"></stringProp>\n" +
                                "        <stringProp name=\"start_offset\"></stringProp>\n" +
                                "        <stringProp name=\"end_offset\"></stringProp>\n" +
                                "        <boolProp name=\"include_checkbox_state\">false</boolProp>\n" +
                                "        <boolProp name=\"exclude_checkbox_state\">false</boolProp>\n" +
                                "      </kg.apc.jmeter.vizualizers.CorrectedResultCollector>\n" +
                                "      <hashTree/>\n";

                        enabled = "true";
                        testPlan = testPlan +

                                //////////////////////////
                                //Active Threads Over Time
                                //////////////////////////
                                "      <kg.apc.jmeter.vizualizers.CorrectedResultCollector guiclass=\"kg.apc.jmeter.vizualizers.ThreadsStateOverTimeGui\" testclass=\"kg.apc.jmeter.vizualizers.CorrectedResultCollector\" testname=\"ActiveThreadsOverTime\" enabled=\"" + enabled + "\">\n" +
                                "        <boolProp name=\"ResultCollector.error_logging\">false</boolProp>\n" +
                                "        <objProp>\n" +
                                "          <name>saveConfig</name>\n" +
                                "          <value class=\"SampleSaveConfiguration\">\n" +
                                "            <time>true</time>\n" +
                                "            <latency>true</latency>\n" +
                                "            <timestamp>true</timestamp>\n" +
                                "            <success>true</success>\n" +
                                "            <label>true</label>\n" +
                                "            <code>true</code>\n" +
                                "            <message>true</message>\n" +
                                "            <threadName>true</threadName>\n" +
                                "            <dataType>true</dataType>\n" +
                                "            <encoding>false</encoding>\n" +
                                "            <assertions>true</assertions>\n" +
                                "            <subresults>true</subresults>\n" +
                                "            <responseData>false</responseData>\n" +
                                "            <samplerData>false</samplerData>\n" +
                                "            <xml>false</xml>\n" +
                                "            <fieldNames>true</fieldNames>\n" +
                                "            <responseHeaders>false</responseHeaders>\n" +
                                "            <requestHeaders>false</requestHeaders>\n" +
                                "            <responseDataOnError>false</responseDataOnError>\n" +
                                "            <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>\n" +
                                "            <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                                "            <bytes>true</bytes>\n" +
                                "            <sentBytes>true</sentBytes>\n" +
                                "            <url>true</url>\n" +
                                "            <threadCounts>true</threadCounts>\n" +
                                "            <idleTime>true</idleTime>\n" +
                                "            <connectTime>true</connectTime>\n" +
                                "          </value>\n" +
                                "        </objProp>\n" +
                                "        <stringProp name=\"filename\"></stringProp>\n" +
                                "        <longProp name=\"interval_grouping\">500</longProp>\n" +
                                "        <boolProp name=\"graph_aggregated\">true</boolProp>\n" +
                                "        <stringProp name=\"include_sample_labels\"></stringProp>\n" +
                                "        <stringProp name=\"exclude_sample_labels\"></stringProp>\n" +
                                "        <stringProp name=\"start_offset\"></stringProp>\n" +
                                "        <stringProp name=\"end_offset\"></stringProp>\n" +
                                "        <boolProp name=\"include_checkbox_state\">false</boolProp>\n" +
                                "        <boolProp name=\"exclude_checkbox_state\">false</boolProp>\n" +
                                "      </kg.apc.jmeter.vizualizers.CorrectedResultCollector>\n" +
                                "      <hashTree/>\n";

                        enabled = "true";
                        testPlan = testPlan +

                                ////////////////////////////
                                //Composite Result Collector
                                ////////////////////////////
                                "      <kg.apc.jmeter.vizualizers.CompositeResultCollector guiclass=\"kg.apc.jmeter.vizualizers.CompositeGraphGui\" testclass=\"kg.apc.jmeter.vizualizers.CompositeResultCollector\" testname=\"CompositeGraph\" enabled=\"" + enabled + "\">\n" +
                                "        <boolProp name=\"ResultCollector.error_logging\">false</boolProp>\n" +
                                "        <objProp>\n" +
                                "          <name>saveConfig</name>\n" +
                                "          <value class=\"SampleSaveConfiguration\">\n" +
                                "            <time>true</time>\n" +
                                "            <latency>true</latency>\n" +
                                "            <timestamp>true</timestamp>\n" +
                                "            <success>true</success>\n" +
                                "            <label>true</label>\n" +
                                "            <code>true</code>\n" +
                                "            <message>true</message>\n" +
                                "            <threadName>true</threadName>\n" +
                                "            <dataType>true</dataType>\n" +
                                "            <encoding>false</encoding>\n" +
                                "            <assertions>true</assertions>\n" +
                                "            <subresults>true</subresults>\n" +
                                "            <responseData>false</responseData>\n" +
                                "            <samplerData>false</samplerData>\n" +
                                "            <xml>false</xml>\n" +
                                "            <fieldNames>true</fieldNames>\n" +
                                "            <responseHeaders>false</responseHeaders>\n" +
                                "            <requestHeaders>false</requestHeaders>\n" +
                                "            <responseDataOnError>false</responseDataOnError>\n" +
                                "            <saveAssertionResultsFailureMessage>true</saveAssertionResultsFailureMessage>\n" +
                                "            <assertionsResultsToSave>0</assertionsResultsToSave>\n" +
                                "            <bytes>true</bytes>\n" +
                                "            <sentBytes>true</sentBytes>\n" +
                                "            <url>true</url>\n" +
                                "            <threadCounts>true</threadCounts>\n" +
                                "            <idleTime>true</idleTime>\n" +
                                "            <connectTime>true</connectTime>\n" +
                                "          </value>\n" +
                                "        </objProp>\n" +
                                "        <stringProp name=\"filename\"></stringProp>\n" +
                                "        <longProp name=\"interval_grouping\">500</longProp>\n" +
                                "        <boolProp name=\"graph_aggregated\">false</boolProp>\n" +
                                "        <stringProp name=\"include_sample_labels\"></stringProp>\n" +
                                "        <stringProp name=\"exclude_sample_labels\"></stringProp>\n" +
                                "        <stringProp name=\"start_offset\"></stringProp>\n" +
                                "        <stringProp name=\"end_offset\"></stringProp>\n" +
                                "        <boolProp name=\"include_checkbox_state\">false</boolProp>\n" +
                                "        <boolProp name=\"exclude_checkbox_state\">false</boolProp>\n" +
                                "        <collectionProp name=\"COMPOSITE_CFG\">\n" +
                                "          <collectionProp name=\"\">\n" +
                                "            <stringProp name=\"-845910532\">TransactionsPerSecond</stringProp>\n" +
                                "          </collectionProp>\n" +
                                "          <collectionProp name=\"\">\n" +
                                "            <stringProp name=\"117711132\">Successful Transactions per Second</stringProp>\n" +
                                "          </collectionProp>\n" +
                                "        </collectionProp>\n" +
                                "      </kg.apc.jmeter.vizualizers.CompositeResultCollector>\n" +
                                "      <hashTree/>" + "\n";

                        testPlan = testPlan +

                         ////////
                         //Footer
                         ////////
                        "    " + "</hashTree>" + "\n" +
                    "  " + "</hashTree>" + "\n" +
                "</jmeterTestPlan>";

        br.write(testPlan);
        br.close();
    }
}
