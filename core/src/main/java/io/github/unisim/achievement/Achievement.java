package io.github.unisim.achievement;

public enum Achievement {
    ENTREPRENEUR("Entrepreneur", 500),
    I_HEART_UNI("I Heart Uni", 1000),
    STUDENT_COUNT("One Hundredth Student", 250);

    private final String name;
    private final int score;

    Achievement(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
