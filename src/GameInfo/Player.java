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
public class Player {
    private double Balance;
    private String NickName;
    private int Rank;
    private double Credit;
    private int Userid;
    private double Factor1;
    private double Factor2;
    private double Factor3;
    private boolean isCreator = false;
    
    private static Player player = null; 
    
    public static void initial(double balance, String nickname, int rank, int credit, int userid, double factor1, double factor2, double factor3){
        player = new Player(balance, nickname, rank, credit, userid, factor1, factor2, factor3);
    }
    
    public static Player GetPlayer(){
        return player;
    }
    
    public void SetBalance(double balance){
        this.Balance = balance;
    }
    
    public double GetBalance(){
        return this.Balance;
    }
    
    public void SetNickName(String nickname){
        this.NickName = nickname;
    }
    
    public String GetNickName(){
        return this.NickName;
    }
    
    public void SetRank(int rank){
        this.Rank = rank;
    }
    
    public int GetRank(){
        return this.Rank;
    }
    
    public void SetCredit(double credit){
        this.Credit = credit;
    }
    
    public double GetCredit(){
        return this.Credit;
    }
    
    public void SetUserId(int userid){
        this.Userid = userid;
    }
    
    public int GetUserId(){
        return this.Userid;
    }
    
    public void SetFactor1(double factor1){
        this.Factor1 = factor1;
    }
    
    public double GetFactor1(){
        return this.Factor1;
    }
     
    public void SetFactor2(double factor2){
        this.Factor1 = factor2;
    }
    
    public double GetFactor2(){
        return this.Factor2;
    }
    
    public void SetFactor3(double factor3){
        this.Factor1 = factor3;
    }
    
    public double GetFactor3(){
        return this.Factor3;
    }
            
    public void SetIsCreator(boolean iscreator){
        isCreator = iscreator;
    }
    
    public boolean GetIsCreator(){
        return this.isCreator;
    }
    
    private Player(double balance, String nickname, int rank, int credit, int userid, double factor1, double factor2, double factor3){
        this.Balance = balance;
        this.NickName = nickname;
        this.Rank = rank;
        this.Credit = credit;
        this.Userid = userid;
        this.Factor1 = factor1;
        this.Factor2 = factor2;
        this.Factor3 = factor3;
    }
}
