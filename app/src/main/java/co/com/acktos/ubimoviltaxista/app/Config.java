package co.com.acktos.ubimoviltaxista.app;

/**
 * Created by Acktos on 2/10/16.
 */
public class Config {


    //DEBUG TAG
    public final static String DEBUG_TAG="ubimovilTaxistaDebug";


    //PREFERENCES
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";


    //GCM
    public final static String TOPIC_TAXI_DRIVER="taxiDriver";
    public final static String COLLAPSE_KEY="collapse_key";
    public final static String COLLAPSE_KEY_DRIVER_SELECT="driver_select";
    public final static String COLLAPSE_KEY_DISTANCE_REQUEST="distance_request";
    public static final String COLLAPSE_KEY_FINISH = "finish_service";

    //BROADCAST
    public final static String BROADCAST_ACTION="co.com.acktos.ubimoviltaxista";
    public final static String BROADCAST_ACTION_FINISH="co.com.acktos.ubimoviltaxista.FINISH";


    // ALARM
    public static final int REQUEST_ALARM_PENDING=111;// Alarm pending intent ID
    public static final long MINIMUM_ALARM_INTERVAL=1000*60*5; // five minutes
    //public static final long MINIMUM_ALARM_INTERVAL=1000*30; // five minutes


    //INTERNAL STORAGE FILES
    public static final String  FILE_DRIVER_PROFILE="driver_profile.json";



    // API END POINTS
    public enum API{

        DRIVER_LOGIN("http://192.34.58.49/login_driver/"),
        GET_CARS("http://192.34.58.49/find_cars_driver/"),
        GET_SERVICES("http://192.34.58.49/get_services_driver/"),
        UPDATE_SERVICE_STATE("http://192.34.58.49/update_state/"),
        SET_CAR("http://192.34.58.49/assign_car/"),
        ASSIGN_SERVICE("http://192.34.58.49/assign_service/"),
        REGISTER_GCM_ID("http://192.34.58.49/update_mobileid/"),
        DRIVER_ALARM("http://192.34.58.49/driver_alarm/"),
        ADD_CREDIT("http://192.34.58.49/add_credit/");


        private final String url;

        API (String uri){
            url=uri;
        }

        public String getUrl(){
            return url;
        }
    }

    // RESPONSE API CODES
    public static final String SUCCESS_CODE="200";
    public static final String FAILED_CODE="402";
    public static final String ERROR_CODE="400";
    public static final String FINISH_WAKE_UP_CODE="676";
    public static final String SERVICE_ALREADY_TAKEN="300";
    public static final String RESULT_CODE="result_code";
    public static final String TOKEN="6f0e37c25064fa258b428ebb6cf79553";


    //API KEYS
    public final static String KEY_ID="id";
    public final static String KEY_PASSWORD="pswrd";
    public final static String KEY_RESPONSE="response";
    public final static String KEY_CODE="code";
    public final static String KEY_MESSAGE="message";
    public final static String KEY_CAR="car";
    public final static String KEY_FIELDS="fields";
    public final static String KEY_ENCRYPT="encrypt";
    public final static String KEY_EMAIL="email";
    public final static String KEY_PSWRD="pswrd";
    public final static String KEY_SERVICE_ID="id";
    public final static String KEY_SERVICE="service";
    public final static String KEY_DRIVER="driver";
    public final static String KEY_DISTANCE="distance";
    public static final String KEY_STATE = "state";
    public final static String KEY_DRIVER_ID="driver_id";
    public final static String KEY_GCM_ID="register_number";
    public final static String KEY_PANIC="panic";


    //FIREBASE

    public final static String KEY_LAST_LOCATION="lastLocation";
    public static final String KEY_LAST_EVENT = "lastEvent";
    public final static String KEY_CITY="city";
    public final static String KEY_UPDATE_DATE="updateDate";
    public final static String TABLE_DRIVERS="drivers";
    public final static String TABLE_SERVICES="services";
    public final static String TABLE_ALARMS="alarms";


    //STATES

    public final static String STATE_ACCEPTED="Accepted";
    public final static String STATE_ON_THE_WAY="On the way";
    public final static String STATE_ARRIVED="Arrived";
    public final static String STATE_ON_BOARD="On Board";
    public final static String STATE_COMPLETED="Completed";
    public final static String STATE_CANCELLED="Cancelled";



}
