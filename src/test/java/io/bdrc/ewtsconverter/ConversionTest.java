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
        //System.out.println("converting \""+s+"\" into "+convertedValue);
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
        res = toUnicode("be’u'i'o", conversionWarnings);
        assertTrue(res.equals("བེའུའིའོ"));
        assertTrue(conversionWarnings.size()==0);
        conversionWarnings = new ArrayList<String>();
        res = toUnicode("pa'm", conversionWarnings);
        assertTrue(res.equals("པའམ"));
        assertTrue(conversionWarnings.size()==0);
    }

    @Test
    public void textDtsAlalcToEwts() {
    	EwtsConverter converterAlalc = new EwtsConverter(false, false, false, false, EwtsConverter.Mode.ALALC);
    	EwtsConverter converterDwts = new EwtsConverter(false, false, false, false, EwtsConverter.Mode.DTS);
        //System.out.println(converterDwts.toUnicode("Ḥdul-ba rnam-par-ḥbyed-pa"));
    	assertTrue(converterDwts.toUnicode("Ḥdul-ba rnam-par-ḥbyed-pa").equals("འདུལ་བ་རྣམ་པར་འབྱེད་པ"));
    	assertTrue(converterAlalc.toUnicode("Ri-gi-ā-ra").equals("རི་གི་ཨཱ་ར"));
    }
    
    @Test
    public void textEwtsToDtsAlalc() {
        //System.out.println(TransConverter.EwtsToAlalc("Ri gi A ra", false));
        assertTrue(TransConverter.EwtsToAlalc("Ri gi A ra", false).equals("ri-gi-ā-ra"));
        assertTrue(TransConverter.EwtsToAlalc("<<n+yA~M", true).equals("\"nʹya\u0304m\u0310"));
        
    }
}
