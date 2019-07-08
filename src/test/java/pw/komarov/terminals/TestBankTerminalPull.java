import org.junit.jupiter.api.*;
import pw.komarov.terminals.BankTerminal;
import pw.komarov.terminals.exceptions.BankTerminalException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestBankTerminalPull {
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

    @Test
    @Order(0)
    void pullMoney() {
        Map<Integer, Integer> expected = new HashMap<>() {
            {
                put(5000,   1);
                put(2000,   1);
                put(1000,   1);
                put(500,    1);
                put(200,    1);
                put(100,    2);
                put(50,     1);
            }
        };

        try {
            Map<Integer, Integer> actual = bankTerminal.requestAmount(8950);
            assertEquals(expected, actual);
        }
        catch (BankTerminalException e) {
            fail(e);
        }
    }

    @Test
    @Order(1)
    void requestAmount() {
        assertEquals(bankTerminal.getAvailableTotalAmount(), 1451250);
    }

    @Test
    @Order(2)
    void balanceAsString() {
        assertEquals(bankTerminal.toString(), "{'50 RUB': 1099, '100 RUB': 848, '200 RUB': 0, '500 RUB': 499, '1000 RUB': 299, '2000 RUB': 9, '5000 RUB': 149}");
    }

    @Test
    @Order(3)
    void balanceStress() {
        try {
            assertEquals(149, bankTerminal.requestAmount(745000).get(5000)); //pulling all '5000RUB': amount = 745000, banknotes = 149
            assertEquals(9, bankTerminal.requestAmount(18000). get(2000)); //pulling all '2000RUB': amount = 18000, banknotes = 9
            assertEquals(298, bankTerminal.requestAmount(298000). get(1000)); //pulling (all-1) '1000RUB': amount = 298000, banknotes = 298

            //balance here: {'50 RUB': 1099, '100 RUB': 848, '200 RUB': 0, '500 RUB': 499, '1000 RUB': 1, '2000 RUB': 0, '5000 RUB': 0}

            assertEquals(new HashMap<Integer,Integer>() { //50=1, 500=15, 100=4, 1000=1
                {
                    put(50, 1);
                    put(100, 4);
                    put(500, 15);
                    put(1000, 1);
                }
            }, bankTerminal.requestAmount(8950));
        }
        catch (BankTerminalException e) {
            fail(e);
        }
    }

    @Test
    @Order(4)
    void balanceAfterStress() {
        assertEquals(bankTerminal.getAvailableTotalAmount(), 381300);
    }
}