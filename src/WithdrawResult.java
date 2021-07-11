import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WithdrawResult {
    private static final Map<Integer, Integer> resultMap = new HashMap<>();
    private boolean error = true;
    private final int DEBUG_MODE_VERBOSITY = 0;

    public boolean hasNoError() {
        return !error;
    }

    public Map<Integer, Integer> getResultMap() {
        return resultMap;
    }

    private boolean canGetCombination(int requestAmount, Map<Integer, Integer> hash_Map) {
        ArrayList<Integer> notes = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : hash_Map.entrySet()) {
            Integer noteValue = entry.getKey();
            Integer noteCount = entry.getValue();
            int j;
            for (j = 0; j < noteCount; j++) {
                notes.add(noteValue);
            }
        }
        Collections.sort(notes, Collections.reverseOrder());
        int noteCount = notes.size();

        // Build up a DP table
        int[][] K = new int[noteCount + 1][requestAmount + 1];

        int i;
        int w;
        for (i = 0; i <= noteCount; i++) {
            for (w = 0; w <= requestAmount; w++) {
                if (i == 0 || w == 0) {
                    K[i][w] = 0;
                } else if (notes.get(i - 1) <= w) {
                    K[i][w] = Math.max(notes.get(i - 1) + K[i - 1][w - notes.get(i - 1)], K[i - 1][w]);
                } else {
                    K[i][w] = K[i - 1][w];
                }
            }
        }

        // back tracking to get the solution
        resultMap.clear();
        i = noteCount;
        w = requestAmount;
        while (i > 0 && w > 0) {
            if (K[i][w] != K[i - 1][w]) {
                int currentItem = notes.get(i - 1);
                if (resultMap.containsKey(currentItem)) {
                    int currentCount = resultMap.get(currentItem) + 1;
                    resultMap.put(currentItem, currentCount);
                } else {
                    resultMap.put(currentItem, 1);
                }
                w = w - notes.get(i - 1);
            }
            i--;
        }

        printDebugMessage(1, noteCount, requestAmount, K);

        return K[noteCount][requestAmount] == requestAmount;
    }

    private void printDebugMessage(int verbosity, int count, int amount, int[][] table) {
        if (verbosity <= DEBUG_MODE_VERBOSITY) {
            System.out.println("Result Map: ");
            for (Map.Entry<Integer, Integer> entry : resultMap.entrySet()) {
                System.out.println("$" + entry.getKey() + " used: " + entry.getValue());
            }
            System.out.println("DP Table: ");
            int i;
            int w;
            for (i = 0; i <= count; i++) {
                for (w = 0; w <= amount; w++) {
                    System.out.print(table[i][w] + " ");
                }
                System.out.println();
            }
        }
    }

    public void getWithdrawResult(int requestAmount, Map<Integer, Integer> hash_Map) {
        error = !canGetCombination(requestAmount, hash_Map);
    }
}
