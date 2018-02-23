package io.zipcoder.casino.GameTools.Deck;

public class Card {
    private Suit suitEnum;
    private Rank rankEnum;

    public Card(Rank rankEnum, Suit suitEnum) {
        this.rankEnum = rankEnum;
        this.suitEnum = suitEnum;
    }

    public Card(){
        this.rankEnum = null;
        this.suitEnum = null;
    }

    public Suit getSuitEnum() {
        return suitEnum;
    }

    public void setSuitEnum(Suit suitEnum) {
        this.suitEnum = suitEnum;
    }

    public Rank getRankEnum() {
        return rankEnum;
    }

    public void setRankEnum(Rank rankEnum) {
        this.rankEnum = rankEnum;
    }
}
