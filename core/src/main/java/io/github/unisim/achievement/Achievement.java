package io.github.unisim.achievement;

public enum Achievement {
    ENTREPRENEUR("Entrepreneur"),
    I_HEART_UNI("I Heart Uni"),
    STUDENT_COUNT("One Hundredth Student");

    private final String name;

    Achievement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
