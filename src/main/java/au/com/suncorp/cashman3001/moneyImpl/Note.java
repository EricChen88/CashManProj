package au.com.suncorp.cashman3001.moneyImpl;

import au.com.suncorp.cashman3001.interfaces.Money;

import java.math.BigDecimal;

/**
 * Created by u339240 on 9/11/2016.
 */
public class Note implements Money {

    private BigDecimal denomination;

    @Override
    public BigDecimal getDenomination() {
        return denomination;

    }

    public Note(BigDecimal denominationIn){
        denomination = denominationIn;
    }
}
