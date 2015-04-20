/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameInfo;

/**
 *
 * @author Wei
 */
public class Room {
    private static Room room = null;
    private int RoomID;
    private String Title;
    private int MaxPlayers;
    private int Wager;
    private int GameType;
    
    public static Room Getroom(){
        return room;
    }
    public static void initial(){
        room = new Room(0,null,0,0,0);
    }
    public static void initial(int roomid, String title, int max, int wager, int type){
        room = new Room(roomid, title, max, wager, type);
        
    }
    public void SetRoomID(int roomid){
        this.RoomID = roomid;
    }
    public int GetRoomID(){
        return this.RoomID;
    }
    public void SetTitle(String title){
        this.Title = title;
    }
    public String GetTtile(){
        return this.Title;
    }
    public void SetMaxPlayers(int max){
        this.MaxPlayers = max;
    }
    public int GetMaxPlayers(){
        return this.MaxPlayers;
    }
    public void SetGameType(int type){
        this.GameType = type;
    }
    public int GetGameType(){
        return this.GameType;
    }
    public void SetWager(int wager){
        this.Wager = wager;
    }
    public int GetWager(){
        return this.Wager;
    }
    private Room(int roomid, String title, int max, int wager, int type){
        this.RoomID = roomid;
        this.Title = title;
        this.MaxPlayers = max;
        this.Wager = wager;
        this.GameType = type;
    }
}
