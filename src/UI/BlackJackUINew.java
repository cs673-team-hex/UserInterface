/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import GameInfo.Player;
import GameInfo.Room;
import JudgeStatus.JudgeStatus;
import Log.Log;
import PokerGame.BlackJackRule;
import SendingData.SSLClient;
import TimerTaskGame.Ask4RoundInfo;
import static TimerTaskGame.Ask4RoundInfo.KEY_ROUNDINFO;
import TimerTaskGame.Ask4RoundInfo.PlayerInfo;
import static TimerTaskGame.Ask4RoundInfo.STATUS;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wei
 */
public class BlackJackUINew extends javax.swing.JFrame {

    /**
     * Creates new form BlackJackUINew
     */
    ArrayList<JLabel> AICardList = new ArrayList<JLabel>();
    ArrayList<JLabel> YourCardList = new ArrayList<JLabel>();
    ArrayList<JLabel> P1CardList = new ArrayList<JLabel>();
    ArrayList<JLabel> P2CardList = new ArrayList<JLabel>();
    ArrayList<JButton> YourActionList = new ArrayList<JButton>();
    /*BlackJackPlay game;
     BlackJackPlayRound round;*/
    Timer timer_roundinfo;
    Ask4RoundInfo task = new Ask4RoundInfo();

    static int START_TIME = 100;
    static int PERIOD = 1000;

    private int myposition = 0;
    PlayerInfo[] Players;
    PlayerInfo[] Old_Players = null;

    private JSONObject response;
    private int status;

    private int current_sum = 0;
    private int current_index = 0;

    private int userid;
    private int roomid;
    public static int KEY_HIT = 1;
    public static int KEY_STAND = 2;
    public static int KEY_DOUBLE = 3;
    public static int KEY_SURREND = 4;

    private boolean RoundEnd = false;
    private int left = 0;

