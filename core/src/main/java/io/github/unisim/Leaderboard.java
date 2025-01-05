package io.github.unisim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Leaderboard {
    private final Map<String, Integer> scoreMap;

    public Leaderboard() {
        scoreMap = new HashMap<>();
        for (var name : List.of("Daniel", "Ellen", "Hussain", "Jason", "Minnie", "Owen")) {
            scoreMap.put(name, ThreadLocalRandom.current().nextInt(30000, 60000));
        }
    }

    public void addScore(String name, int score) {
        scoreMap.put(name, score);
    }

    public int getScore(String name) {
        return scoreMap.getOrDefault(name, 0);
    }

    public List<String> getSortedNames() {
        return scoreMap.entrySet()
            .stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .map(Map.Entry::getKey)
            .toList();
    }
}
