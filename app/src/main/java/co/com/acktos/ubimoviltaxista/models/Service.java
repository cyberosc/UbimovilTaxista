package co.com.acktos.ubimoviltaxista.models;

/**
 * Created by Acktos on 3/2/16.
 */
public class Service {


    private String id;
    private String time;
    private String state;
    private String type;
    private String client;
    private String pickup;


    public Service(String id,String time, String state, String type, String client, String pickup){

        setId(id);
        setTime(time);
        setState(state);
        setType(type);
        setClient(client);
        setPickup(pickup);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }
}
