package au.com.suncorp.cashman3001;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by u339240 on 10/11/2016.
 */
public class Utils {

    public static final String msgInsufficientFund = "There is not enough fund.";
    public static final String msgInvalidUserAmount = "The amount entered is invalid.";
    public static final String msgNoValidCombo = "Please only enter multiples of valid denominations.";

    public static Integer parseIntger(String s) {
        Integer retVal = null;
        try {
            retVal = Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return retVal;
        } catch(NullPointerException e) {
            return retVal;
        }
        // only got here if we didn't return false
        return retVal;
    }
}
