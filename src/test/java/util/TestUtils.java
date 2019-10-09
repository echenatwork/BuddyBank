package util;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class TestUtils {

    private static final double DEFAULT_BIG_DECIMAL_EQUALITY_DELTA = 0.001;

    public static void assertRoughlyEquals(BigDecimal expected, BigDecimal actual, double delta) {
        assertEquals(expected.doubleValue(), actual.doubleValue(), delta);
    }

    public static void assertRoughlyEquals(BigDecimal expected, BigDecimal actual) {
        assertEquals(expected.doubleValue(), actual.doubleValue(), DEFAULT_BIG_DECIMAL_EQUALITY_DELTA);
    }

}
