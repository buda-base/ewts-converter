package io.bdrc.ewtsconverter;

import io.bdrc.ewtsconverter.EwtsConverter;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ConversionTest {

    public static final EwtsConverter converter = new EwtsConverter();

    public String toUnicode(String s, List<String>conversionWarnings) {
        String convertedValue = converter.toUnicode(s, conversionWarnings, true);
        System.out.println("converting \""+s+"\" into "+convertedValue);
        if (conversionWarnings.size() > 0) {
            System.out.println("with warnings: "+String.join(", ", conversionWarnings));
        }
        return convertedValue;
    }

    @Test
    public void textEwts() {
        List<String> conversionWarnings = new ArrayList<String>();
        String res = toUnicode("pa'ng", conversionWarnings);
        assertTrue(res.equals("པའང"));
        assertTrue(conversionWarnings.size()==0);
        conversionWarnings = new ArrayList<String>();
        res = toUnicode("be'u'i'o", conversionWarnings);
        assertTrue(res.equals("བེའུའིའོ"));
        assertTrue(conversionWarnings.size()==0);
        conversionWarnings = new ArrayList<String>();
        res = toUnicode("pa'm", conversionWarnings);
        assertTrue(res.equals("པའམ"));
        assertTrue(conversionWarnings.size()==0);
    }
}
