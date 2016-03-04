package co.com.acktos.ubimoviltaxista.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Acktos on 11/12/15.
 */
public class ServerResponse {

    private String code;
    private String message;

    public static final String KEY_RESPONSE="response";
    public static final String KEY_RESPONSE_MESSAGE="message";
    public static final String KEY_RESPONSE_CODE="code";

    public ServerResponse(String responseObject){

        try {
            JSONObject jsonObject=new JSONObject(responseObject);


            code=jsonObject.getJSONObject(KEY_RESPONSE).getString(KEY_RESPONSE_CODE);
            message=jsonObject.getJSONObject(KEY_RESPONSE).getString(KEY_RESPONSE_MESSAGE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
