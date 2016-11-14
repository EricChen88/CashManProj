package au.com.suncorp.cashman.cashDrawer;

import au.com.suncorp.cashman3001.cashDrawer.CashDrawer;
import au.com.suncorp.cashman3001.moneyImpl.Note;
import au.com.suncorp.cashman3001.exception.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by u339240 on 9/11/2016.
 */
public class CashDrawerTest {

    private CashDrawer cashDrawer;

    @Before
    public void setup(){
        //set up 10 pieces of 20 dollar notes
        cashDrawer = new CashDrawer(new Note(new BigDecimal(20)),10);
    }

    @Test
    public void removeMoneyTest(){
        try {
            cashDrawer.removeMoney(3);
        }
        catch (Exception ex)
        {

        }
        Assert.assertEquals(7, cashDrawer.getQuantity());

    }

    @Test(expected = InsufficientFundException.class)
    public void removeTooMuchMoneyTest() throws Exception{
        try {
            cashDrawer.removeMoney(11);
        }
        catch (Exception ex)
        {
            throw ex;
        }


    }
    @Test
    public void addMoneyTest(){
        cashDrawer.addMoney(3);
        Assert.assertEquals(13, cashDrawer.getQuantity());

    }
}
