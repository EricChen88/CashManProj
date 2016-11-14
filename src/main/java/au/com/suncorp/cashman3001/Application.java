package au.com.suncorp.cashman3001;

import au.com.suncorp.cashman3001.atm.AtmUnit;
import au.com.suncorp.cashman3001.cashDrawer.CashDrawer;
import au.com.suncorp.cashman3001.interfaces.Money;
import au.com.suncorp.cashman3001.moneyImpl.Note;
import javafx.util.Pair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by u339240 on 8/11/2016.
 */
public class Application {
    public static void main(String[] args) {
        AtmUnit atmUnit = new AtmUnit();
        addFund(atmUnit);
        processUserInput(atmUnit);
    }

    private static void printStandPrompt(){
        System.out.println("User options: 1-Withdraw 2-Check Balance 3-Add Money 0-Quit");
    }

    private static void printTotal(AtmUnit atmUnit)
    {
        System.out.print(atmUnit.toString());
        System.out.println("Total: $" + atmUnit.getTotalFund());
    }

    private static void addFund(AtmUnit atmUnit){
        Scanner inScan = new Scanner(System.in);

        System.out.println("Enter the number of $50 dollar notes: ");
        int userVal = -1;
        while(userVal == -1)
        {
            Integer userValPending = Utils.parseIntger(inScan.next());
            if(userValPending != null && userValPending.intValue() >= 0)
            {
                userVal = userValPending.intValue();
                atmUnit.addMoney(new Pair<>(new Note(new BigDecimal(50)), userVal));
            }
            else {
                System.out.println("Invalid input");
                System.out.println("Enter the number of $50 dollar notes: ");
            }
        }

        System.out.println("Enter the number of $20 dollar notes: ");
        userVal = -1;
        while(userVal == -1)
        {
            Integer userValPending = Utils.parseIntger(inScan.next());
            if(userValPending != null && userValPending.intValue() >= 0)
            {
                userVal = userValPending.intValue();
                atmUnit.addMoney(new Pair(new Note(new BigDecimal(20)), userVal));
            }
            else {
                System.out.println("Invalid input");
                System.out.println("Enter the number of $20 dollar notes: ");
            }
        }
    }

    private static void processWithdraw(AtmUnit atmUnit){
        Scanner inScan = new Scanner(System.in);
        System.out.println("Enter the amount to withdraw:");
        Integer userValPending = Utils.parseIntger(inScan.next());
        if(userValPending != null && userValPending.intValue() > 0)
        {

            int userVal = userValPending.intValue();
            List<Exception> errors = new ArrayList();
            List<Pair<Money, Integer>> result = new ArrayList<>();

            result = atmUnit.withdraw(new BigDecimal(userVal), errors);
            if(result.size()>0)
            {
                System.out.print("OK:");
                result.forEach(moneyIntegerPair -> {
                    System.out.printf("$%dX%d ", moneyIntegerPair.getKey().getDenomination().intValue(), moneyIntegerPair.getValue());
                });
                System.out.println();

            }
            errors.forEach(e -> System.out.println(e.getMessage()));
        }
        else {
            System.out.println("Invalid input");
        }
    }
    
    private static void processUserInput(AtmUnit atmUnit){
        int userVal = -1;
        Scanner inScan = new Scanner(System.in);
        
        while(userVal != 0)
        {
            printStandPrompt();
            Integer userValPending = Utils.parseIntger(inScan.next());
            if(userValPending != null && userValPending.intValue() >= 0 && userValPending <= 3)
            {
                userVal = userValPending.intValue();
                if (userVal == 1)
                {
                    //withdraw
                    processWithdraw(atmUnit);
                }
                else if(userVal == 2)
                {
                    printTotal(atmUnit);
                }
                else if(userVal == 3)
                {
                    addFund(atmUnit);
                }
            }
            else {
                System.out.println("Invalid input");
                printStandPrompt();
            }
        }
    }

}
