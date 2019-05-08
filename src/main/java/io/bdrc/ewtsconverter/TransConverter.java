package io.bdrc.ewtsconverter;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

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
 *  
 *  The decision to use NFKD for alalc comes from exchanges with Columbia University for
 *  the purpose of exporting to MARC. 
 */

/**
 * Tibetan EWTS from/to DTS or ALA-LC romanization conversion methods
 * 
 * @author Buddhist Digital Resource Center (BDRC)
 * @version 1.4.0
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

    final static Map<String, String> replMapAlalcToEwts = new TreeMap<String, String>();
    final static Map<String, String> replMapDtsToEwts = new TreeMap<String, String>();
    final static Map<String, String> replMapEwtsToAlalc = new TreeMap<String, String>();

    private final static int NFD = 0;
    private final static int NFC = 1;
    private final static int ALWAYS_ALALC = 2;
    private final static int NEVER_ALALC = 3;

    static {
        init();
    }

    static void addMapping(final String target, final String ewts, final int targetType, final int toAlalc) {
        if (targetType == DTS || targetType == BOTH) {
            replMapDtsToEwts.put(target, ewts);
        }
        if (targetType == ALALC || targetType == BOTH) {
            replMapAlalcToEwts.put(target, ewts);
        }
        if (toAlalc == ALWAYS_ALALC || toAlalc == NFD) {
            replMapEwtsToAlalc.put(ewts, target);
        }
    }

    static void init() {
        // we always handle NFC and NFD, that makes the list a bit cumbersome
        // addMapping("-", " ", BOTH, ALWAYS_ALALC);
        addMapping("ś", "sh", BOTH, NEVER_ALALC);
        addMapping("s\u0301", "sh", BOTH, NEVER_ALALC);
        addMapping("ṣ", "Sh", BOTH, NEVER_ALALC);
        addMapping("s\u0323", "Sh", BOTH, NEVER_ALALC);
        addMapping("ź", "zh", BOTH, NEVER_ALALC);
        addMapping("z\u0301", "zh", BOTH, NEVER_ALALC);
        addMapping("ñ", "ny", BOTH, NEVER_ALALC);
        addMapping("n\u0303", "ny", BOTH, NEVER_ALALC);
        addMapping("ṅ", "ng", BOTH, NEVER_ALALC);
        addMapping("n\u0307", "ng", BOTH, NEVER_ALALC);
        addMapping("ā", "A", BOTH, NFC);
        addMapping("a\u0304", "A", BOTH, NFD);
        addMapping("ī", "I", BOTH, NFC);
        addMapping("i\u0304", "I", BOTH, NFD);
        addMapping("ū", "U", BOTH, NFC);
        addMapping("u\u0304", "U", BOTH, NFD);
        addMapping("ṃ", "M", BOTH, NFC);
        addMapping("m\u0323", "M", BOTH, NFD);
        addMapping("ṁ", "~M", BOTH, NEVER_ALALC);
        addMapping("m\u0307", "~M", BOTH, NEVER_ALALC);
        addMapping("m\u0310", "~M", BOTH, ALWAYS_ALALC); // alalc
        addMapping("m\u0901", "~M", BOTH, NEVER_ALALC); // alalc
        addMapping("m\u0301", "~M`", BOTH, NEVER_ALALC); // dts
        addMapping("ṛ", "r-i", BOTH, NFC); // dts
        addMapping("r\u0323", "r-i", BOTH, NEVER_ALALC);
        addMapping("r\u0325", "r-i", BOTH, NFD); // alalc
        addMapping("ṝ", "r-I", BOTH, NFC); // dts
        addMapping("ṛ\u0304", "r-I", BOTH, NEVER_ALALC);
        addMapping("r\u0323\u0304", "r-I", BOTH, NEVER_ALALC);
        addMapping("r\u0304\u0323", "r-I", BOTH, NEVER_ALALC);
        addMapping("r\u0325\u0304", "r-I", BOTH, NFD); // alalc
        addMapping("r\u0304\u0325", "r-I", BOTH, NEVER_ALALC);
        addMapping("ḷ", "l-i", BOTH, NFC); // dts
        addMapping("l\u0323", "l-i", BOTH, NEVER_ALALC);
        addMapping("l\u0325", "l-i", BOTH, NFD); // alalc
        addMapping("ḹ", "l-i", BOTH, NFC); // dts
        addMapping("ḷ\u0304", "l-i", BOTH, NEVER_ALALC);
        addMapping("l\u0323\u0304", "l-i", BOTH, NEVER_ALALC);
        addMapping("l\u0304\u0323", "l-i", BOTH, NEVER_ALALC);
        addMapping("l\u0325\u0304", "l-i", BOTH, NFD); // alalc
        addMapping("l\u0304\u0325", "l-i", BOTH, NEVER_ALALC);
        addMapping("ṭ", "T", BOTH, NFC);
        addMapping("t\u0323", "T", BOTH, NFD);
        addMapping("ḍ", "D", BOTH, NFC);
        addMapping("d\u0323", "D", BOTH, NFD);
        addMapping("ṇ", "N", BOTH, NFC);
        addMapping("n\u0323", "N", BOTH, NFD);
        addMapping("`", "&", BOTH, ALWAYS_ALALC); // alalc
        addMapping("gʹy", "g.y", BOTH, ALWAYS_ALALC); // \u02B9, alalc example
        // \u02B9, seems to be a general case in alalc description although could
        // sometimes be .
        // we don't map the reverse direction (to alalc) as + will always be discarded
        // by the final regex
        addMapping("ʹ", "+", BOTH, NEVER_ALALC);
        addMapping("’", "'", BOTH, NEVER_ALALC); // \u2019, alalc
        addMapping("‘", "'", BOTH, NEVER_ALALC); // \u2018, just in case
        addMapping("ʼ", "'", BOTH, ALWAYS_ALALC); // \u02BC, alalc
        addMapping("ʾ", "'", BOTH, NEVER_ALALC); // \u02BE, indicated in a work document
        addMapping("v", "w", BOTH, ALWAYS_ALALC); // just in case
        // the only contradiction between DWTS and Ala-lc is:
        addMapping("ḥ", "H", ALALC, NFC); // alalc
        addMapping("h\u0323", "H", ALALC, NFD);
        addMapping("ḥ", "'", DTS, NEVER_ALALC); // dts
        addMapping("h\u0323", "'", DTS, NEVER_ALALC);
        baseDts = replMapDtsToEwts.keySet().toArray(new String[0]);
        replDtsToEwts = replMapDtsToEwts.values().toArray(new String[0]);
        baseAlalc = replMapAlalcToEwts.keySet().toArray(new String[0]);
        replAlalcToEwts = replMapDtsToEwts.values().toArray(new String[0]);
        // Alalc doesn't like shad, nor any kind of punctuation
        replMapEwtsToAlalc.put("<<", "\"");
        replMapEwtsToAlalc.put(">>", "\"");
        // replMapEwtsToAlalc.put(".", "ʹ"); // use regexp instead
        replMapEwtsToAlalc.put("_", " ");
        replMapEwtsToAlalc.put("n+y", "nʹy");
        replMapEwtsToAlalc.put("t+s", "tʹs");
        replMapEwtsToAlalc.put("s+h", "sʹh");
        replMapEwtsToAlalc.put("n+g", "nʹg");
        baseEwts = replMapEwtsToAlalc.keySet().toArray(new String[0]);
        replEwtsToAlalc = replMapEwtsToAlalc.values().toArray(new String[0]);
    }

    /**
     * Converts a string from DTS to EWTS
     * 
     * @param dtsString
     *            the DTS encoded string
     * @return EWTS string
     */
    public static String dtsToEwts(String dtsString) {
        dtsString = dtsString.toLowerCase();
        return StringUtils.replaceEach(dtsString, baseDts, replDtsToEwts);
    }

    /**
     * Converts a string from ALA-LC to EWTS
     * 
     * @param alalcStr
     *            the ALA-LC encoded string
     * @return EWTS string
     */
    public static String alalcToEwts(String alalcStr) {
        alalcStr = alalcStr.toLowerCase();
        return StringUtils.replaceEach(alalcStr, baseAlalc, replAlalcToEwts);
    }

    /**
     * Converts a string from EWTS to ALA-LC (NFKD, lower case)
     * 
     * @param ewtsStr
     *            the EWTS encoded string
     * @param sloppy
     *            if common EWTS should be fixed before conversion
     * @return ALA-LC encoded string
     */
    public static String ewtsToAlalc(String ewtsStr, final boolean sloppy) {
        if (sloppy) {
            ewtsStr = EwtsConverter.normalizeSloppyWylie(ewtsStr);
        }
        ewtsStr = StringUtils.replaceEach(ewtsStr, baseEwts, replEwtsToAlalc);
        // we only want to replace dots with ʹ when they're between letters
        ewtsStr = ewtsStr.replaceAll("([a-zA-Z])\\.([a-zA-Z])", "$1ʹ$2");
        ewtsStr = ewtsStr.replaceAll("[^a-zA-Z0-9 \"ʹʼ`\u0325\u0304\u0303\u0323\u0307\u0301\u0310()\\-]", "");
        // in the case of "ng /", previous regexp will remove the "/" but we'll have a
        // spurious "-":
        ewtsStr = StringUtils.strip(ewtsStr, " ");
        // this will also lower case oddities like R and Y
        ewtsStr = ewtsStr.toLowerCase();
        return ewtsStr;
    }

}
