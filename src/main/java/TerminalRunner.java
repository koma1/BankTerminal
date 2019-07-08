import pw.komarov.terminals.BankTerminal;
import pw.komarov.terminals.exceptions.BankTerminalException;

public class TerminalRunner {

    public static void main(String[] args) {
        BankTerminal terminal = new BankTerminal();

        terminal.pushMoney(50, 1000);
        terminal.pushMoney(100, 850);
        terminal.pushMoney(200, 1);
        terminal.pushMoney(500, 500);
        terminal.pushMoney(1000, 300);
        terminal.pushMoney(2000, 10);
        terminal.pushMoney(5000, 100);
        terminal.pushMoney(5000, 50);
        terminal.pushMoney(50, 100);

        System.out.printf("Terminal balance: %s\n", terminal.getAvailableTotalAmount());
        System.out.printf("Terminal banknotes: %s\n", terminal);

        try {
            System.out.printf("Banknotes pulled: %s\n", terminal.requestAmount(8950).toString());

            System.out.printf("Terminal balance (after pull): %s\n", terminal.getAvailableTotalAmount());
            System.out.printf("Terminal banknotes (after pull): %s\n", terminal);
        }
        catch(BankTerminalException e) {
           e.printStackTrace();
        }
    }
}
