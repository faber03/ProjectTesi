import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class SampleGenerator {

    public static void main(String[] args) throws IOException {

        var queueNumber = 20;
        var artemisServer = "http://172.18.10.147:30341";

        var areanames = new ArrayList<String>();
        var mapWriters = new HashMap<String,BufferedWriter>();
        var mapLinks = new HashMap<String, ArrayList<String>>();
        var mapLinkCount = new HashMap<String, Integer>();

        var header = "linkId;" +
                "timestamp;" +
                "avgTravelTime;" +
                "sdTravelTime;" +
                "numVehicles;" +
                "aggPeriod.seconds;" +
                "aggPeriod.nanos;" +
                "startTime.date.year;" +
                "startTime.date.month;" +
                "startTime.date.day;" +
                "startTime.time.hour;" +
                "startTime.time.minute;" +
                "startTime.time.second;" +
                "startTime.time.nano;" +
                "endTime.date.year;" +
                "endTime.date.month;" +
                "endTime.date.day;" +
                "endTime.time.hour;" +
                "endTime.time.minute;" +
                "endTime.time.second;" +
                "endTime.time.nano" +
                "\n";

        var links = new File("input/all_links_edited.csv");

//        var myReader = new Scanner(links);

//        var csvMapper = new CsvMapper();
//        var schema = CsvSchema.emptySchema().withHeader();
//        var oReader = csvMapper.reader(Link.class).with(schema);

        var mapper  = new CsvMapper();
        CsvSchema sclema = mapper.schemaFor(Link.class)
                .withSkipFirstDataRow(true)
                .withColumnSeparator(';').withoutQuoteChar();

        MappingIterator<Link> iteratorLinks = mapper
                .readerFor(Link.class)
                .with(sclema).readValues(links);

        while (iteratorLinks.hasNext()) {

            Link item = iteratorLinks.next();
            var easyAreaName = StringUtils.stripAccents(item.areaname);

            var outputSelectedDir = new File("output/selected");
            var outputGeneratedDir = new File("output/generated");
            outputSelectedDir.mkdirs();
            outputGeneratedDir.mkdirs();
            if(!areanames.contains(easyAreaName))
            {
                areanames.add(easyAreaName);
                var file = new File("output/generated/" + easyAreaName + ".csv");
                var br = new BufferedWriter(new FileWriter(file, false));
                mapWriters.put(easyAreaName, br);
                mapLinks.put(easyAreaName, new ArrayList<String>());
                mapLinkCount.put(easyAreaName, 0);

                br.write(header);
            }

            var arealinks = mapLinks.get(easyAreaName);
            if(!arealinks.contains(item.id))
            {
                arealinks.add(item.id);
                mapLinkCount.put(easyAreaName, mapLinkCount.get(easyAreaName) + 1);
            }

            var timestamp = new Timestamp(System.currentTimeMillis());
            var writer = mapWriters.get(easyAreaName);
            var random = new Random();

            //random.nextInt(max - min) + min;
            var aggPeriod_seconds = random.nextInt(300 - 100) + 1;
            var startTime_date_month = random.nextInt(12 - 1) + 1;
            var startTime_date_day = random.nextInt(28 - 1) + 1;
            var startTime_time_hour = random.nextInt(23 - 0) + 0;
            var startTime_time_minute = random.nextInt(59 - 0) + 0;
            var startTime_time_second = random.nextInt(59 - 0) + 0;
            var avgTravelTime = (random.nextFloat() * (5F - 0.1)) + 0.1;
            var endTime_time_minute = random.nextInt(59 - startTime_time_hour) + startTime_time_hour;
            var endTime_time_second = random.nextInt(59 - startTime_time_second) + startTime_time_second;

            writer.write(item.id +
                    ";" +
                    timestamp.getTime() + ";" +     //timestamp
                    avgTravelTime + ";"+          //avgTravelTime - 0.1849680004119873
                    "0.0;" +                        //sdTravelTime
                    "1;" +                          //numVehicles
                    aggPeriod_seconds + ";" +       //aggPeriod.seconds - 179
                    "0;" +                          //aggPeriod.nanos;
                    "2021;" +                       //startTime.date.year - 2018
                    startTime_date_month + ";" +    //startTime.date.month - 9
                    startTime_date_day + ";" +      //startTime.date.day - 6
                    startTime_time_hour + ";" +     //startTime.time.hour - 0
                    startTime_time_minute + ";" +   //startTime.time.minute - 0
                    startTime_time_second + ";" +   //startTime.time.second - 0
                    "0;" +                          //startTime.time.nano
                    "2021;" +                       //endTime.date.year - 2018
                    startTime_time_second + ";" +   //endTime.date.month - 9
                    startTime_date_day + ";" +      //endTime.date.day - 6
                    startTime_time_hour + ";" +     //endTime.time.hour - 6
                    endTime_time_minute + ";" +     //endTime.time.minute - 2
                    endTime_time_second + ";" +     //endTime.time.second - 59
                    "0" +                           //endTime.time.nano
                    "\n");
        }

        //close queue files
        for (Map.Entry me : mapWriters.entrySet()) {
            var writers = mapWriters.get(me.getKey());
            writers.close();
        }

        //create info file
        mapLinkCount = mapLinkCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        var file = new File("output/queueInfo.txt");
        var br2 = new BufferedWriter(new FileWriter(file, false));
        var linkIterator = mapLinkCount.entrySet().iterator();

        int i = 1;
        Files.createDirectories(Path.of("output/selected/"));
        String queueInfoNameList = "{";
        String queueInfoQuery = "";
        String queueInfoTopicList = "";
        String queueInfoContent = "";
        String queueInfoPurge = "";
        String queueInfoDelete = "";
        while (linkIterator.hasNext()) {
            var queue = linkIterator.next().getKey();
            var linkCount = linkIterator.next().getValue();

            var easyQueueName = StringUtils.stripAccents(queue);
            queueInfoContent = queueInfoContent + "queue " + i + ": " + StringUtils.stripAccents(easyQueueName) + " - links: " + linkCount + "\n";

            if(i <= queueNumber) {
                queueInfoNameList = queueInfoNameList + "\"" + easyQueueName + "\", ";
                queueInfoQuery = queueInfoQuery + "\"neo4j.topic.cypher." + easyQueueName + "-Northbound\": \"MERGE ()-[s:STREET {linkId: event.linkId}]->() SET s.avgTravelTime = event.avgTravelTime,  s.sdTravelTime = event.sdTravelTime,  s.numVehicles = event.numVehicles,  s.timestamp = event.timestamp,  s.aggPeriod = duration({seconds: event.aggPeriod.seconds, nanoseconds: event.aggPeriod.nanos}),  s.startTime = localdatetime({year: event.startTime.date.year,month: event.startTime.date.month,day: event.startTime.date.day,hour: event.startTime.time.hour,minute: event.sample.startTime.time.minute,second: event.startTime.time.second,nanosecond: event.startTime.time.nano}),  s.endTime = localdatetime({year: event.endTime.date.year,month: event.endTime.date.month,day: event.endTime.date.day,hour: event.endTime.time.hour,minute: event.endTime.time.minute,second: event.endTime.time.second,nanosecond: event.endTime.time.nano})\",\n";
                queueInfoTopicList = queueInfoTopicList + easyQueueName + "-Northbound,";
                queueInfoPurge = queueInfoPurge + "curl -X POST -H \"Content-Type: application/json\" -d  '{ \"type\": \"EXEC\", \"mbean\": \"org.apache.activemq.artemis:address=\\\"" + easyQueueName + "-Northbound\\\",broker=\\\"0.0.0.0\\\",component=addresses,queue=\\\"" + easyQueueName + "-Northbound\\\",routing-type=\\\"anycast\\\",subcomponent=queues\", \"operation\": \"removeMessages(java.lang.String)\", \"arguments\": [ \"\" ] }' -u licit:licit  " + artemisServer + "/console/jolokia/exec\n" + "sleep 1\n";
                queueInfoDelete = queueInfoDelete + "curl -u licit:licit " + artemisServer + "/console/jolokia/exec/org.apache.activemq.artemis:broker=%220.0.0.0%22/destroyQueue\\(java.lang.String\\)/" + easyQueueName + "-Northbound\n" + "sleep 1\n";
                Files.move(Path.of("output/generated/" + queue + ".csv"), Path.of("output/selected/" + StringUtils.stripAccents(queue) + ".csv"), StandardCopyOption.REPLACE_EXISTING);
            }

            i++;
        }

        queueInfoNameList = queueInfoNameList.substring(0, queueInfoNameList.lastIndexOf(",")) + "}";
        queueInfoTopicList = "\"" + queueInfoTopicList.substring(0, queueInfoTopicList.lastIndexOf(",")) + "\"";

        br2.write(queueInfoNameList + "\n\n");
        br2.write(queueInfoTopicList + "\n\n");
        br2.write(queueInfoQuery + "\n\n");
        br2.write(queueInfoPurge + "\n\n");
        br2.write(queueInfoDelete + "\n\n");
        br2.write(queueInfoContent);
        br2.close();
    }
}

