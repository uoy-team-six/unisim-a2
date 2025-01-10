package io.github.unisim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class which represents the game leaderboard.
 */
public class Leaderboard {
    private final Map<String, Integer> scoreMap;

    public Leaderboard() {
        scoreMap = new HashMap<>();
    }

    /**
     * Adds a new entry to the leaderboard for the given player's name and score. If the player already exists, only
     * overwrites their score if the given score is higher.
     *
     * @param name  the name of the player
     * @param score the newest score of the player
     */
    public void addScore(String name, int score) {
        scoreMap.merge(name, score, (oldScore, newScore) -> newScore > oldScore ? newScore : oldScore);
    }

    /**
     * Removes all leaderboard entries.
     */
    public void clear() {
        scoreMap.clear();
    }

    /**
     * Returns the score for the given player name.
     *
     * @param name a player name
     * @return score if the given player name exists; 0 otherwise
     */
    public int getScore(String name) {
        return scoreMap.getOrDefault(name, 0);
    }

    /**
     * Returns a list of player names in score-descending order.
     *
     * @return an immutable list of player names
     */
    public List<String> getSortedNames() {
        return scoreMap.entrySet()
            .stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .map(Map.Entry::getKey)
            .toList();
    }
}
