
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PokerGame;

import TimerTaskGame.Ask4RoundInfo.PlayerInfo;
import TimerTaskGame.Ask4RoundInfo;
import TimerTaskGame.Ask4RoundInfo.PlayerInfo.CardsInfo;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Administrator
 */
public class BlackJackRule {

    static public boolean AmIBlackJack(PlayerInfo player) {
        return AmIBlackJack(player.getCardsInfo());
    }

    static public boolean AmIBlackJack(CardsInfo[] cardArray) {
        if (cardArray.length != 2) {
            //You certainly not, Man!
            return false;
        }
        if (cardArray[0].getnumber() == 1 && cardArray[1].getnumber() >= 10 && cardArray[1].getnumber() < 12) {
            return true;
        }
        if (cardArray[1].getnumber() == 1 && cardArray[0].getnumber() >= 10 && cardArray[0].getnumber() < 12) {
            return true;
        }
        return false;
    }

    static public boolean AmIFiveDragon(PlayerInfo player) {
        return AmIFiveDragon(player.getCardsInfo());
    }

    static public boolean AmIFiveDragon(CardsInfo[] cardArray) {
        if (cardArray.length >= 5 && GetMaxValueOfHand(cardArray) != -1) {
            return true;
        } else {
            return false;
        }
    }

    static public boolean AmIBust(CardsInfo[] cardArray) {
        return GetMaxValueOfHand(cardArray) == -1;
    }

    static public boolean AmIBust(PlayerInfo player) {
        return AmIBust(player.getCardsInfo());
    }

    static public int GetMaxValueOfHand(CardsInfo[] cardArray) {
        int nNumOfAce = 0;
        int nTotal = 0;

        for (CardsInfo card : cardArray) {
            if (card.getnumber() < 14 && card.getnumber() > 10) {
                nTotal += 10;
            } else if(card.getnumber() == 1){
                nTotal += 11;
            } else {
                nTotal += card.getnumber();
            }
            if (card.getnumber() == 1) {
                nNumOfAce++;
            }
        }
        if (nTotal <= 21) {
            return nTotal;
        } else if (nTotal > 21 && nNumOfAce == 0) {
            return -1;
        } else if (nNumOfAce == 0) {
            return nTotal;
        } else {
            //Total > 21 and there is on more A
            for (int i = 0; i < nNumOfAce; i++) {
                nTotal -= 10;
                if (nTotal <= 21) {
                    return nTotal;
                }
            }
            // Could not happen but need some log
            return -1;
        }
    }

    static public int GetMaxValueOfHand(PlayerInfo player) {
        return GetMaxValueOfHand(player.getCardsInfo());
    }

    static public boolean GetBlackJackResult(PlayerInfo pPlayer, PlayerInfo pAI) {
        int nRet = 0;
        boolean bAIWin = false;
        if (BlackJackRule.AmIBlackJack(pAI)) {
            nRet = 10;
            bAIWin = true;
        } else if (BlackJackRule.AmIBlackJack(pPlayer)) {
            nRet = -10;
            bAIWin = false;
        } else if (BlackJackRule.AmIFiveDragon(pAI)) {
            nRet = 20;
            bAIWin = true;
        } else if (BlackJackRule.AmIFiveDragon(pPlayer)) {
            nRet = -20;
            bAIWin = false;
        } else if (BlackJackRule.GetMaxValueOfHand(pPlayer) <= BlackJackRule.GetMaxValueOfHand(pAI)) {
            nRet = 30;
            bAIWin = true;
        } else {
            nRet = -30;
            bAIWin = false;
        }
        return bAIWin;
    }
    
}
