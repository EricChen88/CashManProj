package au.com.suncorp.cashman.atm;

import au.com.suncorp.cashman3001.atm.AtmUnit;

import au.com.suncorp.cashman3001.cashDrawer.CashDrawer;
import au.com.suncorp.cashman3001.exception.InsufficientFundException;
import au.com.suncorp.cashman3001.exception.InvalidUserAmount;
import au.com.suncorp.cashman3001.exception.NoValidCombination;
import au.com.suncorp.cashman3001.interfaces.Money;
import au.com.suncorp.cashman3001.moneyImpl.Note;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by u339240 on 10/11/2016.
 */
public class AtmUnitTest {
    private AtmUnit atmUnit;

    @Before
    public void setup(){
        atmUnit = new AtmUnit();
    }

    @Test
    public void getDistinctMoneyTypeListTest() {
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(20)),5));
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(20)),10));
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(50)), 5));

        Assert.assertEquals(2, atmUnit.getDistinctMoneyTypeList().size());
        Assert.assertEquals(50, atmUnit.getDistinctMoneyTypeList().get(0).getDenomination().intValue());

    }

    @Test
    public void withdrawTest1(){
        List<Exception> errors = new ArrayList();
        List<Pair<Money, Integer>> result = new ArrayList<>();

        //withdraw with no loaded money
        result = atmUnit.withdraw(new BigDecimal(60), errors);
        Assert.assertTrue(result.size()==0);
        Assert.assertTrue(errors.get(0).getClass() == InsufficientFundException.class);


        //withdraw invalid amount
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(20)), 5));
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(50)), 2));
        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(0), errors);
        Assert.assertTrue(result.size() == 0);
        Assert.assertTrue(errors.get(0).getClass() == InvalidUserAmount.class);

        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(-10), errors);
        Assert.assertTrue(result.size() == 0);
        Assert.assertTrue(errors.get(0).getClass() == InvalidUserAmount.class);

        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(10.53), errors);
        Assert.assertTrue(result.size()==0);
        Assert.assertTrue(errors.get(0).getClass() == NoValidCombination.class);

        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(220), errors);
        Assert.assertTrue(result.size()==0);
        Assert.assertTrue(errors.get(0).getClass() == InsufficientFundException.class);

        //withdraw 100
        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(100), errors);
        Assert.assertTrue(result.size()>0);
        Assert.assertTrue(errors.size()==0);
        Assert.assertEquals(atmUnit.getTotalFund(), new BigDecimal(100));

        //another 50 not possible, no money reduced
        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(50), errors);
        Assert.assertTrue(result.size()==0);
        Assert.assertTrue(errors.get(0).getClass() == NoValidCombination.class);
        Assert.assertEquals(atmUnit.getTotalFund(),new BigDecimal(100));

        //withdraw 60
        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(60), errors);
        Assert.assertTrue(result.size() > 0);
        Assert.assertTrue(errors.size()==0);
        Assert.assertEquals(atmUnit.getTotalFund(), new BigDecimal(40));

        //another 60, not enough left
        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(60), errors);
        Assert.assertTrue(result.size() == 0);
        Assert.assertTrue(errors.get(0).getClass() == InsufficientFundException.class);
        Assert.assertEquals(atmUnit.getTotalFund(),new BigDecimal(40));

        //another 40, 0 left
        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(40), errors);
        Assert.assertTrue(result.size() > 0);
        Assert.assertTrue(errors.size()==0);
        Assert.assertEquals(atmUnit.getTotalFund(), new BigDecimal(0));

        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(10.53), errors);
        Assert.assertTrue(result.size() == 0);
        Assert.assertTrue(errors.get(0).getClass() == InsufficientFundException.class);

        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(20), errors);
        Assert.assertTrue(result.size()==0);
        Assert.assertTrue(errors.get(0).getClass() == InsufficientFundException.class);

        atmUnit.addMoney(new Pair<>(new Note(new BigDecimal(20)), 8));
        atmUnit.addMoney(new Pair<>(new Note(new BigDecimal(50)), 3));
        errors = new ArrayList();
        result = new ArrayList<>();
        result = atmUnit.withdraw(new BigDecimal(200), errors);
        Assert.assertTrue(result.size() > 0);
        Assert.assertTrue(errors.size() == 0);
        Assert.assertEquals(atmUnit.getTotalFund(), new BigDecimal(110));

    }

    @Test
    public void checkFundAvailTest(){
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(20)), 9));
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(50)), 5));
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(50)), 5));
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(100)), 0));

        Assert.assertFalse(atmUnit.checkFundAvail(new Pair<>(new Note(new BigDecimal(5)), 11)));
        Assert.assertTrue(atmUnit.checkFundAvail(new Pair<>(new Note(new BigDecimal(20)), 2)));
        Assert.assertTrue(atmUnit.checkFundAvail(new Pair<>(new Note(new BigDecimal(50)), 2)));
        Assert.assertFalse(atmUnit.checkFundAvail(new Pair<>(new Note(new BigDecimal(20)), 20)));
        Assert.assertTrue(atmUnit.checkFundAvail(new Pair<>(new Note(new BigDecimal(50)), 6)));
        Assert.assertFalse(atmUnit.checkFundAvail(new Pair<>(new Note(new BigDecimal(50)), 11)));
        Assert.assertFalse(atmUnit.checkFundAvail(new Pair<>(new Note(new BigDecimal(100)), 11)));

    }

    @Test
    public void generateComboTest3() {
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(20)), 5));
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(50)), 2));
        BigDecimal retVal = new BigDecimal(0);
        List<Pair<Money, Integer>> result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(220), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(200), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(500), result);

        Assert.assertTrue(result.size()==0);

    }

    @Test
    public void generateComboTest2() {
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(20)), 8));
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(50)), 3));
        BigDecimal retVal = new BigDecimal(0);
        List<Pair<Money, Integer>> result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(50.55), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(20), result);


        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(50), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(60), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(70), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(80), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(90), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(100), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(120), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(130), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(200), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(320), result);

        Assert.assertTrue(result.size()==0);



    }

    @Test
    public void generateComboTest(){
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(20)), 10));
        atmUnit.addCashDrawer(new CashDrawer(new Note(new BigDecimal(50)), 5));
        BigDecimal retVal = new BigDecimal(0);
        List<Pair<Money,Integer>> result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(5), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(10), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(15), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(20), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(30), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(40), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(50), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(55), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(60), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(65), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(70), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(80), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(85), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(90), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(95), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(100), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(105), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(106), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(110), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(120), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(125), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(130), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(140), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(150), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(160), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(170), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(180), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(190), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(200), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(300), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(400), result);

        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(500), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(700), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(1000), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(1200), result);
        result = new ArrayList<>();
        retVal = atmUnit.generateCombination(atmUnit.getDistinctMoneyTypeList(), new BigDecimal(50), new BigDecimal(1500), result);

        Assert.assertTrue(retVal.compareTo(new BigDecimal(1500))==0);
    }


}
