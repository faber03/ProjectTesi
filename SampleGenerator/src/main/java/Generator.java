import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class Generator {

    public static void main(String[] args) throws IOException {

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

        var links = new File("resources/all_links_edited.csv");
        //var myReader = new Scanner(links);

//        var csvMapper = new CsvMapper();
//        var schema = CsvSchema.emptySchema().withHeader();
//        var oReader = csvMapper.reader(Link.class).with(schema);

        var mapper  = new CsvMapper();
        CsvSchema sclema = mapper.schemaFor(Link.class)
                .withSkipFirstDataRow(true)
                .withColumnSeparator(';').withoutQuoteChar();

        MappingIterator<Link> iterator = mapper
                .readerFor(Link.class)
                .with(sclema).readValues(links);

        while (iterator.hasNext()) {

            Link item = iterator.next();

            if(!areanames.contains(item.areaname))
            {
                areanames.add(item.areaname);
                var file = new File("output/"+item.areaname+".csv");
                var br = new BufferedWriter(new FileWriter(file));
                mapWriters.put(item.areaname, br);
                mapLinks.put(item.areaname, new ArrayList<String>());
                mapLinkCount.put(item.areaname, 0);

                br.write(header);
            }

            var arealinks = mapLinks.get(item.areaname);
            if(!arealinks.contains(item.id))
            {
                arealinks.add(item.id);
                mapLinkCount.put(item.areaname, mapLinkCount.get(item.areaname) + 1);
            }

            var timestamp = new Timestamp(System.currentTimeMillis());
            var writer = mapWriters.get(item.areaname);
            var random = new Random();

            //random.nextInt(max - min) + min;
            var aggPeriod_seconds = random.nextInt(300 - 100) + 1;
            var startTime_date_month = random.nextInt(12 - 1) + 1;
            var startTime_date_day = random.nextInt(28 - 1) + 1;
            var startTime_time_hour = random.nextInt(23 - 0) + 0;
            var startTime_time_minute = random.nextInt(59 - 0) + 0;
            var startTime_time_second = random.nextInt(59 - 0) + 0;
            var endTime_time_minute = random.nextInt(59 - startTime_time_hour) + startTime_time_hour;
            var endTime_time_second = random.nextInt(59 - startTime_time_second) + startTime_time_second;
            writer.write(item.id +
                    ";" +
                    timestamp.getTime() + ";" +     //timestamp
                    "0.1849680004119873;"+          //avgTravelTime
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

        var file = new File("output/Info.txt");
        var br2 = new BufferedWriter(new FileWriter(file));
        var linkIterator = mapLinkCount.entrySet().iterator();

        int i = 0;
        Files.createDirectories(Path.of("output/selected/"));
        while (linkIterator.hasNext()) {
            var queue = linkIterator.next().getKey();
            var linkCount = linkIterator.next().getValue();
            br2.write("queue: " + queue + " - links: " + linkCount + "\n");

            if(i < 20)
                Files.move(Path.of("output/"+queue+".csv"),Path.of("output/selected/" + StringUtils.stripAccents(queue) + ".csv"));

            i++;
        }

        br2.close();
    }
}