    public BlackJackUINew() {
        //System.out.println("Current Player ID " + Player.GetPlayer().GetUserId());
        initComponents();
        RoundEnd = false;
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                timer_roundinfo.cancel();
                CreateRoom createRoom = new CreateRoom();
                createRoom.setVisible(true);
                try {
                    Log.getInstance().WriteLog();
                } catch (IOException ex) {
                    Logger.getLogger(BlackJackUINew.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        AICardList.add(jAIcard1);
        AICardList.add(jAIcard2);
        AICardList.add(jAIcard3);
        AICardList.add(jAIcard4);
        AICardList.add(jAIcard5);

        YourCardList.add(jPcard1);
        YourCardList.add(jPcard2);
        YourCardList.add(jPcard3);
        YourCardList.add(jPcard4);
        YourCardList.add(jPcard5);

        P1CardList.add(jP1Card1);
        P1CardList.add(jP1Card2);
        P1CardList.add(jP1Card3);
        P1CardList.add(jP1Card4);
        P1CardList.add(jP1Card5);

        P2CardList.add(jP2Card1);
        P2CardList.add(jP2Card2);
        P2CardList.add(jP2Card3);
        P2CardList.add(jP2Card4);
        P2CardList.add(jP2Card5);

        YourActionList.add(jHit);
        YourActionList.add(jStand);
        YourActionList.add(jSurrender);
        YourActionList.add(jDouble);

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        timer_roundinfo = new java.util.Timer(true);

        task.setOnRefreshListner(new Ask4RoundInfo.OnRefreshListener() {

            @Override
            public void onRefresh(String test) {
                Players = task.getPlayerInfo();
                int first_player = 0;
                for(int j = 0; j < Players.length; j++){
                    if(Players[j].getposition() == 1){
                        first_player = j;
                    }
                }
                findmyposition();
                if (Players[first_player].getuserstatus() == 1) {
                    InitialBoardsBetweenRounds();
                    jBet.setText(String.valueOf(task.get_wager()));
                    for (int i = 0; i < 5; i++) {
                        AICardList.get(i).setIcon(null);
                        YourCardList.get(i).setIcon(null);
                        P1CardList.get(i).setIcon(null);
                        P2CardList.get(i).setIcon(null);
                    }
                }
                if (Players[myposition].getuserstatus() == 0) {
                    TerminateControlOfPlayer();
                } else if (Players[myposition].getuserstatus() == 1) {
                    RestoreControlOfPlayer();
                } else if (Players[myposition].getuserstatus() == 2) {
                    DisableDouble();
                    DisableSurrender();
                    //RefreshNumOfPlayerHand(Players[myposition]);
                } else if (Players[myposition].getuserstatus() == 3) {
                    if (BlackJackRule.AmIFiveDragon(Players[myposition])) {
                        jPFiveDragon.setVisible(true);
                    }
                    if (BlackJackRule.AmIBlackJack(Players[myposition])) {
                        jPBlackJack.setVisible(true);
                    }
                    //RefreshNumOfPlayerHand(Players[myposition]);
                    TerminateControlOfPlayer();
                } else if (Players[myposition].getuserstatus() == 4) {
                    //RefreshNumOfPlayerHand(Players[myposition]);
                    jBet.setText(String.valueOf(task.get_wager()*2));
                    TerminateControlOfPlayer();
                }else if(Players[myposition].getuserstatus() == 5){
                    jBet.setText(String.valueOf(task.get_wager()/2));
                    TerminateControlOfPlayer();
                    //RefreshWhenYouLose();
                }
                if (Players[0].getuserstatus() == 3 || Players[0].getuserstatus() == 4 || Players[0].getuserstatus() == 5) {
                    if (Player.GetPlayer().GetIsCreator() == true) {
                        AskForNextRound();
                    }

                    RefreshNumOfPlayerHand(Players[0]);
                    RoundEnd = true;
                    if (!BlackJackRule.GetBlackJackResult(Players[myposition], Players[0]) && Players[myposition].getuserstatus() != 5) {
                        RefreshWhenYouWin();
                    } else {
                        RefreshWhenYouLose();
                    }
                }
                SendCardsToAllPlayer(RoundEnd);
            }

        });
        timer_roundinfo.schedule(task, START_TIME, PERIOD);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jBet = new javax.swing.JTextField();
        jSurrender = new javax.swing.JButton();
        jDouble = new javax.swing.JButton();
        jStand = new javax.swing.JButton();
        jHit = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPScore = new javax.swing.JTextField();
        jPMoney = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPLose = new javax.swing.JLabel();
        jPWin = new javax.swing.JLabel();
        jNextRound = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jAIScore = new javax.swing.JTextField();
        jRound = new javax.swing.JLabel();
        jAllCards = new javax.swing.JPanel();
        AICards = new javax.swing.JPanel();
        jAIcard5 = new javax.swing.JLabel();
        jAIcard4 = new javax.swing.JLabel();
        jAIcard3 = new javax.swing.JLabel();
        jAIcard2 = new javax.swing.JLabel();
        jAIcard1 = new javax.swing.JLabel();
        MyCards = new javax.swing.JPanel();
        jPcard5 = new javax.swing.JLabel();
        jPcard4 = new javax.swing.JLabel();
        jPcard3 = new javax.swing.JLabel();
        jPcard2 = new javax.swing.JLabel();
        jPcard1 = new javax.swing.JLabel();
        jP1Cards = new javax.swing.JPanel();
        jP1Card5 = new javax.swing.JLabel();
        jP1Card4 = new javax.swing.JLabel();
        jP1Card3 = new javax.swing.JLabel();
        jP1Card2 = new javax.swing.JLabel();
        jP1Card1 = new javax.swing.JLabel();
        jP2Cards = new javax.swing.JPanel();
        jP2Card5 = new javax.swing.JLabel();
        jP2Card4 = new javax.swing.JLabel();
        jP2Card3 = new javax.swing.JLabel();
        jP2Card2 = new javax.swing.JLabel();
        jP2Card1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPFiveDragon = new javax.swing.JLabel();
        jPBlackJack = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setLayout(null);

        jPanel2.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Bet:");

        jBet.setEditable(false);
        jBet.setFocusable(false);

        jSurrender.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jSurrender.setText("Surrender");
        jSurrender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSurrenderActionPerformed(evt);
            }
        });

