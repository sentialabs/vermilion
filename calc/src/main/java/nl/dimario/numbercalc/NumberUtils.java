package nl.dimario.numbercalc;

public class NumberUtils {

    public static Number multdiv(Number a, Number b, boolean doDiv) {
        boolean mustFloat = false;
        if(a instanceof Float || a instanceof Double) mustFloat = true;
        if(b instanceof Float || b instanceof Double) mustFloat = true;
        return mustFloat ? multdivFloat(a,b,doDiv) : multdivInt(a,b,doDiv);
    }

    private static Number multdivFloat(Number a, Number b, boolean doDiv) {
        double aa = a.doubleValue();
        double bb = b.doubleValue();
        return doDiv ? aa / bb  : aa * bb;
    }
    private static Number multdivInt(Number a, Number b, boolean doDiv) {
        long aa = a.longValue();
        long bb = b.longValue();
        return doDiv ? aa / bb  : aa * bb;
    }

    public static Number plusmin(Number a, Number b, boolean doAdd) {
        boolean mustFloat = false;
        if(a instanceof Float || a instanceof Double) mustFloat = true;
        if(b instanceof Float || b instanceof Double) mustFloat = true;
        return mustFloat ? plusminFloat(a,b,doAdd) : plusminInt(a,b,doAdd);
    }

    private static Number plusminFloat(Number a, Number b, boolean doAdd) {
        double aa = a.doubleValue();
        double bb = b.doubleValue();
        return doAdd ? aa + bb  : aa - bb;
    }
    private static Number plusminInt(Number a, Number b, boolean doAdd) {
        long aa = a.longValue();
        long bb = b.longValue();
        return doAdd ? aa + bb  : aa - bb;
    }
}
