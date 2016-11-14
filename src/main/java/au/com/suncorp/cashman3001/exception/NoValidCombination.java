package au.com.suncorp.cashman3001.exception;

import au.com.suncorp.cashman3001.Utils;

/**
 * Created by u339240 on 13/11/2016.
 */
public class NoValidCombination extends Exception{
    public NoValidCombination(String message) {
        super(message);
    }

    public NoValidCombination() {
        super(Utils.msgNoValidCombo);
    }
}
