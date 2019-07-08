package pw.komarov.terminals.exceptions;

public class NoFundsException extends BankTerminalException {
    public NoFundsException() {
        super("Terminal is empty!");
    }

    public NoFundsException(String message) {
        super(message);
    }
}
