package pw.komarov.terminals.exceptions;

public class NoExchangeException extends BankTerminalException {
    public NoExchangeException() {
        super("Sorry! We don't have requested exchange! Try another amount");
    }
}
