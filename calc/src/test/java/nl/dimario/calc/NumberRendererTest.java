package nl.dimario.calc;


import nl.dimario.numbercalc.NumberRenderer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberRendererTest {

    private Map<String,Number> context;

    @BeforeEach
    public void setup() {
        context = new HashMap<>();
    }

    @Test
    public void testConstants() {

        ParseTree tree = TestUtils.parseString("${n3.21}");
        NumberRenderer renderer = new NumberRenderer(context);
        Number number = renderer.visit(tree);
        assertEquals( 3.21D, number);

        tree = TestUtils.parseString("${n321}");
        number = renderer.visit(tree);
        assertEquals( 321L, number);
    }
}
