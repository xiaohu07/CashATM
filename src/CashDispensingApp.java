import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;


public class CashDispensingApp {
    enum State {
        INITIALISATION, /* Start up */
        WORKING,
        WAITING /* Need to be re-supplied */
    }

    static final int THRESHOLD = 100;
    static final int DEFAULT = 10;
    final int noteTwenty = 20;
    final int noteFifty = 50;
    /* A hash map that stores how many of each type of bank note the device has */
    Map<Integer, Integer> app = new HashMap<>();

    State state;
    int tempNumTwenty;
    int tempNumFifty;

    /* Constructor */
    public CashDispensingApp() {
        // Initialisation with default value
        this.setState(State.INITIALISATION);
        this.setNoteTwenty(DEFAULT);
        this.setNoteFifty(DEFAULT);
    }

    public int getNoteTwenty() {
        return this.app.get(noteTwenty);
    }

    public int getNoteFifty() {
        return this.app.get(noteFifty);
    }

    public void setNoteTwenty(int val) {
        this.app.put(noteTwenty, val);
    }

    public void setNoteFifty(int val) {
        this.app.put(noteFifty, val);
    }

    public void setState(State state) {
        this.state = state;
    }

    public void checkState() {
        if (this.getNoteTwenty() == 0 && this.getNoteFifty() == 0) {
            this.setState(State.WAITING);
        } else if (this.getTotalAmount() <= THRESHOLD) {
            System.out.println("Need to be re-supplied...");
        }
        this.setState(State.WORKING);
    }

    public int getTotalAmount() {
        return this.noteTwenty * this.getNoteTwenty() + this.noteFifty * this.getNoteFifty();
    }

    public boolean isDoable(int val) {
        /* General failure.
        Recursive function is not very efficient.
        Add this function can improve efficiency.
         */
        return val <= this.getTotalAmount();
    }

    public boolean canGetCombination(int val, int numFifty, int numTwenty) {
        if (val == 0 || numFifty < 0 || numTwenty < 0) {
            this.tempNumFifty = numFifty;
            this.tempNumTwenty = numTwenty;
            return val == 0 && numFifty >= 0 && numTwenty >= 0;
        }
        if (canGetCombination(val - 50, numFifty - 1, numTwenty)) {
            return true;
        }
        return canGetCombination(val - 20, numFifty, numTwenty - 1);
    }

    public void withdrawMoney() {
        int desiredAmount;
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter desired amount: ");
        desiredAmount = myScanner.nextInt();
        if (this.isDoable(desiredAmount) && this.canGetCombination(desiredAmount, this.getNoteFifty(), this.getNoteTwenty())) {
            this.setNoteFifty(this.tempNumFifty);
            this.setNoteTwenty(this.tempNumTwenty);
            System.out.println("Transaction accepted...Please wait...");
        } else {
            System.out.println("Your desired amount is unavailable, please try another amount later...");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CashDispensingApp atm = new CashDispensingApp();

        while (true) {
            switch (atm.state) {
                case INITIALISATION: {
                    System.out.println("Initializing system...");
                    System.out.println(atm.getNoteTwenty() + " $20 notes left.");
                    System.out.println(atm.getNoteFifty() + " $50 notes left.");
                    atm.checkState();
                }
                case WORKING: {
                    atm.withdrawMoney();
                    atm.checkState();
                }
                case WAITING: {
                    System.out.println("No cash available...");
                    /* Do resupply, and reset the state to INITIALISATION here
                    * Can have a interface with some hardware */
                }
            }
            TimeUnit.SECONDS.sleep(3);
        }

    }
}
