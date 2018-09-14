package io.bdrc.ewtsconverter;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;

/* 
 * This file is used to convert DTS and ALALC transliteration schemes to EWTS.
 * 
 * ALALC is defined by this document:
 *    https://www.loc.gov/catdir/cpso/romanization/tibetan.pdf
 *    
 *  DTS (Diacritics Transliteration Scheme) has never been normalized. Some important internal documents use
 *  it though, and we thought it would be useful to handle it too, especially
 *  since the differences are fairly small (they are indicated in the init() function).
 *  
 *  An important difference that is not handled by this file is that alalc and dts both
 *  stack letters by default even if they do not form standard stacks. For instance
 *  ṅkhya in alalc would be ng+kh+ya in ewts (and would produce no warning).
 */

public class TransConverter {
    public static String baseDts[];
    public static String baseAlalc[];
    public static String replAlalcToEwts[];
    public static String replDtsToEwts[];
    public static String baseEwts[];
    public static String replEwtsToAlalc[];
    
    private static final int DTS = 0;
    private static final int ALALC = 1;
    private static final int BOTH = 2;
    
    final static Map<String,String> replMapAlalcToEwts = new TreeMap<String,String>();
    final static Map<String,String> replMapDtsToEwts = new TreeMap<String,String>();
    final static Map<String,String> replMapEwtsToAlalc = new TreeMap<String,String>();
    
    static {
    	init();
    }
    
    static void addMapping(final String target, final String ewts, final int targetType, final boolean toAlalc) {
        if (targetType == DTS || targetType == BOTH) {
            replMapDtsToEwts.put(target, ewts);
        }
        if (targetType == ALALC || targetType == BOTH) {
            replMapAlalcToEwts.put(target, ewts);
        }
        if (toAlalc) {
            replMapEwtsToAlalc.put(ewts, target);
        }
    }

