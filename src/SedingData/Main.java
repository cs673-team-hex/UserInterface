
import UI.Login;
import UI.Registration;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

    public static void main(String args[]) {
        /*try {
         SSLClient.initConnect();
         } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         } */
        Login l = new Login();
        l.setSize(816, 570);
        l.setLocation(300, 200);
        l.setVisible(true);

        

        /*try {
         SSLClient.closeConnect();
         } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         }*/
    }

    /*public static JSONObject getMessge(){
     JSONObject test = new JSONObject();
     try {
     test.put("opt", "login");
     JSONObject info = new JSONObject();
     info.put("username", "11111");
     info.put("passwd", "0123456789");
     test.put("info", info);
     } catch (JSONException e) {
     // TODO Auto-generated catch block
     e.printStackTrace();
     }
     System.out.println(test);
     return test;
     }*/
}
