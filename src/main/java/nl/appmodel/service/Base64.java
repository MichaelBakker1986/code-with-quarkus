package nl.appmodel.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
@Slf4j
@ToString
@Getter
@AllArgsConstructor
public class Base64 {
    static final String _Rixits =
            //   0       8       16      24      32      40      48      56     63
            //   v       v       v       v       v       v       v       v      v
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_-";
    // You have the freedom, here, to choose the glyphs you want for
    // representing your base-64 numbers. The ASCII encoding guys usually
    // choose a set of glyphs beginning with ABCD..., but, looking at
    // your update #2, I deduce that you want glyphs beginning with
    // 0123..., which is a fine choice and aligns the first ten numbers
    // in base 64 with the first ten numbers in decimal.
    // This cannot handle negative numbers and only works on the
    //     integer part, discarding the fractional part.
    // Doing better means deciding on whether you're just representing
    // the subset of javascript numbers of twos-complement 32-bit integers
    // or going with base-64 representations for the bit pattern of the
    // underlying IEEE floating-point number, or representing the mantissae
    // and exponents separately, or some other possibility. For now, bail
    public static String fromId(long number) {
        return fromNumber((number * 100) + 1);
    }
    static String fromNumber(long number) {
        if (number < 0) throw new RuntimeException();

        long rixit; // like 'digit', only in some non-decimal radix
        var  residual = (long) Math.floor(number);
        val  result   = new StringBuilder();
        while (true) {
            rixit = residual % 64;
            result.insert(0, _Rixits.charAt((int) rixit));
            residual = (long) Math.floor(residual >> 6);
            if (residual == 0)
                break;
        }
        return result.toString();
    }
    static long toNumber(String rixits) {
        var      result = 0;
        String[] chars  = rixits.split("");
        for (String aChar : chars) {
            result = (result * 64) + _Rixits.indexOf(aChar);
        }
        return result;
    }
}
