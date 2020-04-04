package nl.dimario.calc;

import nl.dimario.xxx.CalcParser;
import nl.dimario.xxx.CalcParserBaseVisitor;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;

public class CalcRenderer extends CalcParserBaseVisitor<CalcValue> {

    private static final int DEFAULTSIZE = 10000;
    private StringBuilder output;

    public CalcRenderer() {
        this.output = new StringBuilder( DEFAULTSIZE);
    }

    @Override
    public CalcValue visitStatictext(CalcParser.StatictextContext ctx) {
        String staticText = ctx.TEXT().getText();
        output.append( staticText);
        return super.visitStatictext(ctx);
    }

    @Override
    public CalcValue visitResult(CalcParser.ResultContext ctx) {
        try {
            CalcValue value = visit(ctx.expression());
            output.append(value.toString());
        } catch( CalcException cx) {
            output.append( "*** ERROR ");
            output.append( cx.getMessage());
            output.append( " ***");
        }
        return CalcValue.VOID;
    }

    @Override
    public CalcValue visitConstant(CalcParser.ConstantContext ctx) {
        return super.visitConstant(ctx);
    }

    @Override
    public CalcValue visitNumberconst(CalcParser.NumberconstContext ctx) {
        return CalcValue.createNumberValue( ctx.NUMBER().getText());
    }

    @Override
    public CalcValue visitLapseconst(CalcParser.LapseconstContext ctx) {
        return CalcValue.createPeriodValue( ctx.LAPSE().getText());
    }

    @Override
    public CalcValue visitDateconst(CalcParser.DateconstContext ctx) {
        return CalcValue.createDateValue( ctx.DATE().getText());
    }

    @Override
    public CalcValue visitBraces(CalcParser.BracesContext ctx) {
        return visit( ctx.expression());
    }

    @Override
    public CalcValue visitMultdiv(CalcParser.MultdivContext ctx) {
        return multdiv(ctx);
    }

    @Override
    public CalcValue visitAddsub(CalcParser.AddsubContext ctx) {
        return addsub(ctx);
    }

    @Override
    public CalcValue visitFunction(CalcParser.FunctionContext ctx) {
        CalcValue val = visit( ctx.expression());
        if( ! val.isLocalDate()) {
            throw new CalcException( "Can only call functions on Date value");
        }
        String funcName = ctx.func().getText();
        return callFunction( funcName, val);
    }

    public CalcValue multdiv(CalcParser.MultdivContext ctx) {
        CalcValue v1 = visit( ctx.expression( 0));
        CalcValue v2 = visit( ctx.expression( 1));
        if( v1.isBigDecimal() && v2.isBigDecimal()) {
            boolean isMult = ctx.MULT() != null;
            if( isMult) {
                return CalcValue.createNumberValue(v1.getBigDecimal().multiply(v2.getBigDecimal()));
            }
            return CalcValue.createNumberValue(v1.getBigDecimal().divide(v2.getBigDecimal(), 13, RoundingMode.HALF_EVEN));
        }
        throw new CalcException( String.format( "Cannot multiply or divide %s and %s", v1.getType(), v2.getType()));
    }

    public CalcValue addsub( CalcParser.AddsubContext ctx) {
        CalcValue v1 = visit( ctx.expression( 0));
        CalcValue v2 = visit( ctx.expression( 1));
        boolean isAdd = ctx.PLUS() != null;
        if( v1.isBigDecimal() && v2.isBigDecimal()) {
            if( isAdd) {
                return CalcValue.createNumberValue(v1.getBigDecimal().add(v2.getBigDecimal()));
            }
            return CalcValue.createNumberValue(v1.getBigDecimal().subtract(v2.getBigDecimal()));
        }
        if( v1.isLocalDate() && v2.isPeriod()) {
            LocalDate start = v1.getDate();
            Period period =  v2.getPeriod();
            if( isAdd) {
                return CalcValue.createDateValue( start.plus( period));
            }
            return CalcValue.createDateValue( start.minus( period));
        }
        throw new CalcException( String.format( "Cannot add or subtract %s and %s", v1.getType(), v2.getType()));
    }

    private CalcValue callFunction( String funcName, CalcValue val) {

        LocalDate date = val.getDate();

        if( "firstOfYear".equals( funcName)) {
            date = date.with( TemporalAdjusters.firstDayOfYear());
        } else if( "lastOfYear".equals( funcName)) {
            date = date.with( TemporalAdjusters.lastDayOfYear());
        } else if( "firstOfMonth".equals( funcName)) {
            date = date.with(TemporalAdjusters.firstDayOfMonth());
        } else if( "lastOfMonth".equals( funcName)) {
            date = date.with(TemporalAdjusters.lastDayOfMonth());
        } else if( "firstOfQuarter".equals( funcName) ||
                   "lastOfQuarter".equals( funcName)) {
            LocalDate firstOfQuarter = date.with( date.getMonth().firstMonthOfQuarter())
               .with(TemporalAdjusters.firstDayOfMonth());
            if( "lastOfQuarter".equals( funcName)) {
                date = firstOfQuarter.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
            } else {
                date = firstOfQuarter;
            }
        }
        return CalcValue.createDateValue( date);
    }

    public String getOutput() {
        return this.output.toString();
    }

}
