/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import GameInfo.Player;
import GameInfo.Room;
import JudgeStatus.JudgeStatus;
import SendingData.SSLClient;
import TimerTaskGame.Ask4Roominfo;
import static UI.HomePage.START_TIME;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.table.DefaultTableModel;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Wei
 */
public class CreateRoom extends javax.swing.JFrame {

    private int roomid;
    private int userid;

    static int START_TIME = 100;
    static int PERIOD = 1000;

    private int status;
    public static String STATUS = "status";

    private String title;
    private int number;
    private int type;
    private int wager;
    private String game_type;
    private int room_status;
    Timer timer_roominfo;

    /**
     * Creates new form CreateRoom
     */
    public CreateRoom() {
        initComponents();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        /*addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                HomePage homePage;
                try {
                    homePage = new HomePage();
                    homePage.setVisible(true);
                } catch (JSONException ex) {
                    Logger.getLogger(CreateRoom.class.getName()).log(Level.SEVERE, null, ex);
                }
                JSONObject response = null;
                try {
                    response = SSLClient.postMessage(getMessgeQuit());
                } catch (IOException E) {
                    // TODO Auto-generated catch block
                    E.printStackTrace();
                }

                try {
                    status = response.getInt(STATUS);
                } catch (JSONException ex) {
                    Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (JudgeStatus.OutputStatus(status) == false) {
                    return;
                }
            }
        });*/
        timer_roominfo = new java.util.Timer(true);
        DefaultTableModel model = (DefaultTableModel) jRoom.getModel();
        Ask4Roominfo task = new Ask4Roominfo();
        task.setOnRefreshListner(new Ask4Roominfo.OnRefreshListener() {

            @Override
            public void onRefresh(String test) {
                model.setRowCount(0);
                title = Room.Getroom().GetTtile();
                number = Room.Getroom().GetMaxPlayers();
                type = Room.Getroom().GetGameType();
                System.out.println(title + "  " + number + "  " + type);
                if (type == 1) {
                    game_type = "BlackJack";
                }
                wager = Room.Getroom().GetWager();
                jTitle.setText("Welcome to " + title);
                jMax.setText(number + "");
                jWager.setText(wager + "");
                jType.setText(game_type);

                if (task.Get_members_num() > 0) {
                    for (int i = 0; i < task.Get_members_num(); i++) {
                        if (i == 0) {
                            if (task.Get_Members()[i].get_members_id() != Player.GetPlayer().GetUserId()) {
                                jStart.setVisible(false);
                            }
                        }
                        model.addRow(new Object[]{task.Get_Members()[i].get_members_id(), task.Get_Members()[i].get_members_nickname()});
                        //System.out.println(task.get_room_name()[i]+task.get_creator_name()[i]+task.get_game_name()[i]+task.get_currentmax()[i]);
                    }
                }
                System.out.println("RoomStatus!!" + task.Get_roomstatus());
                if (task.Get_roomstatus() == 2) {
                    BlackJackUINew ui = new BlackJackUINew();
                    ui.setVisible(true);
                    try {
                        ui.DoSomethingAtBegin();
                    } catch (MessagingException ex) {
                        Logger.getLogger(BlackJackUINew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    timer_roominfo.cancel();
                    CreateRoom.this.dispose();
                }
                
            }
        });

        timer_roominfo.schedule(task, START_TIME, PERIOD);

    }

    public JSONObject getMessgeQuit() {
        JSONObject test = new JSONObject();
        try {
            userid = Player.GetPlayer().GetUserId();
            roomid = Room.Getroom().GetRoomID();
            test.put("opt", "quitroom");
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jStart = new javax.swing.JButton();
        jQuit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jMax = new javax.swing.JTextField();
        jWager = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jType = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jRoom = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jPanel1.setLayout(null);

        jStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/img/StartGame.jpg"))); // NOI18N
        jStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jStartActionPerformed(evt);
            }
        });
        jPanel1.add(jStart);
        jStart.setBounds(560, 310, 176, 41);

        jQuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/img/QuitRoom.jpg"))); // NOI18N
        jQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jQuitActionPerformed(evt);
            }
        });
        jPanel1.add(jQuit);
        jQuit.setBounds(560, 380, 176, 41);

        jPanel2.setOpaque(false);
        jPanel2.setLayout(null);

        jMax.setEditable(false);
        jMax.setFocusable(false);
        jPanel2.add(jMax);
        jMax.setBounds(110, 20, 40, 25);

        jWager.setEnabled(false);
        jWager.setFocusable(false);
        jPanel2.add(jWager);
        jWager.setBounds(110, 70, 40, 25);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Wager:");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(60, 70, 50, 25);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Max Player:");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(40, 20, 70, 20);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Game Type:");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(40, 120, 70, 25);

        jType.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jType.setForeground(new java.awt.Color(255, 255, 255));
        jPanel2.add(jType);
        jType.setBounds(110, 120, 80, 25);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(570, 0, 230, 200);

        jPanel3.setOpaque(false);

        jTitle.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jTitle.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);
        jPanel3.setBounds(0, 0, 410, 150);

        jRoom.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Nickname"
            }
        ));
        jScrollPane1.setViewportView(jRoom);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(30, 180, 452, 200);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/img/HomePage_bg.jpg"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 800, 450);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jStartActionPerformed
        JSONObject response = null;
        try {
            response = SSLClient.postMessage(getMessgeStart());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            status = response.getInt(STATUS);
        } catch (JSONException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (JudgeStatus.OutputStatus(status) == false) {
            return;
        }
        BlackJackUINew ui = new BlackJackUINew();
        ui.setVisible(true);
        try {
            ui.DoSomethingAtBegin();
        } catch (MessagingException ex) {
            Logger.getLogger(BlackJackUINew.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.dispose();

    }//GEN-LAST:event_jStartActionPerformed

    private void jQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jQuitActionPerformed
        JSONObject response = null;
        try {
            response = SSLClient.postMessage(getMessgeQuit());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            status = response.getInt(STATUS);
        } catch (JSONException ex) {
            Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (JudgeStatus.OutputStatus(status) == false) {
            return;
        }
        timer_roominfo.cancel();
        this.dispose();
    }//GEN-LAST:event_jQuitActionPerformed

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
            java.util.logging.Logger.getLogger(CreateRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateRoom.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CreateRoom().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jMax;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jQuit;
    private javax.swing.JTable jRoom;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jStart;
    private javax.swing.JLabel jTitle;
    private javax.swing.JLabel jType;
    private javax.swing.JTextField jWager;
    // End of variables declaration//GEN-END:variables
}
