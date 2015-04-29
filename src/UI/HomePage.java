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
import TimerTaskGame.Ask4RoomList;
import static UI.CreateRoomInfo.STATUS;
import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;

/**
 *
 * @author Wei
 */
public class HomePage extends javax.swing.JFrame {

    /**
     * Creates new form HomePage
     */
    static int START_TIME = 100;
    static int PERIOD = 1000;

    static String KEY_JOINROOM = "joinroom";
    static String KEY_USERID = "userid";
    static String KEY_ROOMID = "roomid";

    static String KEY_RES = "result";
    static String KEY_HISTORIES = "histories";
    static String KEY_TRANID = "transactionid";
    static String KEY_CARDNUM = "cardnumber";
    static String KEY_TYPE = "type";
    static String KEY_TIME = "time";
    static String KEY_AMOUNT = "amount";

    static String KEY_TRANHISTORY = "tranhistory";
    private int page;
    private int num = 10;
    private JSONArray histories;

    private int[] TransactionId = new int[num];
    private String[] CardNum = new String[num];
    private int[] Type = new int[num];
    private long[] Date = new long[num];
    private double[] Amount = new double[num];
    private int count;

    private int selected_row = -1;
    private int userid;
    private int roomid;
    private int status;
    private int[] roomids = new int[num];
    private int[] roomwagers = new int[num];
    private int[] roomtypes = new int[num];
    private int[] roommax = new int[num];
    private String[] roomtitles = new String[num];
    Ask4RoomList task = new Ask4RoomList();
    Timer timer_roomlist;
    
