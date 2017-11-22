package io.zipcoder.casino.games.craps;


import io.zipcoder.casino.nuts_n_bolts.User;

import java.text.NumberFormat;
import java.util.Random;
import static io.zipcoder.casino.nuts_n_bolts.Input.getPositiveDoubleInput;
import static io.zipcoder.casino.nuts_n_bolts.Input.getStringInput;

public class CrapsConsole {

    public static void main(String[] args){
        CrapsConsole console = new CrapsConsole();
        console.run();
    }

    private Craps game = new Craps();
    private User player;
    private double mainPotBet;
    private double sidePotBet;
    private boolean pointSet=false;
    private boolean pointMet=false;
    private Random rando=new Random();
    private NumberFormat defaultFormat;

    public CrapsConsole(){
        this(new User("Uncle Bob", 10_000.0));
    }

    public CrapsConsole(User user){
        player=user;
        defaultFormat=NumberFormat.getCurrencyInstance();
    }

    public void run(){
        welcomePlayer();
        game.determineFirstRoller();
        do {
            while (!pointSet) {//Continue to bet until the roller
                            //throws a point instead of a win/loss.
                initialBet();
                pointSet = resolveInitialThrow(game.initialThrow());
            }
            while (!pointMet) {//Continue to bet until the roller
                            //meets their point or craps out
                secondaryBet();
                pointMet = resolveSecondaryThrow(game.secondaryThrow());
            }
            changeTurns();//Reset flags, change active player
        }while(game.play(getStringInput("Continue playing? [Y/N] ")));
    }

    private void initialBet(){

        if (game.getPlayerTurn())
        {
            playerInitialBets(generateBotBet());
        }
        else
        {
            playerInitialBets();
        }

    }
    private void playerInitialBets() {
        do {
///////////////
            System.out.println(game.toString());
            mainPotBet = getPositiveDoubleInput
                    ("How much would you like to bet? ");
///////////////
        }while (player.getWallet().getMoney()<mainPotBet);

        game.takeBet(player.getWallet().takeOutMoney(mainPotBet));//player bet
        game.takeBet(mainPotBet);//house bet matches
    }
    private void playerInitialBets(Double betToMatch){
        game.takeBet(betToMatch);//house bet to match
///////////////
        System.out.println(game.toString());
        System.out.println("You have to match "+betToMatch);
///////////////
        game.takeBet(player.getWallet().takeOutMoney(betToMatch));//player matches bet
        mainPotBet=betToMatch;
    }

    private double generateBotBet(){
        return(Math.pow(rando.nextDouble(),2)*
                (rando.nextInt(player.getWallet().getMoney().intValue()))+1);
    }

    private void secondaryBet(){
///////////////
        System.out.println(game.toString());
///////////////
        if (game.getPlayerTurn())
        {
            playerSecondaryBets(generateBotBet());
        }
        else
        {
            playerSecondaryBets();
        }
    }
    private void playerSecondaryBets(){
        do {
///////////////
            sidePotBet = getPositiveDoubleInput
                    ("How much would you like to bet? ");
///////////////
        }while (player.getWallet().getMoney()< sidePotBet);

        game.takeSideBet(player.getWallet().takeOutMoney(sidePotBet));//player bet
        game.takeSideBet(sidePotBet);//house bet matches
    }
    private void playerSecondaryBets(Double betToMatch){
        game.takeSideBet(betToMatch);//house bet to match
///////////////
        System.out.println("You have to match "+betToMatch);
///////////////
        game.takeSideBet(player.getWallet().takeOutMoney(betToMatch));//player matches bet
        sidePotBet=betToMatch;
    }

    private boolean resolveInitialThrow(int resultOfThrownDice){
        if (resultOfThrownDice!=0){
            //non-Thrower (-1) or thrower (1) wins the mainPotBet
            resolveInitialThrowBet(resultOfThrownDice);
            return false;
        }
        //point was set and we move onto the
        // next step in the game
        return true;
    }
    private void resolveInitialThrowBet(int a){
        if (a==1){
            if (game.getPlayerTurn()){//If the thrower is the player and they won, pay them
                player.getWallet().addMoney(game.emptyPot());
                //Player wins the pot and we go back to bet again
            }
            else//If the bot is the thrower, empty the pot
            {
                mainPotBet=game.emptyPot();//mainPotBet will be overwritten in the next
                                //function call, so we can use it here to catch this
                                //method's return
            }
        }
        else {
            if (game.getPlayerTurn()) {//If the thrower is the player and they lost, empty the pot and bet again
                mainPotBet=game.emptyPot();
            }
            else{//if the bot is the thrower and they lost, pay the player
                player.getWallet().addMoney(game.emptyPot());
            }
        }
///////////////
        System.out.println(game.toString());
///////////////
    }

