import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import pw.komarov.terminals.BankTerminal;
import pw.komarov.terminals.exceptions.AmountNotMultipleException;
import pw.komarov.terminals.exceptions.NoExchangeException;
import pw.komarov.terminals.exceptions.NoFundsException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestBankTerminalAmounts {
    static final BankTerminal bankTerminal = new BankTerminal();

    @BeforeAll
    void setup() {
        bankTerminal.pushMoney(50, 1000);
        bankTerminal.pushMoney(100, 850);
        bankTerminal.pushMoney(200, 1);
        bankTerminal.pushMoney(500, 500);
        bankTerminal.pushMoney(1000, 300);
        bankTerminal.pushMoney(2000, 10);
        bankTerminal.pushMoney(5000, 100);
        bankTerminal.pushMoney(5000, 50);
        bankTerminal.pushMoney(50, 100);
    }

    @Order(0)
    @Test
    void testTotal() {
        assertEquals(1460200, bankTerminal.getAvailableTotalAmount());
    }

    @Order(1)
    @Test
    void testEachBanknote() {
        assertNull(bankTerminal.getBanknotesCount(0));

        assertEquals(1100, bankTerminal.getBanknotesCount(50));
        assertEquals(850, bankTerminal.getBanknotesCount(100));
        assertEquals(1, bankTerminal.getBanknotesCount(200));
        assertEquals(500, bankTerminal.getBanknotesCount(500));
        assertEquals(300, bankTerminal.getBanknotesCount(1000));
        assertEquals(10, bankTerminal.getBanknotesCount(2000));
        assertEquals(150, bankTerminal.getBanknotesCount(5000));
    }

    @Order(2)
    @Test
    void testThrows() {
        assertThrows(NoFundsException.class, () -> new BankTerminal().requestAmount(100));
        assertThrows(AmountNotMultipleException.class, () -> bankTerminal.requestAmount(33));
        assertThrows(NoExchangeException.class, () -> bankTerminal.requestAmount(100000000));

        assertThrows(IllegalArgumentException.class, () -> bankTerminal.pushMoney(null, 500));
        assertThrows(IllegalArgumentException.class, () -> bankTerminal.pushMoney(0, 500));
        assertThrows(IllegalArgumentException.class, () -> bankTerminal.pushMoney(500, 0));
        assertThrows(IllegalArgumentException.class, () -> bankTerminal.requestAmount(-1));
    }
}