        jDouble.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jDouble.setText("Double");
        jDouble.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDoubleActionPerformed(evt);
            }
        });

        jStand.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jStand.setText("Stand");
        jStand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStandActionPerformed(evt);
            }
        });

        jHit.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jHit.setText("Hit");
        jHit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jHitActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Player Score:");

        jPScore.setEditable(false);
        jPScore.setFocusable(false);

        jPMoney.setEditable(false);
        jPMoney.setFocusable(false);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setText("Player Money");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jHit, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jStand, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jDouble, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addComponent(jSurrender)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jBet, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPScore, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(jPMoney))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jHit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jStand, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDouble, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSurrender, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBet, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPScore, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 480, 790, 102);

        jPanel5.setOpaque(false);
        jPanel5.setLayout(null);

        jPLose.setFont(new java.awt.Font("Segoe Script", 1, 48)); // NOI18N
        jPLose.setForeground(new java.awt.Color(255, 255, 255));
        jPLose.setText("YOU LOSE!!!");
        jPanel5.add(jPLose);
        jPLose.setBounds(30, 0, 320, 90);

        jPWin.setFont(new java.awt.Font("Segoe Script", 1, 48)); // NOI18N
        jPWin.setForeground(new java.awt.Color(255, 255, 255));
        jPWin.setText("YOU WIN!!!");
        jPanel5.add(jPWin);
        jPWin.setBounds(30, 10, 340, 80);

        jPanel1.add(jPanel5);
        jPanel5.setBounds(190, 340, 400, 120);

        jNextRound.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jNextRound.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/img/nextround.png"))); // NOI18N
        jNextRound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jNextRoundActionPerformed(evt);
            }
        });
        jPanel1.add(jNextRound);
        jNextRound.setBounds(640, 400, 130, 41);

        jPanel3.setOpaque(false);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("AI Score:");
        jLabel8.setToolTipText("");
        jLabel8.setFocusable(false);

        jAIScore.setEditable(false);
        jAIScore.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jAIScore.setEnabled(false);
        jAIScore.setFocusable(false);
        jAIScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAIScoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jAIScore, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jAIScore, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 3, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);
        jPanel3.setBounds(610, 10, 190, 50);

        jRound.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        jPanel1.add(jRound);
        jRound.setBounds(680, 420, 100, 40);

        jAllCards.setFocusable(false);
        jAllCards.setOpaque(false);
        jAllCards.setLayout(null);

        AICards.setOpaque(false);
        AICards.setLayout(null);
        AICards.add(jAIcard5);
        jAIcard5.setBounds(144, 0, 73, 90);
        AICards.add(jAIcard4);
        jAIcard4.setBounds(108, 0, 73, 90);
        AICards.add(jAIcard3);
        jAIcard3.setBounds(72, 0, 73, 90);
        AICards.add(jAIcard2);
        jAIcard2.setBounds(36, 0, 73, 90);
        AICards.add(jAIcard1);
        jAIcard1.setBounds(0, 0, 73, 90);

        jAllCards.add(AICards);
        AICards.setBounds(190, 10, 218, 90);

        MyCards.setOpaque(false);
        MyCards.setLayout(null);
        MyCards.add(jPcard5);
        jPcard5.setBounds(144, 0, 73, 90);
        MyCards.add(jPcard4);
        jPcard4.setBounds(108, 0, 73, 90);
        MyCards.add(jPcard3);
        jPcard3.setBounds(72, 0, 72, 90);
        MyCards.add(jPcard2);
        jPcard2.setBounds(36, 0, 73, 90);
        MyCards.add(jPcard1);
        jPcard1.setBounds(0, 0, 73, 90);

        jAllCards.add(MyCards);
        MyCards.setBounds(190, 120, 218, 90);
        MyCards.getAccessibleContext().setAccessibleDescription("");

        jP1Cards.setFocusable(false);
        jP1Cards.setOpaque(false);
        jP1Cards.setLayout(null);
        jP1Cards.add(jP1Card5);
        jP1Card5.setBounds(0, 112, 73, 90);
        jP1Cards.add(jP1Card4);
        jP1Card4.setBounds(0, 84, 73, 90);
        jP1Cards.add(jP1Card3);
        jP1Card3.setBounds(0, 56, 73, 90);
        jP1Cards.add(jP1Card2);
        jP1Card2.setBounds(0, 28, 73, 90);
        jP1Cards.add(jP1Card1);
        jP1Card1.setBounds(0, 0, 73, 90);

        jAllCards.add(jP1Cards);
        jP1Cards.setBounds(10, 5, 73, 210);

        jP2Cards.setFocusable(false);
        jP2Cards.setOpaque(false);
        jP2Cards.setLayout(null);
        jP2Cards.add(jP2Card5);
        jP2Card5.setBounds(0, 112, 73, 90);
        jP2Cards.add(jP2Card4);
        jP2Card4.setBounds(0, 84, 73, 90);
        jP2Cards.add(jP2Card3);
        jP2Card3.setBounds(0, 56, 73, 90);
        jP2Cards.add(jP2Card2);
        jP2Card2.setBounds(0, 28, 73, 90);
        jP2Cards.add(jP2Card1);
        jP2Card1.setBounds(0, 0, 73, 90);

        jAllCards.add(jP2Cards);
        jP2Cards.setBounds(515, 5, 73, 210);

        jPanel1.add(jAllCards);
        jAllCards.setBounds(100, 120, 590, 220);

        jPanel4.setFocusable(false);
        jPanel4.setOpaque(false);
        jPanel4.setLayout(null);

        jPFiveDragon.setFont(new java.awt.Font("Segoe Script", 1, 48)); // NOI18N
        jPFiveDragon.setForeground(new java.awt.Color(255, 255, 255));
        jPFiveDragon.setText("  Five Dragons!!!");
        jPanel4.add(jPFiveDragon);
        jPFiveDragon.setBounds(10, 0, 440, 100);

        jPBlackJack.setFont(new java.awt.Font("Segoe Script", 1, 48)); // NOI18N
        jPBlackJack.setForeground(new java.awt.Color(255, 255, 255));
        jPBlackJack.setText("    BlackJack!!!");
        jPanel4.add(jPBlackJack);
        jPBlackJack.setBounds(10, 0, 440, 100);

        jPanel1.add(jPanel4);
        jPanel4.setBounds(140, 0, 460, 110);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PokerCardImage/Table.jpg"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 800, 590);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 798, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void DoSomethingAtBegin() throws MessagingException {
        InitialBoardsBetweenRounds();
    }

    public void InitialBoardsBetweenRounds() {
        jPFiveDragon.setVisible(false);
        jPBlackJack.setVisible(false);
        jPLose.setVisible(false);
        jPWin.setVisible(false);
        jNextRound.setVisible(false);
        RoundEnd = false;

        for (int i = 0; i < task.getPlayernum(); i++) {
            RefreshNumOfPlayerHand(Players[i]);
            Players[i].set_current_index(0);
            current_sum = 0;
        }
    }

    public void findmyposition() {
        System.out.println("Player numbers " + Players.length);
        for (int i = 0; i < Players.length; i++) {
            if (Players[i].getuserid() == Player.GetPlayer().GetUserId()) {
                myposition = i;
                return;
            }
            //System.out.println("Player ID" + Players[i].getuserid());
            //System.out.println("Current Player" + Player.GetPlayer().GetUserId());
        }

    }

    public void RefreshMoneyOfPlayer() {
        //TODO should get money from DB,
        jPMoney.setText(String.valueOf(Player.GetPlayer().GetCredit()));
    }

    public void RefreshNumOfPlayerHand(PlayerInfo player) {
        int sum = 0;
        if (player != null) {
            sum = BlackJackRule.GetMaxValueOfHand(player);
            player.set_max_value(sum);
            if (player.getuserid() == Player.GetPlayer().GetUserId()) {
                if (sum == -1) {
                    jPScore.setText("BUST!!");
                } else {
                    jPScore.setText(String.valueOf(sum));
                }
            } else if (player.getuserid() == 0) {
                if (sum == -1) {
                    jAIScore.setText("BUST!!");
                } else {
                    jAIScore.setText(String.valueOf(sum));
                }
            }
            /*for (int i = 0; i < player.getcardsnum(); i++) {
             if (player.getCardsInfo()[i].getnumber() < 14 || player.getCardsInfo()[i].getnumber() > 10) {
             sum = sum + 10;
             } else {
             sum = sum + player.getCardsInfo()[i].getnumber();
             }
             if (player.getuserid() == Player.GetPlayer().GetUserId()) {
             if (sum > 21) {
             jPScore.setText("BUST!!");
             } else { 
             jPScore.setText(String.valueOf(sum));
             }
             } else if (player.getuserid() == 0) {
             if (sum > 21) {
             jAIScore.setText("BUST!!");
             } else {
             jAIScore.setText(String.valueOf(sum));
             }
             }
             }*/
        }
    }

    public void AskForNextRound() {
        jNextRound.setVisible(true);
    }

    public void GameEndProcedure() throws MessagingException {
        //String strTime = String.valueOf(System.currentTimeMillis());
        //Log.getInstance().Log(1, "Player ID:" + strTime);
        //RefreshLog(Log.getInstance().getLog());

        //JOptionPane.showMessageDialog(this, "Press Ok to Send E-Mail(Takes 20s),Your ID: " + strTime + "\n", "Thanks!", JOptionPane.INFORMATION_MESSAGE);
        //Log.getInstance().MailLog();
        //Log.getInstance().ClearLog();
        // CleanLog();
        //New Game!!!
        DoSomethingAtBegin();
    }

    public void TerminateControlOfPlayer() {
        for (JButton button : YourActionList) {
            button.setEnabled(false);
        }
    }

    public void RestoreControlOfPlayer() {
        for (JButton button : YourActionList) {
            button.setEnabled(true);
        }
    }

    public String getCardStr(int color, int number) {
        switch (color) {
            case 1:
                return "Spade" + number;
            case 2:
                return "Heart" + number;
            case 3:
                return "Club" + number;
            case 4:
                return "Diamond" + number;
        }
        System.out.println("Color: " + color);
        return "Back";
    }

    public void SendCardToPosition(PlayerInfo Player, boolean roundend) {
        //System.out.println("RoundEnd" + RoundEnd);
        //System.out.println("Player" + Player.getuserid() + " Position " + Player.getposition());
        //System.out.println("MyPosition " + myposition);
        //System.out.println("Position " + Player.getposition());
        current_index = Player.get_current_index();
        //System.out.println("Index " + current_index);
        //System.out.println("Cards " + Player.getCardsInfo()[current_index].getcolor() + Player.getCardsInfo()[current_index].getnumber() + Player.getCardsInfo()[current_index].gethide());
        if (Player.getposition() == Players[myposition].getposition()) {
            String add = "/PokerCardImage/" + getCardStr(Player.getCardsInfo()[current_index].getcolor(), Player.getCardsInfo()[current_index].getnumber()) + ".png";
            System.out.println(add);
            YourCardList.get(current_index).setIcon(new javax.swing.ImageIcon(getClass().getResource(add)));
            /*if (Player.getCardsInfo()[current_index].gethide() == 1) {
             YourCardList.get(current_index).setIcon(new javax.swing.ImageIcon(getClass().getResource("/PokerCardImage/Back.png")));
             } else if (Player.getCardsInfo()[current_index].gethide() == 0) {
             String add = /"/PokerCardImage/" + getCardStr(Player.getCardsInfo()[current_index].getcolor(), Player.getCardsInfo()[current_index].getcolor()) + ".png";
             System.out.println(add);
             YourCardList.get(current_index).setIcon(new javax.swing.ImageIcon(getClass().getResource(add)));
             }*/
        } else if (Player.getposition() == 0) {
            if ((Player.getCardsInfo()[current_index].gethide() == 1) && (roundend == false)) {
                AICardList.get(current_index).setIcon(new javax.swing.ImageIcon(getClass().getResource("/PokerCardImage/Back.png")));
            } else if (Player.getCardsInfo()[current_index].gethide() == 0 || roundend == true) {
                String add = "/PokerCardImage/" + getCardStr(Player.getCardsInfo()[current_index].getcolor(), Player.getCardsInfo()[current_index].getnumber()) + ".png";
                //System.out.println("Color " + Player.getCardsInfo()[0].getcolor());
                //System.out.println("Number " + Player.getCardsInfo()[0].getnumber());
                //System.out.println(add);
                AICardList.get(current_index).setIcon(new javax.swing.ImageIcon(getClass().getResource(add)));
            }
        } else if (Player.getposition() - 1 == (Players[myposition].getposition() + 1) % 3) {
            //System.out.println("RoundEnd" + RoundEnd);
            if (Player.getCardsInfo()[current_index].gethide() == 1 && roundend == false) {
                P1CardList.get(current_index).setIcon(new javax.swing.ImageIcon(getClass().getResource("/PokerCardImage/Back.png")));
            } else if (Player.getCardsInfo()[current_index].gethide() == 0 || roundend == true) {
                String add = "/PokerCardImage/" + getCardStr(Player.getCardsInfo()[current_index].getcolor(), Player.getCardsInfo()[current_index].getnumber()) + ".png";
                //System.out.println("Reach");
                P1CardList.get(current_index).setIcon(new javax.swing.ImageIcon(getClass().getResource(add)));
            }
        } else if (Player.getposition() - 1 == Players[myposition].getposition() % 3) {
            if (Player.getCardsInfo()[current_index].gethide() == 1 && roundend == false) {
                P2CardList.get(current_index).setIcon(new javax.swing.ImageIcon(getClass().getResource("/PokerCardImage/Back.png")));
            } else if (Player.getCardsInfo()[current_index].gethide() == 0 || roundend == true) {
                String add = "/PokerCardImage/" + getCardStr(Player.getCardsInfo()[current_index].getcolor(), Player.getCardsInfo()[current_index].getnumber()) + ".png";
                P2CardList.get(current_index).setIcon(new javax.swing.ImageIcon(getClass().getResource(add)));
            }
        }
        current_index++;
        Player.set_current_index(current_index);

    }

    public void SendCardsToAllPlayer(boolean roundend) {
        for (int i = 0; i < Players.length; i++) {
            //if (Old_Players == null || Players[i].Is_CardsUpdated(Old_Players[i], Players[i])) {
            current_sum = Players[i].getcardsnum();
            //System.out.println("Cards sum " + Players[i].getcardsnum());
            while (Players[i].get_current_index() < current_sum) {
                SendCardToPosition(Players[i], roundend);
            }
            //}
        }
        Old_Players = Players;
    }

    /*if (nPosition > 5 || nPosition < 1) {
     return;
     }

     JLabel jAILabel = new JLabel();
     String cardAddress = "";
     if (!bFaceup) {
     cardAddress = "/PokerCardImage/Back.png";
     }
     switch (nPosition) {
     case 1:
     jAILabel = bAI ? jAIcard1 : jPcard1;
     break;
     case 2:
     jAILabel = bAI ? jAIcard2 : jPcard2;
     break;
     case 3:
     jAILabel = bAI ? jAIcard3 : jPcard3;
     break;
     case 4:
     jAILabel = bAI ? jAIcard4 : jPcard4;
     break;
     case 5:
     jAILabel = bAI ? jAIcard5 : jPcard5;
     break;
     default:
     throw new AssertionError();
     }
     //jAICard1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/PokerCardImage/cic.png")));
     if (bFaceup) {
     cardAddress = "/PokerCardImage/" + card.getCardNameFromPNG() + ".png";
     }

     jAILabel.setIcon(
     new javax.swing.ImageIcon(getClass().getResource(cardAddress)));*/
    public void RefreshWhenYouLose() {
        jPLose.setVisible(true);
    }

    public void RefreshWhenYouWin() {
        jPWin.setVisible(true);
    }

    public void DisableHit() {
        jHit.setEnabled(false);
    }

    public void DisableDouble() {
        jDouble.setEnabled(false);
    }

    public void DisableSurrender() {
        jSurrender.setEnabled(false);
    }

    public void DisableStand() {
        jStand.setEnabled(false);
    }


    private void jAIScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAIScoreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jAIScoreActionPerformed

    private void jHitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jHitActionPerformed
        // TODO add your handling code here:
        /*if (round == null) {
         return;
         }
         if (round.isPlayerPhase()) {
         if (game.getPlayer().AmIDouble()) {
         //GetOneCardAndStand
         round.PlayerHit();
         //Should not do this, but I am lazy.
         jStandActionPerformed(null);
         return;
         }

         if (!BlackJackRule.AmIBust(game.getPlayer())) {
         round.PlayerHit();
         DisableDouble();
         DisableSurrender();
         //Check GameStatus
         if (BlackJackRule.AmIBust(game.getPlayer())) {
         //Should not do this, but I am lazy.
         jStandActionPerformed(null);
         }
         if (BlackJackRule.AmIFiveDragon(game.getPlayer())) {
         //Should not do this, but I am lazy.3
         .
         jStandActionPerformed(null);
         }
         }
         }*/
        Perform_Action(KEY_HIT);
        task.run();
        RefreshNumOfPlayerHand(Players[myposition]);
    }//GEN-LAST:event_jHitActionPerformed

    public JSONObject getMessgeStart() {
        JSONObject test = new JSONObject();
        try {
            userid = Player.GetPlayer().GetUserId();
            roomid = Room.Getroom().GetRoomID();
            test.put("opt", "startgame");
            test.put("userid", userid);
            JSONObject info = new JSONObject();
            info.put("roomid", roomid);
            test.put("info", info);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(test);
        return test;
    }

    public JSONObject getMessgeAction(int key) {
        JSONObject test = new JSONObject();
        userid = Player.GetPlayer().GetUserId();
        try {
            test.put("opt", "blackjack");
            test.put("userid", userid);
            JSONObject info = new JSONObject();
            roomid = Room.Getroom().GetRoomID();
            info.put("roomid", roomid);
            userid = Player.GetPlayer().GetUserId();
            info.put("opt", key);
            test.put("info", info);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(test);
        return test;
    }

    public void Perform_Action(int key) {
        try {
            response = SSLClient.postMessage(getMessgeAction(key));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(response);
        try {
            status = response.getInt(STATUS);
        } catch (JSONException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
        //static method
        if (JudgeStatus.OutputStatus(status) == false) {
            return;
        }
    }

    private void jNextRoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jNextRoundActionPerformed
        // TODO add your handling code here:
        try {
            response = SSLClient.postMessage(getMessgeStart());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(response);
        try {
            status = response.getInt(STATUS);
        } catch (JSONException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }
        //static method
        if (JudgeStatus.OutputStatus(status) == false) {
            return;
        }

        task.run();
    }//GEN-LAST:event_jNextRoundActionPerformed

    private void jStandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStandActionPerformed
        Perform_Action(KEY_STAND);
        task.run();
        RefreshNumOfPlayerHand(Players[myposition]);
    }//GEN-LAST:event_jStandActionPerformed


    private void jSurrenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSurrenderActionPerformed
        
        Perform_Action(KEY_SURREND);
        task.run();
        RefreshNumOfPlayerHand(Players[myposition]);
    }//GEN-LAST:event_jSurrenderActionPerformed

    private void jDoubleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDoubleActionPerformed
        // TODO add your handling code here:
        
        Perform_Action(KEY_DOUBLE);
        task.run();
        RefreshNumOfPlayerHand(Players[myposition]);
    }//GEN-LAST:event_jDoubleActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BlackJackUINew.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BlackJackUINew.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BlackJackUINew.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BlackJackUINew.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BlackJackUINew ui = new BlackJackUINew();
                ui.setVisible(true);
                try {
                    ui.DoSomethingAtBegin();

                } catch (MessagingException ex) {
                    Logger.getLogger(BlackJackUINew.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AICards;
    private javax.swing.JPanel MyCards;
    private javax.swing.JTextField jAIScore;
    private javax.swing.JLabel jAIcard1;
    private javax.swing.JLabel jAIcard2;
    private javax.swing.JLabel jAIcard3;
    private javax.swing.JLabel jAIcard4;
    private javax.swing.JLabel jAIcard5;
    private javax.swing.JPanel jAllCards;
    private javax.swing.JTextField jBet;
    private javax.swing.JButton jDouble;
    private javax.swing.JButton jHit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JButton jNextRound;
    private javax.swing.JLabel jP1Card1;
    private javax.swing.JLabel jP1Card2;
    private javax.swing.JLabel jP1Card3;
    private javax.swing.JLabel jP1Card4;
    private javax.swing.JLabel jP1Card5;
    private javax.swing.JPanel jP1Cards;
    private javax.swing.JLabel jP2Card1;
    private javax.swing.JLabel jP2Card2;
    private javax.swing.JLabel jP2Card3;
    private javax.swing.JLabel jP2Card4;
    private javax.swing.JLabel jP2Card5;
    private javax.swing.JPanel jP2Cards;
    private javax.swing.JLabel jPBlackJack;
    private javax.swing.JLabel jPFiveDragon;
    private javax.swing.JLabel jPLose;
    private javax.swing.JTextField jPMoney;
    private javax.swing.JTextField jPScore;
    private javax.swing.JLabel jPWin;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel jPcard1;
    private javax.swing.JLabel jPcard2;
    private javax.swing.JLabel jPcard3;
    private javax.swing.JLabel jPcard4;
    private javax.swing.JLabel jPcard5;
    private javax.swing.JLabel jRound;
    private javax.swing.JButton jStand;
    private javax.swing.JButton jSurrender;
    // End of variables declaration//GEN-END:variables
}
