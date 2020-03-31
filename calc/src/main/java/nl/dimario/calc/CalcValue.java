package nl.dimario.calc;

/**
 * This bean represents a value that can be used in the calc expression language evaluator.
 * For the relevant types you can create a value from String and also obtain a
 * String representation from the underlying type.
 * Currently supported values types are BigDecimal for numbers (both Integer and floating point),
 * LocalDate for dates and custom type DateLapse for denoting a time interval .
 */
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class CalcValue {

    public static CalcValue VOID = new CalcValue( null);

    private static final String DATEFORMAT = "dd-MM-yyyy";
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern( DATEFORMAT);


    private Object value;

    private CalcValue( Object value) {
        this.value = value;
    }

    static public CalcValue createDateValue( LocalDate val) {
        return new CalcValue( val);
    }

    static public CalcValue createDateValue( String val) {
        LocalDate date = LocalDate.parse( val,  dateFormatter);
        return new CalcValue( date);
    }

    static public CalcValue createPeriodValue( String val) {
        int cutoff = val.length() - 1;
        int amount = Integer.parseInt( val.substring(0, cutoff));
        Period p = null;
        switch( val.charAt( cutoff)) {
            case 'D' : p = Period.ofDays( amount); break;
            case 'M' : p = Period.ofMonths( amount); break;
            case 'Y' : p = Period.ofYears( amount); break;
        }
        return new CalcValue( p);
    }

    static public CalcValue createNumberValue( BigDecimal val) {
        return new CalcValue( val);
    }

    static public CalcValue createNumberValue( String val) {
        BigDecimal bd = new BigDecimal( val);
        return new CalcValue( bd);
    }

    public String toString() {
        if( value == null) {
            return "(null value)";
        }
        if( value instanceof BigDecimal) {
            return NumberFormat.getNumberInstance().format( (BigDecimal) value);
        }
        if( value instanceof LocalDate) {
            return ((LocalDate) this.value).format( dateFormatter);
        }
        if( value instanceof Period) {
            return ((Period) this.value).toString();
        }
        return "CalcValue does not support Java type " + this.value.getClass().getSimpleName();
    }

    public String getType() {
        if( this.value == null) {
            return "(null value)";
        }
        return this.value.getClass().getTypeName();
    }

    public BigDecimal getBigDecimal() {
        return (BigDecimal) this.value;
    }

    public LocalDate getDate() {
        return (LocalDate) this.value;
    }

    public Period getPeriod() {
        return (Period) this.value;
    }
    public boolean isBigDecimal() {
        return value instanceof BigDecimal;
    }

    public boolean isLocalDate() {
        return value instanceof LocalDate;
    }

    public boolean isPeriod() {
        return value instanceof Period;
    }
}
