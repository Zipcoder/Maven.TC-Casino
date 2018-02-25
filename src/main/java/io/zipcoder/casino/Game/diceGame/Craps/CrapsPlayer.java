package io.zipcoder.casino.Game.diceGame.Craps;


import io.zipcoder.casino.*;
import io.zipcoder.casino.CasinoUtilities.Console;

import java.util.ArrayList;

public class CrapsPlayer extends Player implements Gambler {

    private boolean passLine;
    private boolean odds;
    private boolean come;
    private boolean dontCome;
    private ArrayList<Integer> comePoints;
    private ArrayList<Integer> dontComePoints;

    private Escrow playerEscrow;



    public CrapsPlayer(Profile someProfile) {

        super(someProfile);
        this.playerEscrow = new Escrow();
        this.comePoints = new ArrayList<Integer>(0);
        this.dontComePoints = new ArrayList<Integer>(0);

    }


    public boolean bet(TypeOfBet typeOfBet, double amount) {
        double accountBalance = this.getProfile().getAccountBalance();

        if (accountBalance < amount) {
            Console.print("Insufficient funds : cannot place bet");
            return false;
        }
        else if (this.escrowContains(typeOfBet)) {
            double escrowBalance = this.getEscrowBet(typeOfBet);
            this.getProfile().setAccountBalance(accountBalance - amount);
            this.setEscrow(typeOfBet, amount + escrowBalance);
            return true;
        }
        else {
            this.getProfile().setAccountBalance(accountBalance - amount);
            this.setEscrow(typeOfBet, amount);
            return true;
        }

    }

    public void win(TypeOfBet typeOfBet, double payoutMultiplier) {
        double accountBalance = this.getProfile().getAccountBalance();
        double escrow = this.getEscrowBet(typeOfBet);
        double winnings = escrow + (escrow * payoutMultiplier);

        this.getProfile().setAccountBalance(accountBalance+ winnings);
        this.setEscrow(typeOfBet,0);
    }

    public void lose(TypeOfBet typeOfBet) {
        this.setEscrow(typeOfBet, 0);
    }

    public void setEscrow(TypeOfBet typeOfBet, double incomingBet){
        this.playerEscrow.getEscrow().put(typeOfBet, incomingBet);
    }


    public double getEscrowBet(TypeOfBet typeOfBet){
        return this.playerEscrow.getEscrow().get(typeOfBet);
    }

    public boolean escrowContains(TypeOfBet typeOfBet){
        return playerEscrow.getEscrow().containsKey(typeOfBet) ;
    }

    public boolean isPassLine() {
        return this.passLine;
    }

    public void setPassLine(boolean input) {
        this.passLine = input;

    }

    public void setOdds(boolean input){
        this.odds = input;
    }

    public boolean hasOdds(){
        return this.odds;
    }
    public void setCome(boolean input){
        this.come = input;
    }
    public boolean isCome(){
        return this.come;
    }

    public void setDontCome(boolean input){
        this.dontCome=input;
    }

    public boolean isDontCome(){
        return this.dontCome;
    }

    public ArrayList<Integer> getComePoints(){
        return this.comePoints;
    }

    public ArrayList<Integer> getDontComePoints(){
        return this.dontComePoints;
    }

    public void addComePoint(Integer point){
        this.comePoints.add(point);
    }

    public void removeComePoint(Integer point){
        this.comePoints.remove(point);
    }

    public void addDontComePoint(Integer point){
        this.dontComePoints.add(point);
    }

    public void removeDontComePoint(Integer point){
        this.dontComePoints.remove(point);
    }

}