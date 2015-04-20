/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JudgeStatus;

import javax.swing.JOptionPane;

/**
 *
 * @author Wei
 */
public class JudgeStatus {
    //private int Status;
    public JudgeStatus(){}
    static public boolean OutputStatus(int stat){
        switch(stat){
            case 200: return true;
            case 501: JOptionPane.showMessageDialog(null, "Connection failed");
                break;
            case 502: JOptionPane.showMessageDialog(null, "Json format unmatched");
                break;
            case 503: JOptionPane.showMessageDialog(null, "Operation unsupported");
                break;
            case 504: JOptionPane.showMessageDialog(null, "Parameter unmatched");
                break;
            case 505: JOptionPane.showMessageDialog(null, "Invalid user name or password");
                break;
            case 506: JOptionPane.showMessageDialog(null, "Operation failed");
                break; 
        }
        return false;
    }
}
