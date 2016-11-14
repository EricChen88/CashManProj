package au.com.suncorp.cashman3001.atm;

import au.com.suncorp.cashman3001.cashDrawer.CashDrawer;
import au.com.suncorp.cashman3001.exception.InsufficientFundException;
import au.com.suncorp.cashman3001.exception.InvalidUserAmount;
import au.com.suncorp.cashman3001.exception.NoValidCombination;
import au.com.suncorp.cashman3001.interfaces.Money;
import au.com.suncorp.cashman3001.moneyImpl.Note;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by u339240 on 10/11/2016.
 */
public class AtmUnit {
    private List<CashDrawer> cashDrawers = new ArrayList<>();

    public void addCashDrawer(CashDrawer cashDrawerToAdd)
    {
        cashDrawers.add(cashDrawerToAdd);
    }

    private boolean dispenseMoney(Pair<Money,Integer> combo)
    {
        List<CashDrawer> allAvailCashdraws = cashDrawers.stream().filter(cashDrawer -> cashDrawer.getMoney().getDenomination().compareTo(combo.getKey().getDenomination())==0 && cashDrawer.getQuantity()>0).collect(Collectors.toList());

        if(allAvailCashdraws.size() ==0)
            return false;

        if (allAvailCashdraws.get(0).getQuantity() >= combo.getValue()) {
            allAvailCashdraws.get(0).setQuantity(allAvailCashdraws.get(0).getQuantity() - combo.getValue());
            return true;
        } else {
            int allRemainingQuan = allAvailCashdraws.get(0).getQuantity();
            allAvailCashdraws.get(0).setQuantity(0);
            return dispenseMoney(new Pair<>(combo.getKey(),combo.getValue() - allRemainingQuan));
        }

    }

    public void addMoney(Pair<Money,Integer> combo)
    {
        CashDrawer target = cashDrawers.stream().filter(cashDrawer -> cashDrawer.getMoney().getDenomination().compareTo(combo.getKey().getDenomination()) == 0).findFirst().orElse(null);
        if(target == null)
        {
            addCashDrawer(new CashDrawer(new Note(combo.getKey().getDenomination()), combo.getValue()));
        }
        else {
            target.setQuantity(target.getQuantity() + combo.getValue());
        }
    }

    public List<Pair<Money, Integer>> withdraw(BigDecimal amount, List<Exception> errors){
        List<Pair<Money,Integer>> retResult= new ArrayList<>();

        if(cashDrawers.size() == 0)
        {
            errors.add(new InsufficientFundException());
            return retResult;
        }

        if(getTotalFund().compareTo(amount) == -1)
        {
            errors.add(new InsufficientFundException());
            return retResult;
        }

        if(amount.compareTo(new BigDecimal(0)) < 1)
        {
            errors.add(new InvalidUserAmount());
            return retResult;
        }
        List<Money> availMoneyTypes = getDistinctMoneyTypeList();

        BigDecimal retRemainder = generateCombination(availMoneyTypes,availMoneyTypes.get(0).getDenomination(),amount, retResult);
        if(retResult.size() > 0)
        {
            //valid withdraw and fund avail, proceed with withdraw
            retResult.stream().forEach(moneyIntegerPair -> dispenseMoney(moneyIntegerPair));
        }

        else
        {
            if(retRemainder.compareTo(getTotalFund())==1)
            {
                errors.add(new InsufficientFundException());
            }
            else
            {
                errors.add(new NoValidCombination());
            }

        }


        return retResult;

    }

    public BigDecimal getTotalFund()
    {
        BigDecimal retVal = new BigDecimal(0);

        for (int i = 0; i < cashDrawers.size(); i++) {
            retVal = retVal.add(cashDrawers.get(i).getMoney().getDenomination().multiply(new BigDecimal(cashDrawers.get(i).getQuantity())));
        }

        return retVal;
    }

    public boolean checkFundAvail(Pair<Money,Integer> toCheck)
    {
        int totalAvailCounter = cashDrawers.stream().filter(cashDrawer -> {
            return cashDrawer.getMoney().getDenomination().compareTo(toCheck.getKey().getDenomination())==0;
        }).mapToInt(value -> value.getQuantity()).sum();

        return totalAvailCounter >= toCheck.getValue();
    }

    //Given the list of money types to check, lazy check from the start denomination and amount given for possible combination, considering available funds
    // Remainder is returned
    public BigDecimal generateCombination(List<Money> moneyTypes, BigDecimal checkDenom, BigDecimal checkAmt, List<Pair<Money, Integer>> comboRslt)
    {
        List<Pair<Money,Integer>> retResult= new ArrayList<>();
        BigDecimal[] divRslt = checkAmt.divideAndRemainder(checkDenom);
        BigDecimal currentQuantity = new BigDecimal(0);
        BigDecimal currentRemainder = new BigDecimal(0);

        for (int i = 0; i <= divRslt[0].intValue(); i++) {
            currentQuantity = divRslt[0].subtract(new BigDecimal(i));
            currentRemainder = divRslt[1].add(checkDenom.multiply(new BigDecimal(i)));

            Pair<Money,Integer> combo = new Pair<>(new Note(checkDenom), currentQuantity.intValue());
            if(checkFundAvail(combo))            {
                if(currentRemainder.compareTo(new BigDecimal(0))==0 && currentQuantity.compareTo(new BigDecimal(0))==1){
                    //valid combo
                    retResult.add(combo);
                }
                else if(checkDenom.compareTo(moneyTypes.get(moneyTypes.size()-1).getDenomination())!=0 && currentRemainder.compareTo(new BigDecimal(0))==1)
                {
                    //more denom exists, continue checking
                    //get the next demon to check
                    Money nextDenom = moneyTypes.stream().filter(money -> money.getDenomination().compareTo(checkDenom) == -1).findFirst().orElse(null);

                    if(nextDenom != null) {
                        List<Pair<Money,Integer>> childVal = new ArrayList<>();
                        currentRemainder = generateCombination(moneyTypes, nextDenom.getDenomination(), currentRemainder, childVal);

                        if(childVal.size() > 0)
                        {
                            if(combo.getValue().intValue() > 0)
                            {
                                retResult.add(combo);
                            }
                            retResult.addAll(childVal);
                        }
                    }
                }

                if (retResult.size()>0)
                {
                    //found a valid combination now for all notes types below current denomination
                    break;
                }
            }

        }

        comboRslt.addAll(retResult);
        return currentRemainder;


    }



    public List<Money> getDistinctMoneyTypeList()
    {
        List<Money> retVal = new ArrayList<>();

        cashDrawers.stream().forEach(cashDrawer -> {
            if(!retVal.stream().anyMatch(money -> money.getDenomination().compareTo(cashDrawer.getMoney().getDenomination())==0)) {
                retVal.add(cashDrawer.getMoney());
            }
        });

        return retVal.stream().sorted((o1, o2) -> {return o2.getDenomination().compareTo(o1.getDenomination());}).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < cashDrawers.size(); i++) {
            s.append(cashDrawers.get(i).getMoney().getDenomination() + ": " + cashDrawers.get(i).getQuantity() + "\n");
        }
        return s.toString();
    }

}
