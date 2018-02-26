package io.zipcoder.casino.core;


public class Player {
    private Long chips;

    public Player() {
        chips = 500l;
    }

    public Boolean canCoverBet(Long minimumBetCharge) {
        return (chips >= minimumBetCharge);
    }

    public long betChips(long bet) {
        bet = (canCoverBet(bet)) ? bet : chips;
        chips = chips - bet;
        return bet;
    }

    public void addChips(long numberOfChipsToAdd) {
        chips += numberOfChipsToAdd;
    }

    public Long getBalance() {
        return chips;
    }
}