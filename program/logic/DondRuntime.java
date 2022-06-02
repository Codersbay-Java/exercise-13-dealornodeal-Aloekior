package logic;

import java.util.*;

public class DondRuntime {
    private static Map<Integer, Double> suitcases = new HashMap<>();
    private static double playerValue = 0;
    private static final int[] rounds = {6,5,4,3,2,1,1,1,1};
    private static int userCase;
    private static Scanner scanner = new Scanner(System.in);
    private static double bankOffer = 0;

    public DondRuntime() {
        RunGame();
    }

    private static void RunGame() {
        boolean bankDeal = false;
        createSuitcases();
        userCase = userSelection();
        playerValue = removeCase(userCase);
        printRemainingCases();

        for (int i = 0; i < rounds.length; i++) {
            while (rounds[i] > 0) {
                int caseToRemove = userSelection();
                System.out.println("Case [" + caseToRemove + "] contained: $" + removeCase(caseToRemove));
                rounds[i]--;
                printRemainingCases();
            }
            double bankOffer = getBankOffer(i + 1);

            System.out.println("The bank offers $" + bankOffer + " for your case — DEAL OR NO DEAL? (Enter 'DEAL' or press any button)");
            if (scanner.nextLine().equalsIgnoreCase("deal")) {
                bankDeal = true;
                break;
            }
            printRemainingCases();
        }
        finale(bankDeal);
    }

    private static void createSuitcases() {
        Double[] values = {0.01, 1d, 5d, 10d, 25d, 50d, 75d, 100d, 200d, 300d, 400d, 500d, 750d, 1000d, 5000d,
                10000d, 25000d, 50000d, 75000d, 100000d, 200000d, 300000d, 400000d, 500000d, 750000d, 1000000d};

        if (!debugSetup()) {
            List<Double> shuffled = new ArrayList<>(Arrays.stream(values).toList());
            Collections.shuffle(shuffled);
            values = shuffled.toArray(new Double[0]);
        }

        for (int i = 0; i < values.length; i++) {
            suitcases.put(i + 1, values[i]);
        }
        printRemainingCases();
    }

    private static boolean debugSetup() {
        System.out.print("Enter D/d for debugging mode ");
        if (scanner.nextLine().equalsIgnoreCase("d")) {
            return true;
        }
        return false;
    }

    private static double getBankOffer(int currentRound) {
        double totalValue = playerValue;

        for (Double v : suitcases.values()) {
            totalValue += v;
        }
        return Math.round(totalValue / suitcases.size() * currentRound / 10);
    }

    private static int userSelection() {
        int input;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (playerValue == 0) {
                System.out.print("Please choose your suitcase 1-26 with your price: ");
            } else {
                System.out.print("Pick a suitcase to eliminate from the game: ");
            }

            try {
                input = scanner.nextInt();
                if (!suitcases.containsKey(input)) {
                    throw new InputMismatchException();
                }
                return input;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Try again.");
                scanner.nextLine();
            }
        }
    }

    private static double removeCase(int caseNumber) {
        double value = suitcases.get(caseNumber);
        suitcases.remove(caseNumber);
        return value;
    }

    private static void printRemainingCases() {
        for (int key : suitcases.keySet()) {
            System.out.print("[" + key + "]");
        }
        System.out.println();
    }

    private static void finale(boolean bankDeal) {
        Integer lastCase = (Integer) suitcases.keySet().stream().toArray()[0];
        System.out.println("Case [" + lastCase + "] contained: $" + removeCase(lastCase));
        if (bankDeal) {
            System.out.println("Congratulations, you accepted the banks offer of $" + bankOffer);
        }
        System.out.println("Your selected case [" + userCase + "] contained $" + playerValue);
        System.out.println("Thank you for playing DEAL OR NO DEAL!");
    }
}