    private boolean resolveSecondaryThrow(int resultOfThrownDice){
        switch (resultOfThrownDice) {

            case 0: {//Not a point, not a spread, not a crap. Roll again
                neitherWinsAnyPot();
                return false;
            }
            case -1://Crapped out. pay non-thrower
            case 1 : {//Point met. Pay out to thrower
                resolveSecondaryThrowBet(resultOfThrownDice);
                return true;
            }
            default: {//Pair made, pay sideBet to non-thrower.
                return false;
            }
        }//end switch
    }
    private void resolveSecondaryThrowBet(int a){
        if (a==1){//Point met, pay out thrower from mainPot and sidePot
            if (game.getPlayerTurn())
            {//if player is the thrower, give them the pots and then reset bet vars
                playerWinsBothPots();

            }
            else//if player is not the thrower, empty pot and reset bet vars
            {
                opponentWinsBothPots();
            }
        } else
            if(a==-1)//Crapped out. Pay out the non-thrower from mainPot and sidePot
            {
                if (game.getPlayerTurn())
                {
                    opponentWinsBothPots();
                }
                else
                {
                    playerWinsBothPots();
                }
            } else//Won the pair, but not the point. Pay non-thrower the sidePot
                {
                    if (game.getPlayerTurn())
                    {
                        playerWinsSidePot();
                    }
                    else
                    {
                        opponentWinsSidePot();
                    }
                }

    }

    private void displayOpponentBetting(){

    }
    private void displayPlayerBetting(){

    }

    private void neitherWinsAnyPot(){
        System.out.println("A "+game.getNumberRolled()+" was rolled... nothing special.");
        System.out.println("You have "+player.getWallet().getMoney()+" in your wallet now.");
        printPots();
        enterAnyKeyToContinue();
    }
    private void playerWinsSidePot(){

        System.out.println("You rolled "+game.getNumberRolled()+" and won the Side Pot!");
        System.out.println(defaultFormat.format(game.getSidePot().getMoney())+" from Side Pot");

        player.getWallet().addMoney(game.emptySidePot());

        System.out.println("You have "+defaultFormat.format(player.getWallet().getMoney())+" in your wallet now");
        printPots();

        enterAnyKeyToContinue();

        sidePotBet=0;
    }
    private void opponentWinsSidePot(){

        System.out.println("Opponent rolled "+game.getNumberRolled()+" and won the Side Pot!");
        System.out.println(defaultFormat.format(game.getSidePot().getMoney())+" from Side Pot");

        sidePotBet=game.emptySidePot();

        System.out.println("You have "+defaultFormat.format(player.getWallet().getMoney())+" in your wallet now");
        printPots();

        enterAnyKeyToContinue();

        sidePotBet=0;
    }
    private void opponentWinsBothPots() {
        System.out.println("Opponent rolled "+game.getNumberRolled()+" and won everything!");
        System.out.println(defaultFormat.format(game.getMainPot().getMoney())+" from Main Pot");
        System.out.println(defaultFormat.format(game.getSidePot().getMoney())+" from Side Pot");

        mainPotBet=game.emptyPot();
        sidePotBet=game.emptySidePot();

        System.out.println("You have "+defaultFormat.format(player.getWallet().getMoney())+" in your wallet now");
        printPots();

        enterAnyKeyToContinue();

        mainPotBet=0;
        sidePotBet=0;
    }
    private void playerWinsBothPots() {

        System.out.println("You rolled "+game.getNumberRolled()+" and won everything!");
        System.out.println(defaultFormat.format(game.getMainPot().getMoney())+" from Main Pot");
        System.out.println(defaultFormat.format(game.getSidePot().getMoney())+" from Side Pot");

        player.getWallet().addMoney(game.emptyPot());
        player.getWallet().addMoney(game.emptySidePot());

        System.out.println("You have "+defaultFormat.format(player.getWallet().getMoney())+" in your wallet now");
        printPots();

        enterAnyKeyToContinue();
        mainPotBet=0;
        sidePotBet=0;
    }

    private void welcomePlayer(){
        System.out.println("Hello, "+player.getName()+". Welcome to the "+game.getClass().getSimpleName()+" table.");
    }
    private void changeTurns(){
        mainPotBet=0;
        sidePotBet=0;
        pointSet=false;
        pointMet=false;
        game.changePlayerTurn();
    }
    private void printPots() {
        System.out.println(defaultFormat.format(game.getMainPot().getMoney())+" now in Main Pot");
        System.out.println(defaultFormat.format(game.getSidePot().getMoney())+" now in Side Pot");
    }
    private void enterAnyKeyToContinue(){
        String dump = getStringInput("Enter any key to continue: ");
    }

}