import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class CashDispensingApp {

    /* A hash map that stores how many of each type of bank note the device has */
    private static final Map<Integer, Integer> noteMap = new HashMap<>();
    private final int BALANCE_THRESHOLD;
    private int balance = 0;
    private boolean balanceAlert = false; // TODO if get extra time
    private final int DEBUG_MODE_VERBOSITY = 1;

    /* Constructor */
    public CashDispensingApp(int defaultNoteCount, int balanceThreshold) {
        // Initialisation with default value
        BALANCE_THRESHOLD = balanceThreshold;
        for (Denomination note : Denomination.values()) {
            noteMap.put(note.getNoteValue(), defaultNoteCount);
        }
        updateBalance();
        printDebugMessage("Initialisation...", 1);
    }

    private void updateNoteMap(Map<Integer, Integer> hash_Map) {
        for (Map.Entry<Integer, Integer> entry : hash_Map.entrySet()) {
            int currentNote = entry.getKey();
            int currentCount = noteMap.get(currentNote) - entry.getValue();
            noteMap.put(currentNote, currentCount);
        }
        updateBalance();
        printDebugMessage("After transaction...", 1);
    }

    private void updateBalance() {
        balance = 0;
        noteMap.forEach((noteValue, noteCount) -> balance += noteValue * noteCount);
        balanceAlert = balance <= BALANCE_THRESHOLD;
    }

    public WithdrawResult withdrawMoney(int requestedAmount) {
        WithdrawResult withdrawResult = new WithdrawResult();
        if (requestedAmount <= balance) {
            withdrawResult.getWithdrawResult(requestedAmount, noteMap);
            if (withdrawResult.hasNoError()) {
                updateNoteMap(withdrawResult.getResultMap());
            }
        }
        return withdrawResult;
    }

    /* Print balance if in debug mode */
    private void printDebugMessage(String message, int verbosity) {
        if (verbosity <= DEBUG_MODE_VERBOSITY) {
            System.out.println(message);
            System.out.println("Balance: " + balance);
            for (Map.Entry<Integer, Integer> entry : noteMap.entrySet()) {
                System.out.println("$" + entry.getKey() + " left: " + entry.getValue());
            }
        }
    }

    public static void main(String[] args) {
        CashDispensingApp atm = new CashDispensingApp(10, 100);
        Scanner myScanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter desired amount: ");
            int requestedAmount = myScanner.nextInt();
            WithdrawResult withdrawResult = atm.withdrawMoney(requestedAmount);
            if (withdrawResult.hasNoError()) {
                System.out.println("Transaction accepted...Please wait...");
            } else {
                System.out.println("Invalid amount requested...");
            }
        }
    }
}
