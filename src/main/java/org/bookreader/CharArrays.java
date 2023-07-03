package org.bookreader;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharArrays {

    public static final int HEIGHT = 8;
    public static final int WIDTH = 5;
    private static final String SORTED_CHARACTERS = "etoansihrldumyg.wcI,fkpbv?jHWTMAS-'xNqzYOBRDCJPELG:2F0U513!4V;*86QK7)(%~`@#$^&9_+=[{]}\\|ZX<>/";

    // vars for typing shortcuts
    private static final boolean f = false;
    private static final boolean t = true;
    private static boolean[] fs() { return new boolean[]{f,f,f,f,f};}
    private static boolean[] blank() {return fs();}
    private static boolean[] ts() { return new boolean[]{t,t,t,t,t};}
    private static boolean[] line() {return ts();}
    private static boolean[] nb(boolean a, boolean b, boolean c, boolean d, boolean e) { return new boolean[]{a,b,c,d,e};}
    private static boolean[] nb(boolean a, boolean b, boolean c, boolean d) { return new boolean[]{a,b,c,d,f};}
    private static boolean[] nb(boolean a, boolean b, boolean c) { return new boolean[]{a,b,c,f,f};}
    private static boolean[] nb(boolean a, boolean b) { return new boolean[]{a,b,f,f,f};}
    private static boolean[] nb(boolean a) { return new boolean[]{a,f,f,f,f};}
    private static boolean[] nb()          { return new boolean[]{t,f,f,f,f};}
    private static boolean[] nb(int a) {
        switch(a){
            case(2):
                return nb(t,t);
            case(3):
                return nb(t,t,t);
            case(4):
                return nb(t,t,t,t);
            default:
                return nb();
        }
    }
    private static boolean[] walls() { return new boolean[]{t,f,f,f,t};}
    private static boolean[] iwall() { return new boolean[]{f,t,t,t,f};}

    public static final boolean[][] e = {fs(),fs(),{f,t,t,t,f},{t,f,f,f,t},ts(),{t,f,f,f,f},{f,t,t,t,t},fs()};
    public static final boolean[][] letter_t = {nb(f,t,f,f,f),nb(f,t,f,f,f),nb(t,t,t,f,f),nb(f,t,f,f,f),nb(f,t,f,f,f),nb(f,t,f,f,f),nb(f,f,t,f,f),fs()};
    public static final boolean[][] o = {fs(),fs(),nb(f,t,t,t,f),nb(t,f,f,f,t),nb(t,f,f,f,t),nb(t,f,f,f,t),nb(f,t,t,t,f),fs()};
    public static final boolean[][] a = {fs(),fs(),nb(f,t,t,t,f),nb(f,f,f,f,t),nb(f,t,t,t,t),nb(t,f,f,f,t),nb(f,t,t,t,t),fs()};
    public static final boolean[][] n = {fs(),fs(),nb(4),walls(),walls(),walls(),walls(),fs()};
    public static final boolean[][] s = {blank(),blank(),nb(f,t,t,t,t),nb(t),nb(f,t,t,t,f),nb(f,f,f,f,t),nb(t,t,t,t),blank()};
    public static final boolean[][] i = {nb(t),blank(),nb(),nb(t),nb(t),nb(t),nb(t),blank()};
    public static final boolean[][] h = {nb(t),nb(t),nb(t,f,t,t),nb(t,t,f,f,t),walls(),walls(),walls(),blank()};
    public static final boolean[][] r = {blank(),blank(),nb(t,f,t,t,f),nb(t,t,f,f,t),nb(t),nb(t),nb(t),blank()};
    public static final boolean[][] l = {nb(),nb(),nb(),nb(),nb(),nb(),nb(f,t),blank()};
    public static final boolean[][] d = {nb(f,f,f,f,t),nb(f,f,f,f,t),nb(f,t,t,f,t),nb(t,f,f,t,t),walls(),walls(),nb(f,t,t,t,t),blank()};
    public static final boolean[][] u = {blank(),blank(),walls(),walls(),walls(),walls(),nb(f,t,t,t,t),blank()};
    public static final boolean[][] m = {blank(), blank(), nb(t,t,f,t,f),nb(t,f,t,f,t),nb(t,f,t,f,t),walls(),walls(),blank()};
    public static final boolean[][] y = {blank(),blank(),walls(),walls(),walls(),nb(f,t,t,t,t),nb(f,f,f,f,t),nb(t,t,t,t)};
    public static final boolean[][] g = {blank(),fs(),nb(f,t,t,t,t),walls(),walls(),nb(f,t,t,t,t),nb(f,f,f,f,t),nb(t,t,t,t)};
    public static final boolean[][] period = {blank(),blank(),blank(),blank(),blank(),blank(),nb(),blank()};
    public static final boolean[][] w = {fs(),fs(),walls(),walls(),nb(t,f,t,f,t),nb(t,f,t,f,t),nb(f,t,t,t,t),blank()};
    public static final boolean[][] c = {fs(),fs(),iwall(),walls(),nb(),walls(),iwall(),fs()};
    public static final boolean[][] I = {nb(t,t,t),nb(f,t),nb(f,t),nb(f,t),nb(f,t),nb(f,t),nb(t,t,t),fs()};
    public static final boolean[][] comma = {fs(),fs(),fs(),fs(),fs(),fs(),nb(),nb()};
    public static final boolean[][] letter_f = {nb(f,f,t,t),nb(f,t),nb(t,t,t,t),nb(f,t),nb(f,t),nb(f,t),nb(f,t),fs()};
    public static final boolean[][] k = {nb(),nb(),nb(t,f,f,t),nb(t,f,t),nb(t,t),nb(t,f,t),nb(t,f,f,t),fs()};
    public static final boolean[][] p = {fs(),fs(),nb(t,f,t,t),nb(t,t,f,f,t),walls(),nb(t,t,t,t),nb(),nb()};
    public static final boolean[][] b = {nb(),nb(),nb(t,f,t,t),nb(t,t,f,f,t),walls(),walls(),nb(4),blank()};
    public static final boolean[][] v = {fs(),fs(),walls(),walls(),walls(),nb(f,t,f,t,f),nb(f,f,t),fs()};
    public static final boolean[][] question = {iwall(),walls(),nb(f,f,f,f,t),nb(f,f,f,t),nb(f,f,t),fs(),nb(f,f,t),fs()};
    public static final boolean[][] j = {nb(f,f,f,f,t),fs(),nb(f,f,f,f,t),nb(f,f,f,f,t),nb(f,f,f,f,t),walls(),walls(),iwall()};
    public static final boolean[][] H = {walls(),walls(),ts(),walls(),walls(),walls(),walls(),fs()};
    public static final boolean[][] W = {walls(),walls(),walls(),walls(),nb(t,f,t,f,t),nb(t,t,f,t,t),walls(),fs()};
    public static final boolean[][] T = {ts(),nb(f,f,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),fs()};
    public static final boolean[][] M = {walls(),{t,t,f,t,t},{t,f,t,f,t},walls(),walls(),walls(),walls(),fs()};
    public static final boolean[][] A = {iwall(),walls(),ts(),walls(),walls(),walls(),walls(),fs()};
    public static final boolean[][] S = {{f,t,t,t,t},nb(),{f,t,t,t,f},{f,f,f,f,t},{f,f,f,f,t},walls(),iwall(),fs()};
    public static final boolean[][] dash={fs(),fs(),fs(),ts(),fs(),fs(),fs(),fs()};
    public static final boolean[][] apos={nb(),nb(),fs(),fs(),fs(),fs(),fs(),fs()};
    public static final boolean[][] x = {fs(),fs(),walls(),{f,t,f,t,f},nb(f,f,t),{f,t,f,t,f},walls(),blank()};
    public static final boolean[][] N = {walls(),{t,t,f,f,t},{t,f,t,f,t},{t,f,f,t,t},walls(),walls(),walls(),fs()};
    public static final boolean[][] q = {fs(),fs(),{f,t,t,f,t},{t,f,f,t,t},walls(),{f,t,t,t,t},{f,f,f,f,t},{f,f,f,f,t}};
    public static final boolean[][] z = {fs(),fs(),ts(),{f,f,f,t,f},{f,f,t,f,f},nb(f,t),line(),blank()};
    public static final boolean[][] Y = {walls(),nb(f,t,f,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),blank()};
    public static final boolean[][] O = {iwall(),walls(),walls(),walls(),walls(),walls(),iwall(),blank()};
    public static final boolean[][] B = {nb(4),walls(),nb(4),walls(),walls(),walls(),nb(4),blank()};
    public static final boolean[][] R = {nb(4),walls(),nb(4),walls(),walls(),walls(),walls(),blank()};
    public static final boolean[][] D = {nb(4),walls(),walls(),walls(),walls(),walls(),nb(4),blank()};
    public static final boolean[][] C = {iwall(),walls(),nb(),nb(),nb(),walls(),iwall(),blank()};
    public static final boolean[][] J = {{f,f,f,f,t},{f,f,f,f,t},{f,f,f,f,t},{f,f,f,f,t},{f,f,f,f,t},walls(),iwall(),fs()};
    public static final boolean[][] P = {nb(4),walls(),nb(4),nb(),nb(),nb(),nb(),fs()};
    public static final boolean[][] E = {line(),nb(),nb(3),nb(),nb(),nb(),line(),fs()};
    public static final boolean[][] L = {nb(),nb(),nb(),nb(),nb(),nb(),line(),fs()};
    public static final boolean[][] G = {{f,t,t,t,t},nb(),{t,f,f,t,t},walls(),walls(),walls(),iwall(),blank()};
    public static final boolean[][] colon = {fs(),fs(),nb(),fs(),fs(),fs(),nb(),fs()};
    public static final boolean[][] two = {iwall(),walls(),{f,f,f,f,t},{f,f,t,t,f},nb(f,t),walls(),ts(),fs()};
    public static final boolean[][] F = {line(),nb(),nb(3),nb(),nb(),nb(),nb(),fs()};
    public static final boolean[][] zero = {iwall(),walls(),{t,f,f,t,t},{t,f,t,f,t},{t,t,f,f,t},walls(),iwall(),fs()};
    public static final boolean[][] U = {walls(),walls(),walls(),walls(),walls(),walls(),iwall(),fs()};
    public static final boolean[][] five = {ts(),nb(),nb(4),{f,f,f,f,t},{f,f,f,f,t},walls(),iwall(),fs()};
    public static final boolean[][] one = {nb(f,f,t),nb(f,t,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),line(),fs()};
    public static final boolean[][] three = {iwall(),walls(),{f,f,f,f,t},{f,f,t,t,f},{f,f,f,f,t},walls(),iwall(),blank()};
    public static final boolean[][] exclam = {nb(),nb(),nb(),nb(),nb(),fs(),nb(),fs()};
    public static final boolean[][] four = {{f,f,f,t,t},{f,f,t,f,t},{f,t,f,f,t},walls(),line(),{f,f,f,f,t},{f,f,f,f,t},fs()};
    public static final boolean[][] V = {walls(),walls(),walls(),walls(),{f,t,f,t,f},{f,t,f,t,f},nb(f,f,t),blank()};
    public static final boolean[][] semic = {fs(),fs(),nb(),fs(),fs(),fs(),nb(),nb()};
    public static final boolean[][] star = {nb(t,f,t),nb(f,t),nb(t,f,t),fs(),fs(),fs(),fs(),fs()};
    public static final boolean[][] eight = {iwall(),walls(),walls(),iwall(),walls(),walls(),iwall(),fs()};
    public static final boolean[][] six = {nb(f,f,t,t),nb(f,t),nb(),nb(4),walls(),walls(),iwall(),fs()};
    public static final boolean[][] Q = {iwall(),walls(),walls(),walls(),walls(),nb(t,f,f,t),nb(f,t,t,f,t),fs()};
    public static final boolean[][] K = {walls(),nb(t,f,f,t),nb(3),nb(t,f,f,t),walls(),walls(),walls(),fs()};
    public static final boolean[][] seven = {line(),walls(),{f,f,f,f,t},{f,f,f,t,f},nb(f,f,t),nb(f,f,t),nb(f,f,t),fs()};
    public static final boolean[][] rightP = {nb(),nb(f,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),nb(f,t),nb(),fs()};
    public static final boolean[][] leftP = {nb(f,f,t),nb(f,t),nb(),nb(),nb(),nb(f,t),nb(f,f,t),fs()};
    public static final boolean[][] percent = {walls(), {t,f,f,t,f},{f,f,f,t,f},nb(f,f,t),nb(f,t),{f,t,f,f,t},walls(),fs()};
    // special case, column 6 is ignored
    public static final boolean[][] curl = {{f,t,t,f,f},{t,f,f,t,t},fs(),fs(),fs(),fs(),fs(),fs()};
    public static final boolean[][] grave = {nb(),nb(f,t),fs(),fs(),fs(),fs(),fs(),fs(),fs()};
    // special case, @ has a width of 6, so we ignore the last column
    public static final boolean[][] at = {fs(),{f,t,t,t,t},nb(),nb(t,f,t,t),nb(t,f,t),nb(t,f,t,t,t),nb(),{f,t,t,t,t}};
    public static final boolean[][] pound = {{f,t,f,t,f},{f,t,f,t,f},line(),{f,t,f,t,f},ts(),{f,t,f,t,f},{f,t,f,t,f},blank()};
    public static final boolean[][] dollar = {nb(f,f,t),{f,t,t,t,t},nb(),nb(f,t,t,t),{f,f,f,f,t},nb(4),nb(f,f,t),blank()};
    public static final boolean[][] up = {nb(f,f,t),nb(f,t,f,t),walls(),fs(),fs(),fs(),fs(),fs(),fs()};
    public static final boolean[][] ampersand = {{f,f,t,f,f},{f,t,f,t,f},nb(f,f,t),nb(f,t,t,f,t),{t,f,t,t,f},{t,f,f,t,f},{f,t,t,f,t},fs()};
    public static final boolean[][] nine = {iwall(),walls(),walls(),{f,t,t,t,t},{f,f,f,f,t},{f,f,f,t,f},{f,t,t,f,f},fs()};
    public static final boolean[][] under = {fs(),fs(),fs(),fs(),fs(),fs(),fs(),line()};
    public static final boolean[][] plus = {fs(),nb(f,f,t),nb(f,f,t),line(),nb(f,f,t),nb(f,f,t),fs(),fs()};
    public static final boolean[][] equals = {fs(), fs(), line(),fs(),fs(),line(),fs(),fs()};
    public static final boolean[][] leftB = {nb(3),nb(),nb(),nb(),nb(),nb(),nb(3),fs()};
    public static final boolean[][] leftC = {nb(f,f,t),nb(f,t),nb(f,t),nb(),nb(f,t),nb(f,t),nb(f,f,t),fs()};
    public static final boolean[][] rightB= {nb(3),nb(f,f,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),nb(f,f,t),nb(3),fs()};
    public static final boolean[][] rightC= {nb(),nb(f,t),nb(f,t),nb(f,f,t),nb(f,t),nb(f,t),nb(),fs()};
    public static final boolean[][] backslash={nb(),nb(f,t),nb(f,t),nb(f,f,t),nb(f,f,f,t),{f,f,f,t,f},{f,f,f,f,t},fs()};
    public static final boolean[][] line = {nb(),nb(),nb(),nb(),nb(),nb(),nb(),fs()};
    public static final boolean[][] Z = {line(),{f,f,f,f,t},{f,f,f,t,f},nb(f,f,t),nb(f,t),nb(),line(),fs()};
    public static final boolean[][] X = {walls(),{f,t,f,t,f},nb(f,f,t),{f,t,f,t,f},walls(),walls(),walls(),blank()};
    public static final boolean[][] left={{f,f,f,t,f},nb(f,f,t),nb(f,t),nb(),nb(f,t),nb(f,f,t),{f,f,f,t,f},fs()};
    public static final boolean[][] right = {nb(),nb(f,t),nb(f,f,t),nb(f,f,f,t),nb(f,f,t),nb(f,t),nb(),fs()};
    public static final boolean[][] fSlash = {{f,f,f,f,t},{f,f,f,t,f},{f,f,f,t,f},nb(f,f,t),nb(f,t),nb(f,t),nb(),fs()};

    // Condense them all into one array
    private static final boolean[][][] charBools = {e,letter_t,o,a,n,s,i,h,r,l,d,u,m,y,g,period,w,c,I,comma,letter_f,k,
            p,b,v,question,j,H,W,T,M,A,S,dash,apos,x,N,q,z,Y,O,B,R,D,C,J,P,E,L,G,colon,two,F,zero,U,five,one,three,
            exclam,four,V,semic,star,eight,six,Q,K,seven,rightP,leftP,percent,curl,grave,at,pound,dollar,up,ampersand,
            nine,under,plus,equals,leftB,leftC,rightB,rightC,backslash,line,Z,X,left,right,fSlash};
    private static final char[] chars = {'e','t','o','a','n','s','i','h','r','l','d','u','m','y','g','.','w','c','I',',',
            'f','k','p','b','v','?','j','H','W','T','M','A','S','-','\'','x','N','q','z','Y','O','B','R','D','C','J','P','E',
            'L','G',':','2','F','0','U','5','1','3','!','4','V',';','*','8','6','Q','K','7',')','(','%','~','`','@',
            '#','$','^','&','9','_','+','=','[','{',']','}','\\','|','Z','X','<','>','/',};
    private static final Map<Character,Integer> charIndexMap = getCharIndexMap();

    public static boolean[][][] getAll() {
        return charBools;
    }
    public static char[] getAllChars() {
        return SORTED_CHARACTERS.toCharArray();
    }
    public static Map<Character,Integer> getCharIndexMap() {
        if (charIndexMap == null) {
            Map<Character, Integer> cim = new HashMap<>();
            for (int i = 0; i < chars.length; i++) {
                cim.put(chars[i], i);
            }
            return cim;
        } else return charIndexMap;
    }
    public static boolean[][] getArray(char c) {
        return charBools[charIndexMap.get(c)];
    }

    public static String toString(char c) {
        return toString(getArray(c));
    }
    public static String toString(boolean[][] c) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if(c[i][j]) {
                    stringBuilder.append("[]");
                } else {
                    stringBuilder.append("  ");
                }
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public static boolean compare(boolean[][] array, boolean[][] otherArray) {
        for (int r = 0; r < HEIGHT; r++) {
            for (int c = 0; c < WIDTH; c++) {
                if (array[r][c] != otherArray[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * This comparison skips every other row and column, but still checks
     * (3, 3) and (3, 2). It appears to be correct for the 93 acceptable characters.
     * This makes it 1.6 times faster than regular compare().
     * It is still 9 times slower than a knownDigit compare.
     * @param array
     * @param otherArray
     * @return
     */
    public static boolean riskyCompare(boolean[][] array, boolean[][] otherArray) {
        if (array[3][3] != otherArray[3][3] || array[3][2] != otherArray[3][2]) {
            return false;
        }
        for (int r = 0; r < HEIGHT; r+=2) {
            for (int c = 0; c < WIDTH; c+=2) {
                if (array[r][c] != otherArray[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean[][] emptyArray() {
        return new boolean[][]{fs(),fs(),fs(),fs(),fs(),fs(),fs(),fs()};
    }

}
