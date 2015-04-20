/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimerTaskGame;

import GameInfo.Player;
import JudgeStatus.JudgeStatus;
import SendingData.SSLClient;
import UI.Registration;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wei
 */
public class Ask4RoomList extends TimerTask {

    public interface OnRefreshListener {

        public void onRefresh(String test);
    }

    private OnRefreshListener onRefreshListener = null;

    public void setOnRefreshListner(OnRefreshListener listener) {
        onRefreshListener = listener;
    }

    private int page = 1;
    private int num = 10;
    private int type = 1;
    private JSONObject response;
    public static String KEY_RES = "result";
    public static String KEY_ROOMS = "rooms";
    public static String KEY_TITLE = "title";
    public static String KEY_USERNAME = "nickname";
    public static String KEY_GAMETYPE = "type";
    public static String KEY_CURRENTNUMBER = "cnumber";
    public static String KEY_MAXNUMBER = "mnumber";
    public static String KEY_ROOMID = "roomid";
    public static String KEY_WAGER = "wager";
    public static int ROOMSPACE = 10;
    
    private String[] room_title = new String[num];
    private String[] creator_name = new String[num];
    private String[] room_name = new String[num];
    private int[] room_type = new int[num];
    private int game_type;
    private String[] currentmax = new String[num];
    private int[] room_max = new int[num];
    public int[] room_wager = new int[num];
    private int currentnumber;
    private int max;
    private int room_number;
    private int userid;
    private int roomid;
    private int[] roomids = new int[ROOMSPACE];
    private JSONArray roominfo;

    private int status;
    public static String STATUS = "status";

    @Override
    public void run() {
        //JSONObject response = null;
        try {
            response = SSLClient.postMessage(getMessge());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            status = response.getInt(STATUS);
        } catch (JSONException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
        //static method
        if(JudgeStatus.OutputStatus(status) == false){
            return;
        }

        //System.out.println(response);
        JSONObject result = new JSONObject();
        try {
            result = response.getJSONObject(KEY_RES);
            // System.out.println(result);
        } catch (JSONException ex) {
            Logger.getLogger(Ask4RoomList.class.getName()).log(Level.SEVERE, null, ex);
        }
        roominfo = new JSONArray();

        roominfo = result.optJSONArray(KEY_ROOMS);
        //System.out.println(roominfo);
        JSONObject room = new JSONObject();
        if (roominfo != null) {
            room_number = roominfo.length();
            //System.out.println(room_number);
            if (roominfo.length() > 0) {
                //System.out.println("ReachA");
                for (int i = 0; i < roominfo.length(); i++) {
                    //System.out.println("ReachB");
                    try {
                        //System.out.println("ReachC");
                        room = roominfo.getJSONObject(i);
                        //System.out.println(room);
                        room_title[i] = room.getString(KEY_TITLE);
                        creator_name[i] = room.getString(KEY_USERNAME);
                        game_type = room.getInt(KEY_GAMETYPE);
                        room_wager[i] = room.getInt(KEY_WAGER);
                        room_type[i] = game_type;
                        roomid = room.getInt(KEY_ROOMID);
                        //System.out.println("GameIds: " + roomid);
                        roomids[i] = roomid;
                        //System.out.println(roomids.length);
                        if (game_type == 1) {
                            room_name[i] = "BlackJack";
                        }
                        currentnumber = room.getInt(KEY_CURRENTNUMBER);
                        max = room.getInt(KEY_MAXNUMBER);
                        room_max[i] = max;
                        currentmax[i] = currentnumber + "/" + max;
                        //System.out.println(room_name[i]+creator_name[i]+room_name[i]+currentmax[i]);
                    } catch (JSONException ex) {
                        Logger.getLogger(Ask4RoomList.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } else {
            room_number = 0;
        }
        
        /*for(int j = 0; j < roomids.length; j++){
            System.out.println(roomids[j]);
        }*/
        
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh("test");
        }
        //System.out.println(roomid);
    }

    public int[] get_roomids(){
        return roomids;
    }
    
    public int get_room_number() {
        return room_number;
    }

    public String[] get_room_title() {
        return room_title;
    }

    public int[] get_room_wager() {
        return room_wager;
    }
    
    public int[] get_room_type() {
        return room_type;
    }
    
    public int[] get_room_max() {
        return room_max;
    }
    
    public String[] get_creator_name() {
        return creator_name;
    }

    public String[] get_room_name() {
        return room_name;
    }

    public String[] get_currentmax() {
        return currentmax;
    }

    /*public JSONObject getresponse(){
     return this.response;
     }*/
    public JSONObject getMessge() {
        JSONObject test = new JSONObject();
        userid = Player.GetPlayer().GetUserId();
        try {
            test.put("opt", "roomlist");
            test.put("userid", userid);
            JSONObject info = new JSONObject();
            info.put("page", page);
            info.put("num", num);
            info.put("type", type);
            userid = Player.GetPlayer().GetUserId();

            test.put("info", info);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(test);
        return test;
    }
}
