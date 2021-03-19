import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "length", "ffs", "speed", "frc", "netclass", "fow", "routenumber", "areaname", "name", "geom" })
public class Link {
    public String id;
    public String length;
    public String ffs;
    public String speed;
    public String frc;
    public String netclass;
    public String fow;
    public String routenumber;
    public String areaname;
    public String name;
    public String geom;
}
