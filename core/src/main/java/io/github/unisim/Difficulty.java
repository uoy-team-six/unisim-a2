package io.github.unisim;

public enum Difficulty {
    EASY("Easy", 10000),
    NORMAL("Normal", 7000),
    HARD("Hard", 5000);

    private final String name;
    private final int startingMoney;

    Difficulty(String name, int startingMoney) {
        this.name = name;
        this.startingMoney = startingMoney;
    }

    public String getName() {
        return name;
    }

    public int getStartingMoney() {
        return startingMoney;
    }
}
