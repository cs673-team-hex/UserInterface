package SendingData;


import UI.HomePage;
import UI.Login;
import UI.Registration;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Main {

    public static void main(String args[]) {
        try {
         SSLClient.initConnect();
         } catch (Exception e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         } 
        Login l = new Login();
        l.setSize(800, 530);
        l.setLocation(300, 200);
        l.setVisible(true);
        
        /*HomePage h = null;
        try {
            h = new HomePage();
        } catch (JSONException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        h.setSize(810,460);
        h.setLocation(0,0);
        h.setVisible(true);*/
        

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