    public static void init() {
    	final Map<String,String> replMap = new TreeMap<String,String>();
    	// we always handle NFC and NFD, that makes the list a bit cumbersome
    	addMapping("-", " ", BOTH, true);
    	addMapping("ś", "sh", BOTH, true);
    	addMapping("s\\u0301", "sh", BOTH, false);
    	addMapping("ź", "zh", BOTH, true);
    	addMapping("z\\u0301", "zh", BOTH, false);
    	addMapping("ñ", "ny", BOTH, true);
    	addMapping("n\\u0303", "ny", BOTH, false);
    	addMapping("ṅ", "ng", BOTH, true);
    	addMapping("n\\u0307", "ng", BOTH, false);
    	addMapping("ā", "A", BOTH, true);
    	addMapping("a\\u0304", "A", BOTH, false);
    	addMapping("ī", "I", BOTH, true);
    	addMapping("i\\u0304", "I", BOTH, false);
    	addMapping("ū", "U", BOTH, true);
    	addMapping("u\\u0304", "U", BOTH, true);
    	addMapping("ṃ", "M", BOTH, true);
    	addMapping("m\\u0323", "M", BOTH, true);
    	addMapping("ṁ", "~M", BOTH, true);
    	addMapping("m\\u0307", "~M", BOTH, true);
    	addMapping("m\\u0310", "~M", BOTH, true); // alalc
    	addMapping("m\\u0901", "~M", BOTH, true); // alalc
    	addMapping("m\\u0301", "~M`", BOTH, true); // dts
    	addMapping("ṛ", "r-i", BOTH, true); // dts
    	addMapping("r\\u0323", "r-i", BOTH, true);
    	addMapping("r\\u0325", "r-i", BOTH, true); // alalc
    	addMapping("ṝ", "r-I", BOTH, true); // dts
    	addMapping("ṛ\\u0304", "r-I", BOTH, true);
    	addMapping("r\\u0323\\u0304", "r-I", BOTH, true);
    	addMapping("r\\u0304\\u0323", "r-I", BOTH, true);
    	addMapping("r\\u0325\\u0304", "r-I", BOTH, true); // alalc
    	addMapping("r\\u0304\\u0325", "r-I", BOTH, true);
    	addMapping("ḷ", "l-i", BOTH, true); // dts
    	addMapping("l\\u0323", "l-i", BOTH, true);
    	addMapping("l\\u0325", "l-i", BOTH, true); // alalc
    	addMapping("ḹ", "l-i", BOTH, true); // dts
    	addMapping("ḷ\\u0304", "l-i", BOTH, false);
    	addMapping("l\\u0323\\u0304", "l-i", BOTH, false);
    	addMapping("l\\u0304\\u0323", "l-i", BOTH, false);
    	addMapping("l\\u0325\\u0304", "l-i", BOTH, false); // alalc
    	addMapping("l\\u0304\\u0325", "l-i", BOTH, false);
    	addMapping("ṭ", "T", BOTH, true);
    	addMapping("t\\u0323", "T", BOTH, false);
    	addMapping("ḍ", "D", BOTH, true);
    	addMapping("d\\u0323", "D", BOTH, false);
    	addMapping("ṇ", "N", BOTH, true);
    	addMapping("n\\u0323", "N", BOTH, false);
    	addMapping("ṣ", "Sh", BOTH, true);
    	addMapping("s\\u0323", "Sh", BOTH, false);
    	addMapping("`", "&", BOTH, true); // alalc
    	addMapping("gʹy", "g.y", BOTH, false); // \u02B9, alalc example
    	addMapping("ʹ", "+", BOTH, false); // \u02B9, seems to be a general case in alalc description although could sometimes be .
        addMapping("’", "'", BOTH, true); // \u2019, alalc
        addMapping("‘", "'", BOTH, false); // \u2018, just in case
    	addMapping("ʼ", "'", BOTH, false); // \u02BC, alalc
    	addMapping("ʾ", "'", BOTH, false); // \u02BE, indicated in a work document
    	addMapping("v", "w", BOTH, true); // just in case
    	// the only contradiction between DWTS and Ala-lc is:
    	addMapping("ḥ", "H", ALALC, true); // alalc
    	addMapping("h\\u0323", "H", ALALC, false);
    	addMapping("ḥ", "'", DTS, false); // dwts
    	addMapping("h\\u0323", "'", DTS, false);
    	baseDts = replMapDtsToEwts.keySet().toArray(new String[0]);
    	replDtsToEwts = replMapDtsToEwts.values().toArray(new String[0]);
    	baseAlalc = replMapAlalcToEwts.keySet().toArray(new String[0]);
        replAlalcToEwts = replMapDtsToEwts.values().toArray(new String[0]);
        // Alalc doesn't like shad, nor any kind of punctuation
        replMapEwtsToAlalc.put(" /", "");
        replMapEwtsToAlalc.put("/", "");
        replMapEwtsToAlalc.put("<<", "\"");
        replMapEwtsToAlalc.put(">>", "\"");
        replMapEwtsToAlalc.put(".", "ʹ");
        replMapEwtsToAlalc.put("n+y", "nʹy");
        replMapEwtsToAlalc.put("t+s", "tʹs");
        replMapEwtsToAlalc.put("s+h", "sʹh");
        replMapEwtsToAlalc.put("n+g", "nʹg");
        replMapEwtsToAlalc.put("+", "");
        replMapEwtsToAlalc.put(";", "");
        replMapEwtsToAlalc.put("|", "");
        replMapEwtsToAlalc.put("!", "");
        replMapEwtsToAlalc.put(":", "");
        replMapEwtsToAlalc.put("=", "");
        replMapEwtsToAlalc.put("_", " ");
        replMapEwtsToAlalc.put("*", "-");
        baseEwts = replMapEwtsToAlalc.keySet().toArray(new String[0]);
        replEwtsToAlalc = replMapEwtsToAlalc.values().toArray(new String[0]);
    }

    public static String dtsToEwtsTokens(String s) {
    	s = s.toLowerCase();
    	return StringUtils.replaceEach(s, baseDts, replDtsToEwts);
    }

    public static String alalcToEwtsTokens(String s) {
    	s = s.toLowerCase();
    	return StringUtils.replaceEach(s, baseAlalc, replAlalcToEwts);
    }

    public static String EwtsToAlalc(String s) {
        s =  StringUtils.replaceEach(s, baseEwts, replEwtsToAlalc);
        return WordUtils.capitalize(s);
    }
    
}
