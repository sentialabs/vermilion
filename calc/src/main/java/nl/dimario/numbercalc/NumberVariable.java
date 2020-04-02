package nl.dimario.numbercalc;

public class NumberVariable {

    private Number value;
    private String name;

    public NumberVariable(String name) {
        this.name = name;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
