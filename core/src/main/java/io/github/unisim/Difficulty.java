package io.github.unisim;

public enum Difficulty {
    EASY("Easy", 100000),
    NORMAL("Normal", 75000),
    HARD("Hard", 50000);

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
