package com.ziminer.leetcode.java;

import java.util.*;

/**
 * A binary watch has 4 LEDs on the top which represent the hours (0-11), and the 6 LEDs on the bottom represent the minutes (0-59).
 * Each LED represents a zero or one, with the least significant bit on the right.
 *
 * 0011
 * 011001
 *
 * For example, the above binary watch reads "3:25".
 *
 * Given a non-negative integer n which represents the number of LEDs that are currently on,
 * return all possible times the watch could represent.
 */
public class BinaryWatch {

    private static final int HOURS_BITS = 4;

    private static final int MINS_BITS = 6;

    private List<Integer> possibleNumbers(int numBits, int numOnes, int max) {
        if (numOnes > numBits || numBits < 0 || numOnes < 0 || max < 0) {
            return Collections.emptyList();
        }

        final Map<Integer, List<Integer>> bitsSetToPossibleNums = new HashMap<>();
        bitsSetToPossibleNums.put(0, new LinkedList<>(Arrays.asList(0)));
        bitsSetToPossibleNums.put(1, new LinkedList<>(Arrays.asList(1)));

        // Iterate over the bits, calculating the possible numbers if the current bit is set,
        // given the possible numbers if the last bit was set.
        //
        // numBits = 3, numOnes = 2
        // 0 -> (0 -> [0], 1 -> [1])
        // 1 -> (0 -> [0], 1 -> [2, 1], 2 -> [3])
        // 2 -> (0 -> [0], 1 -> [2, 1, 4], 2 -> [6, 5, 3])
        for (int i = 1; i < numBits; ++i) {
            final int curBit = i;
            // Need a temporary map so that numbers calculated for this bit
            // don't interfere with each other.
            final Map<Integer, List<Integer>> temp = new HashMap<>();
            bitsSetToPossibleNums.forEach((numSetBits, possibilities) -> {
                // Check if we can try setting the current bit.
                if (numSetBits < numOnes) {
                    List<Integer> curPossibilities = temp.computeIfAbsent(numSetBits + 1, k -> new LinkedList<>());
                    possibilities.stream()
                            .map(num -> num + (1 << curBit))
                            .filter(num -> num <= max)
                            .forEach(curPossibilities::add);
                }
            });

            // Merge the temporary map back into the main map.
            temp.forEach((setBits, possibilities) -> {
                bitsSetToPossibleNums.computeIfAbsent(setBits, k -> new LinkedList<>()).addAll(possibilities);
            });
        }

        return bitsSetToPossibleNums.get(numOnes);
    }

    public List<String> readBinaryWatch(int num) {
        List<String> possibilities = new ArrayList<>();
        for (int hoursOn = 0; hoursOn <= Math.min(5, num); ++hoursOn) {
            int minsOn = num - hoursOn;
            List<Integer> possibleHours = possibleNumbers(HOURS_BITS, hoursOn, 11);
            List<Integer> possibleMins = possibleNumbers(MINS_BITS, minsOn, 59);
            for (Integer possibleHour : possibleHours) {
                for (Integer possibleMin : possibleMins) {
                    possibilities.add(possibleHour + ":" + ((possibleMin < 10) ? "0" : "") + possibleMin);
                }
            }
        }
        return possibilities;
    }
}
