package au.com.suncorp.cashman3001.cashDrawer;

import au.com.suncorp.cashman3001.exception.InsufficientFundException;
import au.com.suncorp.cashman3001.interfaces.Money;

/**
 * Created by u339240 on 9/11/2016.
 */
public class CashDrawer {

    private int quantity;

    private Money money;

    public CashDrawer(Money moneyIn, int quantityIn){
        quantity = quantityIn;
        money  = moneyIn;
    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addMoney(int quantityIn) {
        quantity += quantityIn;
    }

    public void removeMoney(int quantityOut) throws InsufficientFundException{
        if(quantityOut > quantity)
            throw new InsufficientFundException("Insufficient fund");

        quantity -= quantityOut;
    }




}
