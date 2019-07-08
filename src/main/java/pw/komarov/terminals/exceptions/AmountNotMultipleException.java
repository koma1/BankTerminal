package pw.komarov.terminals.exceptions;

public class AmountNotMultipleException extends BankTerminalException {
    public AmountNotMultipleException(int multiple) {
        super(String.format("Amount must be a multiple of %d", multiple));
    }
}