    public HomePage() throws JSONException {
        initComponents();
        jRecord.setVisible(false);
        //jScrollPane1.getViewport().setOpaque(false);
        //jScrollPane1.setOpaque(false);
        //jScrollPane1.getColumnHeader().setOpaque(false);
        //getContentPane().add(jScrollPane1,BorderLayout.CENTER);
        jName.setText(Player.GetPlayer().GetNickName());
        jMoney.setText("Money: " + Player.GetPlayer().GetBalance() + "");
        jRank.setText("Rank: " + Player.GetPlayer().GetRank() + "");

        timer_roomlist = new java.util.Timer(true);
        //Ask4RoomList task = new Ask4RoomList();
        DefaultTableModel model = (DefaultTableModel) jRoomList.getModel();
        jRoomList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (jRoomList.getSelectedRow() == -1) {
                    return;
                } else {
                    selected_row = jRoomList.getSelectedRow();
                    //System.out.println("Listener: " + selected_row);
                }
            }
        });
        task.setOnRefreshListner(new Ask4RoomList.OnRefreshListener() {

            @Override
            public void onRefresh(String test) {
                model.setRowCount(0);
                jName.setText(Player.GetPlayer().GetNickName());
                jMoney.setText("Money: " + Player.GetPlayer().GetBalance() + "");
                jRank.setText("Rank: " + Player.GetPlayer().GetRank() + "");
                if (task.get_room_number() > 0) {
                    for (int i = 0; i < task.get_room_number(); i++) {
                        model.addRow(new Object[]{task.get_room_title()[i], task.get_creator_name()[i], task.get_room_name()[i], task.get_currentmax()[i]});
                        //System.out.println(task.get_room_name()[i]+task.get_creator_name()[i]+task.get_game_name()[i]+task.get_currentmax()[i]);
                    }
                }
                if (selected_row != -1) {
                    jRoomList.setRowSelectionInterval(selected_row, selected_row);
                    //System.out.println(jRoomList.getSelectedRow());
                }
                roomids = task.get_roomids();
                roomwagers = task.get_room_wager();
                roomtypes = task.get_room_type();
                roommax = task.get_room_max();
                roomtitles = task.get_room_title();
            }

        });
        timer_roomlist.schedule(task, START_TIME, PERIOD);

        //System.out.println("Reach");
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
        jButton1 = new javax.swing.JButton();
        jMoney = new javax.swing.JLabel();
        jName = new javax.swing.JLabel();
        Purchase = new javax.swing.JButton();
        jRank = new javax.swing.JLabel();
        jTran = new javax.swing.JButton();
        jRecord = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jRoomList = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jCreate = new javax.swing.JButton();
        jJoin = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setLayout(null);

        jPanel2.setOpaque(false);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/img/pic1.png"))); // NOI18N

        jMoney.setForeground(new java.awt.Color(255, 255, 255));
        jMoney.setText("Money");

        jName.setForeground(new java.awt.Color(255, 255, 255));
        jName.setText("Name");

        Purchase.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/img/Charge.jpg"))); // NOI18N
        Purchase.setPreferredSize(new java.awt.Dimension(100, 23));
        Purchase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PurchaseActionPerformed(evt);
            }
        });

        jRank.setForeground(new java.awt.Color(255, 255, 255));
        jRank.setText("Rank");

        jTran.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/img/Transaction.png"))); // NOI18N
        jTran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTranActionPerformed(evt);
            }
        });

        jRecord.setText("Game Record");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTran, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jRecord)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Purchase, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRank, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jMoney, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Purchase, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jRank, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTran, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRecord))
                .addContainerGap())
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(440, 0, 360, 140);

        jPanel3.setOpaque(false);

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setOpaque(false);

        jRoomList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Room Name", "Creator Name", "Game Type", "Current/Total Num"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jRoomList.setOpaque(false);
        jRoomList.getTableHeader().setResizingAllowed(false);
        jRoomList.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jRoomList);
        if (jRoomList.getColumnModel().getColumnCount() > 0) {
            jRoomList.getColumnModel().getColumn(0).setResizable(false);
            jRoomList.getColumnModel().getColumn(1).setResizable(false);
            jRoomList.getColumnModel().getColumn(2).setResizable(false);
            jRoomList.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel3);
        jPanel3.setBounds(0, 130, 530, 320);

        jPanel4.setOpaque(false);

        jCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/img/CreateRoom.jpg"))); // NOI18N
        jCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCreateActionPerformed(evt);
            }
        });

        jJoin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/img/JoinRoom.jpg"))); // NOI18N
        jJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jJoinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jJoin, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addComponent(jJoin, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );

        jPanel1.add(jPanel4);
        jPanel4.setBounds(550, 250, 230, 170);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/UI/img/HomePage_bg.jpg"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 799, 450);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCreateActionPerformed
        // TODO add your handling code here:

        CreateRoomInfo c = new CreateRoomInfo();
        c.setSize(400, 300);
        c.setLocation(0, 0);
        c.setVisible(true);
        timer_roomlist.cancel();
        this.dispose();
    }//GEN-LAST:event_jCreateActionPerformed

    private void PurchaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PurchaseActionPerformed
        // TODO add your handling code here:
        Purchase p = new Purchase();
        p.setVisible(true);
    }//GEN-LAST:event_PurchaseActionPerformed

    private void jJoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jJoinActionPerformed
        // TODO add your handling code here:
        if (jRoomList.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Please select a room to join");
            return;
        }
        JSONObject response = null;
        try {
            response = SSLClient.postMessage(getMessgeJoin());
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
        if (JudgeStatus.OutputStatus(status) == false) {
            return;
        }
        Player.GetPlayer().SetIsCreator(false);
        CreateRoom C = new CreateRoom();
        C.setSize(800, 450);
        C.setLocation(0, 0);
        C.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jJoinActionPerformed

    public JSONObject getMessgeJoin() {
        JSONObject test = new JSONObject();
        try {
            test.put("opt", KEY_JOINROOM);
            userid = Player.GetPlayer().GetUserId();
            test.put("userid", userid);
            JSONObject info = new JSONObject();
            jRoomList.getSelectedRow();
            //int row = jRoomList.getSelectedRow();

            //Ask4RoomList task = new Ask4RoomList();
            //System.out.println(selected_row);
            for (int j = 0; j < roomids.length; j++) {
                //System.out.println(roomids[j]);
            }
            //System.out.println(selected_row);
            Room.Getroom().SetRoomID(roomids[selected_row]);
            Room.Getroom().SetGameType(roomtypes[selected_row]);
            Room.Getroom().SetMaxPlayers(roommax[selected_row]);
            Room.Getroom().SetTitle(roomtitles[selected_row]);
            Room.Getroom().SetWager(roomwagers[selected_row]);
            roomid = Room.Getroom().GetRoomID();
            info.put("roomid", roomid);
            test.put("info", info);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(test);
        return test;
    }

    private void jTranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTranActionPerformed
        // TODO add your handling code here:
        JSONObject response = null;
        try {
            response = SSLClient.postMessage(getMessgeTran());
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
        if (JudgeStatus.OutputStatus(status) == false) {
            return;
        }
        JSONObject result = null;
        histories = new JSONArray();
        try {
            result = response.getJSONObject(KEY_RES);
            histories = result.getJSONArray(KEY_HISTORIES);
            if (histories.length() == 0) {
                TransactionHistory t = new TransactionHistory();
                t.setVisible(true);
            } else {
                count = histories.length();
                for (int i = 0; i < histories.length(); i++) {
                    JSONObject history = histories.getJSONObject(i);
                    TransactionId[i] = history.getInt(KEY_TRANID);
                    CardNum[i] = history.getString(KEY_CARDNUM);
                    Type[i] = history.getInt(KEY_TYPE);
                    Date[i] = history.getLong(KEY_TIME);
                    Amount[i] = history.getDouble(KEY_AMOUNT);
                }
                TransactionHistory t = new TransactionHistory(TransactionId, CardNum, Type, Date, Amount, count);
                t.setVisible(true);
            }
        } catch (JSONException ex) {
            Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jTranActionPerformed

    public JSONObject getMessgeTran() {
        JSONObject test = new JSONObject();
        try {
            test.put("opt", KEY_TRANHISTORY);
            userid = Player.GetPlayer().GetUserId();
            test.put("userid", userid);
            JSONObject info = new JSONObject();
            page = 1;
            info.put("page", page);
            info.put("num", num);
            test.put("info", info);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println(test);
        return test;
    }

    //JSONObject jsonObject = new JSONObject(string);
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
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    new HomePage().setVisible(true);
                } catch (JSONException ex) {
                    Logger.getLogger(HomePage.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Purchase;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jCreate;
    private javax.swing.JButton jJoin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jMoney;
    private javax.swing.JLabel jName;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel jRank;
    private javax.swing.JButton jRecord;
    private javax.swing.JTable jRoomList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jTran;
    // End of variables declaration//GEN-END:variables
}
