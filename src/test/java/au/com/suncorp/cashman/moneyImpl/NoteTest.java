package au.com.suncorp.cashman.moneyImpl;

import au.com.suncorp.cashman3001.moneyImpl.Note;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Created by u339240 on 9/11/2016.
 */
public class NoteTest {

    @Test
    public void setDenomination(){
        Note testNote = new Note(new BigDecimal(20));
        Assert.assertEquals(new BigDecimal(20), testNote.getDenomination());

        testNote = new Note(new BigDecimal(50));
        Assert.assertEquals(new BigDecimal(50), testNote.getDenomination());
    }
}
