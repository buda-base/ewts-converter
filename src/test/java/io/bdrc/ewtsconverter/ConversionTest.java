package io.bdrc.ewtsconverter;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ConversionTest {

    public static final EwtsConverter converter = new EwtsConverter();

    public String toUnicode(String s, List<String> conversionWarnings) {
        String convertedValue = converter.toUnicode(s, conversionWarnings, true);
        // System.out.println("converting \""+s+"\" into "+convertedValue);
        if (conversionWarnings.size() > 0) {
            System.out.println("with warnings: " + String.join(", ", conversionWarnings));
        }
        return convertedValue;
    }

    @Test
    public void textEwts() {
        List<String> conversionWarnings = new ArrayList<String>();
        String res = toUnicode("pa'ng", conversionWarnings);
        assertTrue(res.equals("པའང"));
        assertTrue(conversionWarnings.size() == 0);
        conversionWarnings = new ArrayList<String>();
        res = toUnicode("be’u'i'o", conversionWarnings);
        assertTrue(res.equals("བེའུའིའོ"));
        assertTrue(conversionWarnings.size() == 0);
        conversionWarnings = new ArrayList<String>();
        res = toUnicode("pa'm", conversionWarnings);
        assertTrue(res.equals("པའམ"));
        assertTrue(conversionWarnings.size() == 0);
        assertTrue(converter.toUnicode("ga&").equals("ག྅"));
        assertTrue(converter.toWylie("ག྅").equals("ga&"));
        assertTrue(converter.toWylie("སཱོ").equals("sO"));
        assertTrue(converter.toUnicode("sO").equals("སཱོ"));
        assertTrue(converter.toUnicode("mNyon mNges gTso rDza padma").equals("མཉོན་མངེས་གཙོ་རྫ་པདྨ"));
    }

    @Test
    public void textDtsAlalcToEwts() {
        EwtsConverter converterAlalc = new EwtsConverter(false, false, false, false, EwtsConverter.Mode.ALALC);
        EwtsConverter converterDwts = new EwtsConverter(false, false, false, false, EwtsConverter.Mode.DTS);
        assertTrue(converterDwts.toUnicode("Ḥdul-ba rnam-par-ḥbyed-pa").equals("འདུལ་བ་རྣམ་པར་འབྱེད་པ"));
        assertTrue(converterAlalc.toUnicode("Ri-gi-ā-ra").equals("རི་གི་ཨཱ་ར"));
        assertTrue(converterAlalc.toUnicode("Ri-gi-i-ra").equals("རི་གི་ཨི་ར"));
        assertTrue(converterAlalc.toUnicode("gʹya tʹsa").equals("གཡ་ཏྶ"));
    }

    @Test
    public void textConvertSloppy() {
        assertTrue(EwtsConverter.normalizeSloppyWylie("Mi la lHan Mi kaHthog 'uM Ma m ").equals("mi la lhan mi kaHthog 'uM ma ma "));
    }
   
    @Test
    public void textConvertLenient() {
        assertTrue(EwtsConverter.normalizeForLenientSearch("Mi la lHan Mi kaHthog kaH thog 'uM Ma m ").equals("mi la lhan mi kaHthog kaHthog 'uM ma m "));
        assertTrue(EwtsConverter.normalizeForLenientSearch("Rje sTong rTen Rnying rNying rJe").equals("rje stong rten rnying rnying rje"));
        assertTrue(EwtsConverter.normalizeForLenientSearch("Ḥdul-bā rnam-par-ḥbyed-pa\u0304").equals("'dul ba rnam par 'byed pa"));
        assertTrue(EwtsConverter.normalizeForLenientSearch("bar-la br-i br-I").equals("bar la bri bri"));
    }
    
    @Test
    public void textEwtsToDtsAlalc() {
        assertTrue(TransConverter.ewtsToAlalc("Ri gi A ra sha", false).equals("ri gi ā ra sha"));
        assertTrue(TransConverter.ewtsToAlalc("<<n+yA~M", true).equals("\"nʹyām̐"));
        assertTrue(TransConverter.ewtsToAlalc("ba_cang /", true).equals("ba cang"));
        assertTrue(TransConverter.ewtsToAlalc("kl-i", true).equals("kl̥̄"));
        assertTrue(TransConverter.ewtsToAlalc("tshi shi", true).equals("tshi shi"));
        assertTrue(TransConverter.ewtsToAlalc("g.yag", true).equals("gʹyag"));
        assertTrue(TransConverter.ewtsToAlalc("ga&", true).equals("ga`"));
        assertTrue(TransConverter.ewtsToAlalc("dwa", true).equals("dwa"));
        assertTrue(TransConverter.ewtsToAlalc("bka' 'gyur", true).equals("bkaʼ ʼgyur"));
        assertTrue(TransConverter.ewtsToAlalc("par gzhi 1., par thengs 2.", true).equals("par gzhi 1 par thengs 2"));
        assertTrue(TransConverter.dtsToEwts("ša śa").equals("sha sha"));
        assertTrue(TransConverter.alalcToEwts("ā").equals("A"));
    }
}
