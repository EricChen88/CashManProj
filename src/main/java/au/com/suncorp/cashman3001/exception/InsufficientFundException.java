package au.com.suncorp.cashman3001.exception;

import au.com.suncorp.cashman3001.Utils;

/**
 * Created by u339240 on 9/11/2016.
 */
public class InsufficientFundException extends Exception {

    public InsufficientFundException(String message) {
        super(message);
    }

    public InsufficientFundException() {
        super(Utils.msgInsufficientFund);
    }


}
