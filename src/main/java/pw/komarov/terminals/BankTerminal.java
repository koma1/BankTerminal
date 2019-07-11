package pw.komarov.terminals;

import lombok.NonNull;
import pw.komarov.terminals.exceptions.AmountNotMultipleException;
import pw.komarov.terminals.exceptions.BankTerminalException;
import pw.komarov.terminals.exceptions.NoExchangeException;
import pw.komarov.terminals.exceptions.NoFundsException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BankTerminal {
    private final Map<Integer,Integer> balance = new HashMap<>();

    public synchronized void pushMoney(Map<Integer, Integer> money) {
        money.forEach(this::pushMoney);
    }

    public synchronized void pushMoney(@NonNull Integer banknoteNominal, int count) {
        if((banknoteNominal <= 0) || (count <= 0))
            throw new IllegalArgumentException("Banknote nominal or number of notes can't be less zero!");

            balance.put(banknoteNominal, balance.getOrDefault(banknoteNominal, 0) + count);
    }

    public synchronized int getAvailableTotalAmount() {
        AtomicInteger result = new AtomicInteger();

        balance.forEach((nominal, count) -> result.addAndGet(nominal * count));

        return result.get();
    }

    public synchronized Integer getBanknotesCount(int nominal) {
        return balance.getOrDefault(nominal, null);
    }

    public synchronized HashMap<Integer,Integer> requestAmount(int amount) throws NoFundsException, AmountNotMultipleException, NoExchangeException {
        int min = getMinNote();

        if(min < 1)
            throw new NoFundsException();

        if((amount % min) > 0)
            throw new AmountNotMultipleException(min);

        HashMap<Integer,Integer> result = requestExchange(amount);
        if(result == null)
            throw new NoExchangeException();

        //apply changes to balance
        result.forEach((nm, cnt) -> balance.put(nm, balance.getOrDefault(nm, 0) - cnt));

        return result;
    }

    private Integer getMinNote() {
        synchronized(balance) {
            return balance.keySet().stream().min(Integer::compareTo).orElse(-1);
        }
    }

    private HashMap<Integer,Integer> requestExchange(int amount) {
        if(amount <= 0)
            throw new IllegalArgumentException("Amount must be great than zero!");

        HashMap<Integer,Integer> result = new HashMap<>();

        Iterator<Integer> itr = balance.keySet().stream().sorted(Comparator.reverseOrder()).iterator();

        while(itr.hasNext()) { //for each, for all available banknotes nominal. from max to min (5000 -> n -> 50)
            int nm = itr.next(); //nominal
            int cnt = (amount / nm); //required banknotes count of this nominal
            int avail = balance.getOrDefault(nm, 0); //available banknotes count for this nominal
            cnt = Math.min(cnt, avail);
            amount -= (cnt * nm);

            if(cnt > 0)
                result.put(nm, result.getOrDefault(nm, 0) + cnt);
        }

        if(amount == 0)
            return result;
        else
            return null;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder("{");
            balance.keySet().stream().sorted().forEach(nominal ->
                    sb.append(String.format("'%d RUB': %d, ", nominal, balance.get(nominal))));
            if(!balance.isEmpty())
                sb.delete(sb.length() - 2, sb.length());

        return sb.append("}").toString();
    }
}
