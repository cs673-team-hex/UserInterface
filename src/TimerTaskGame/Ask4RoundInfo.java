/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TimerTaskGame;

import GameInfo.Player;
import GameInfo.Room;
import JudgeStatus.JudgeStatus;
import SendingData.SSLClient;
import static TimerTaskGame.Ask4RoomList.STATUS;
import static TimerTaskGame.Ask4Roominfo.KEY_RES;
import static TimerTaskGame.Ask4Roominfo.KEY_ROOMINFO;
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
public class Ask4RoundInfo extends TimerTask {

    private JSONObject response;
    private int status;
    private int userid;
    private int roomid;

    public static String STATUS = "status";
    public static String KEY_ROUNDINFO = "blackjackroundinfo";
    public static String KEY_RES = "result";
    public static String KEY_MEMBERS = "members";
    public static String KEY_USERID = "userid";
    public static String KEY_POSITION = "position";
    public static String KEY_USERSTATUS = "userstatus";
    public static String KEY_CARDS = "cards";
    public static String KEY_COLOR = "color";
    public static String KEY_NUMBER = "number";
    public static String KEY_HIDE = "hide";
    public static int MAXMEMBER = 4;

    private JSONArray members;
    PlayerInfo[] Players;
    private int player_num;
    
    //private int[] userids = new int[MAXMEMBER];
    //private int[] positions = new int[MAXMEMBER];
    //private int[] userstatus = new int[MAXMEMBER];
    //private JSONArray cards;
    //private int[] positions = new int[MAXMEMBER];

    public interface OnRefreshListener {

        public void onRefresh(String test);
    }

    private OnRefreshListener onRefreshListener = null;

    public void setOnRefreshListner(OnRefreshListener listener) {
        onRefreshListener = listener;
    }
    
    @Override
    public void run() {
        try {
            response = SSLClient.postMessage(getMessge());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(response);
        try {
            status = response.getInt(STATUS);
        } catch (JSONException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
        //static method
        if (JudgeStatus.OutputStatus(status) == false) {
            return;
        }

        JSONObject result = new JSONObject();
        try {
            result = response.getJSONObject(KEY_RES);
            members = result.getJSONArray(KEY_MEMBERS);
        } catch (JSONException ex) {
            Logger.getLogger(Ask4Roominfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONObject member = new JSONObject();
        JSONArray cards_total;
        if (member != null) {
            player_num = members.length();
            Players = new PlayerInfo[members.length()];
            System.out.println("members length " + members.length());
            for (int i = 0; i < members.length(); i++) {
                try {
                    //cards_total = members.getJSONArray(i);
                    member = members.getJSONObject(i);
                    cards_total = member.getJSONArray(KEY_CARDS);
                    Players[i] = new PlayerInfo(cards_total);
                    System.out.println(cards_total);
                    Players[i].setuserid(member.getInt(KEY_USERID));
                    Players[i].setposition(member.getInt(KEY_POSITION));
                    Players[i].setuserstatus(member.getInt(KEY_USERSTATUS));
                } catch (JSONException ex) {
                    Logger.getLogger(Ask4RoundInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh("test");
        }
    }

    public PlayerInfo[] getPlayerInfo(){
        return Players;
    }
    
    public int getPlayernum(){
        return player_num;
    }
    
    public class PlayerInfo {

        final int MAXCARDS = 5;
        private int userid;
        private int position;
        private int userstatus;
        private boolean is_left;
        private boolean is_right;
        private int current_index = 0;
        //private int player_numbers;
        private int cards_num;
        private int max_value;
        CardsInfo[] Cards;

        public int get_current_index(){
            return current_index;
        }
        
        public void set_current_index(int index){
            current_index = index;
        }
        
        public int get_max_value(){
            return max_value;
        }
        
        public void set_max_value(int MaxValue){
            max_value = MaxValue;
        }
        
        public boolean get_is_left(){
            return is_left;
        }
        
        public boolean get_is_right(){
            return is_right;
        }
        
        public void set_is_left(boolean Is_Left){
            is_left = Is_Left;
        }
        
        public void set_is_right(boolean Is_Right){
            is_right = Is_Right;
        }
        
        public boolean Is_CardsUpdated(PlayerInfo Old_playerinfo, PlayerInfo New_playerinfo){
            if(Old_playerinfo.cards_num != New_playerinfo.cards_num || Old_playerinfo.Cards[0].hide != New_playerinfo.Cards[0].hide){
                return true;
            }
            else{
                return false;
            }
        }
        
        public PlayerInfo(JSONArray cards) {
            if(cards == null){return;}
            Cards = new CardsInfo[cards.length()];
            cards_num = cards.length();
            for (int i = 0; i < cards.length(); i++) {
                JSONObject temp;
                try {
                    temp = cards.getJSONObject(i);
                    //System.out.println("Temp " + temp);
                    Cards[i] = new CardsInfo(temp);
                } catch (JSONException ex) {
                    Logger.getLogger(Ask4RoundInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        public class CardsInfo {

            private int color;
            private int number;
            private int hide;
            
            public CardsInfo(JSONObject card) {
                try {
                    System.out.println("Card " + card);
                    color = card.getInt(KEY_COLOR);
                    number = card.getInt(KEY_NUMBER);
                    hide = card.getInt(KEY_HIDE);
                    System.out.println("Color " + color + " Number" + number + " Hide" + hide);
                } catch (JSONException ex) {
                    Logger.getLogger(Ask4RoundInfo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            public void setcolor(int Color) {
                color = Color;
            }

            public void setnumber(int Number) {
                number = Number;
            }

            public void sethide(int Hide) {
                hide = Hide;
            }

            public int getcolor() {
                return color;
            }

            public int getnumber() {
                return number;
            }

            public int gethide() {
                return hide;
            }
        }

        public int getcardsnum(){
            return cards_num;
        }
        
        public CardsInfo[] getCardsInfo() {
            return Cards;
        }

        public void setuserid(int Userid) {
            userid = Userid;
        }

        public void setposition(int Position) {
            position = Position;
        }

        public void setuserstatus(int Userstatus) {
            userstatus = Userstatus;
        }

        public int getuserid() {
            return userid;
        }

        public int getposition() {
            return position;
        }

        public int getuserstatus() {
            return userstatus;
        }
    }

    public JSONObject getMessge() {
        JSONObject test = new JSONObject();
        userid = Player.GetPlayer().GetUserId();
        try {
            test.put("opt", KEY_ROUNDINFO);
            test.put("userid", userid);
            JSONObject info = new JSONObject();
            roomid = Room.Getroom().GetRoomID();
            info.put("roomid", roomid);
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
