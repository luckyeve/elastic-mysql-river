package com.luckyeve.elastic.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by lixy on 2016/11/12.
 */
public class StringUtil {

    private static final Logger LOG = LoggerFactory.getLogger(StringUtil.class);
    private static StringUtil _instance = new StringUtil();
    public static String Illegal="[\\\\`~!@\\s#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）+|{}【】‘；：”“’。，、？～＠＃＄％＾＆＊（）．，＋＝｜｛｝［］；：＂＂＇＇＼＜＞／｀×•﹏·《》〈〉]";
    public static String IllegalBlank="[\u00a0]";

    public static StringUtil getInstance() {
        return _instance;
    }

    private static final Map<Byte, String> byteHex = new HashMap<Byte, String>();

    public static final Map<String, String> ESCAPE_HTML = new LinkedHashMap<String, String>();

    private static final char[] args = new char[]{'q','w','e','r','t','y','u','i','o','p'
            ,'a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m'
            ,'M','N','B','V','C','X','Z','L','K','J','H','G','F','D','S','A'
            ,'P','O','I','U','Y','T','R','E','W','Q','1','2','3','4','5','6','7','8','9','0'};
    private static final char[] numbers = new char[]{'1','2','3','4','5','6','7','8','9','0'};

    public static final String atreg="@[^\\.^\\,^:^;^!^(^)^\\?^\\s^#^@^。^，^：^；^！^？^（^）^<]+";


    static {
        String stmp = "";
        for (int i = -128; i < 128; i++) {
            byte b = (byte) i;
            stmp = (Integer.toHexString(b & 0XFF));
            if (stmp.length() == 1) {
                stmp = "0"+stmp;
            }
            byteHex.put(b, stmp);
        }

//		ESCAPE_HTML.put("&","&amp;");
        ESCAPE_HTML.put(" ","&nbsp;");
        ESCAPE_HTML.put("<","&lt;");
        ESCAPE_HTML.put(">","&gt;");
//		ESCAPE_HTML.put("\"","&quot;");
    }


    public static String nvl(String str, String def) {
        return (str == null || "".equals(str.trim())) ? def : str;
    }

    public static Object nvl(Object str, Object def) {
        return str == null ? def : str;
    }

    public static String nvl(String str) {
        return nvl(str, "");
    }

    /**
     * 随机验证码字母大小写和数字
     * @param number 几位验证码
     * @return
     */
    public static String getVerificationCode (int number){
        int length = args.length;
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for(int i=0;i<number;i++){
            code.append(args[random.nextInt(length)]);
        }
        return code.toString();
    }

    /**
     * 是否为纯数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 随机数验证码，纯数字
     * @param number 几位验证码
     * @return
     */
    public static String getRandomNumber(int number){
        int length = numbers.length;
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for(int i=0;i<number;i++){
            code.append(numbers[random.nextInt(length)]);
        }
        return code.toString();
    }


    public static String getRandomString(int length) {
        char[] r = new char[length];
        Random random = new Random();
        for (int i = 0; i < r.length; i++) {
            r[i] = args[random.nextInt(args.length)];
        }
        return String.valueOf(r);
    }
    public static String md5Encrypt(String str) {
        if(StringUtil.isEmpty(str))
            return "";
        try {
            java.security.MessageDigest alga = java.security.MessageDigest.getInstance("MD5");
            alga.update(str.getBytes());
            byte[] digesta = alga.digest();
            return (byte2hex(digesta));
        } catch (NoSuchAlgorithmException ex) {
            LOG.error(" StringUtil  encrypt  error   :  " + ex.getMessage(), ex);
            return "";
        }
    }

    public String getEncryption(String originString) {
        String result = null;
        if (originString != null) {
            try {
                // 指定加密的方式为MD5
                java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
                // 进行加密运算
                byte bytes[] = md.digest(originString.getBytes());
                for (int i = 0; i < bytes.length; i++) {
                    // 将整数转换成十六进制形式的字符串 这里与0xff进行与运算的原因是保证转换结果为32位
                    String str = Integer.toHexString(bytes[i] & 0xFF);
                    if (str.length() == 1) {
                        str += "F";
                    }
                    result += str;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return result.toLowerCase();
    }


    public static String replaceSpeChar(String name){
        Pattern p=Pattern.compile(Illegal);

        Matcher m=p.matcher(name);

        name = m.replaceAll("");
        String regex = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w\\-]+";
        return name.replaceAll(regex, "");
    }



    public static int getLength(String str) {

        return length(str);
    }
    public static String byte2hex(byte[] bytes) {
        StringBuffer hs = new StringBuffer(bytes.length*2);
        for (byte b : bytes) {
            hs.append(byteHex.get(b));
        }

        return hs.toString().toLowerCase();
    }

    public static boolean isBlank(String str) {
        return str == null || "".equals(str.trim());
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean notEmpty(String str) {
        return str != null && !"".equals(str);
    }
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 过滤html代码,并将空格 和换行换成 br nbsp;
     *
     * @param src
     * @deprecated
     * @return
     */
    public String filterHTMLCode(String src) {
        return this.showPureTextInHtml(src);
    }

    public String showPureTextInHtml(String src) {
        if (src == null)
            return "";
        String r = src.replaceAll("  ", " &nbsp;");// 两个空格替换成一个空格+&nbsp;
        r = r.replaceAll("\n", "<br>");
        return r;
    }

    public static String getMixRandom(int length) {
        int[] array = new int[length];
        char[] chars = new char[length];
        StringBuilder str = new StringBuilder();
        int temp = 0;
        for (int i = 0; i < length; i++) {
            while (true) {
                temp = (int) (Math.random() * 1000);
                if (temp <= 125 && temp >= 65)
                    break;
            }
            array[i] = temp;
            chars[i] = (char) array[i];
            str.append(chars[i]);
        }
        return str.toString();
    }

    /**
     * 关闭StringBuffer 从起点到终点的引号
     *
     * @param src
     * @param beginPos
     * @param endPos
     * @return int 关闭的引号个数
     */
    public int endQuote(StringBuffer src, int beginPos, int endPos) {
        int r = 0;
        if (endPos <= 0 || endPos <= beginPos || endPos > src.length())
            return r;
        String t = src.substring(beginPos, endPos);
        // System.out.println("t:"+t);
        String temp = this.endQuote(t);
        r = temp != null ? (temp.length() - t.length()) : 0;
        if (r > 0) {
            src.replace(beginPos, endPos, temp);
        }
        return r;
    }


    public static String getIntRandom(int length) {
        int[] array = new int[length];
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            array[i] = (int) (Math.random() * 10);
            str.append(array[i]);
        }
        return str.toString();
    }
    /**
     * 关闭单、双引号
     *
     * @param src
     * @return
     */
    public String endQuote(String src) {
        if (src.indexOf('\'') < 0 && src.indexOf('\"') < 0)
            return src;
        char[] t = src.toCharArray();
        StringBuffer r = new StringBuffer(t.length + 10);
        // 关闭
        Stack<String> stack = new Stack<String>();
        for (int i = 0; i < t.length; i++) {
            if (t[i] != '\'' && t[i] != '\"')
                r.append(t[i]);
            else {
                // System.out.println(r);
                if (stack.isEmpty()
                        || !stack.peek().equals(String.valueOf(t[i]))) {// 不相等的情况
                    if (stack.isEmpty())
                        stack.push(String.valueOf(t[i]));
                    else {
                        if (stack.search(String.valueOf(t[i])) != -1) {
                            r.append(stack.pop());// 封闭上一个'\'' 或者'\"'
                            stack.pop();
                        } else {
                            r.append(stack.pop());
                            stack.push(String.valueOf(t[i]));
                        }
                    }
                } else {
                    stack.pop();
                }
                r.append(t[i]);
            }
        }
        while (!stack.isEmpty()) {
            r.append(stack.pop());
        }
        return r.toString();
    }


    /**
     * 取字符串的前一部分字符,会自动加"..".
     * 连续多空行 只保留一个
     *
     * @param src 源字符串
     * @param length 要截断的长度.
     * @return String
     */
    public static String getBeginString(String src, int length) {
        if (src == null)
            return "";
        String result = getString(src, 0, length, null);
        if (result.length() != src.length()) {
            result = getString(src, 0, length - 2, null);
            result = result + "src/main";
        }
        return result.replaceAll("\\s*\\r?\\n(\\s*\\r?\\n)+", "\\r\\n");
    }



    /**
     * 获取纯文本
     * @param content
     * @return
     */
    public static String getText(String content) {
        if (content == null || content.length() == 0) {
            return content;
        }
        //替换图片
        content = Pattern.compile("<\\s*img\\s+([^>]*)\\s*>").matcher(content).replaceAll("[图片]");
        //替换其他html
        content = content.replaceAll("</\\s*a>", " </a>");
        if(!StringUtil.isEmpty(content) && (content.contains("<video") || content.contains("<embed") || content.contains("<object"))){
            //替换视频
            content = Pattern.compile("<\\s*(video|embed|object)\\s+([^>]*)\\s*>").matcher(content).replaceAll("[视频]");;
        }
        String text = content.replaceAll("(</?\\??[\\w\\:]{1,20}\\s*/?\\s*>)|(<\\??[\\w\\:]+\\s*[^\\s>]+\\s*=[^>]+\\s*>)", "");
        text = text.replace("<!", "");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        text = text.replace("&nbsp;", "");

        return text;
    }

    /**
     * 取指定字符串的某截内容
     *
     * @param src 原始字符串
     * @param beginIndex 开始位置以英文字符为标准
     * @param length 要截取的长度,以英文字符为标准,1个中文字符=2个英文字符
     * @param enc 字符编码
     * @return
     */

    public static String getString(String src, int beginIndex, int length, String enc) {
        StringBuffer result = new StringBuffer(length);
        if (src == null || beginIndex >= src.length()) {
            return "";
        }
        try {
            enc = enc == null ? "UTF-8" : enc;
            String s;
            int count = 0;
            for (int i = 0; i < src.length() && count < length; i++) {
                s = String.valueOf(src.charAt(i));
                if (s.getBytes(enc).length > 1) {// 非英文字面统一作为2个单位宽度的字符处理
                    count += 2;
                } else {
                    count++;
                }
                if (count < length) {
                    result.append(s);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
    public static String escapeLikeSql(String var){
        if(!StringUtil.isEmpty(var)){
            String newValue="";
            newValue=var.trim().replaceAll("\\\\","\\\\\\\\");
            newValue=newValue.replaceAll("'","\\\\'");
            newValue=newValue.replaceAll("_","\\\\_");
            newValue=newValue.replaceAll("\"","\\\\\"");
            newValue=newValue.replaceAll("%","\\\\%");
            return newValue;
        }else{
            return var;
        }
    }
    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     * @param value 指定的字符串
     * @return 字符串的长度
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        String emoji = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\u2500-\u257F]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else if(temp.matches(emoji)){
				/* emoji 字符长度为2 */
                valueLength += 2;
            }else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    public static String substring(String str, int len) {
        if (str == null) {
            return str;
        }
        if (str.length() > len) {
            return str.substring(0, len);
        }
        return str;
    }

    public static boolean isDigit(String str) {
        if (str == null || str.trim().length() == 0)
            return false;
        boolean isDigit = true;
        char[] arr = str.trim().toCharArray();
        for (char ch : arr) {
            if (!Character.isDigit(ch)) {
                isDigit = false;
                break;
            }
        }
        return isDigit;
    }


    /**
     * 将字符串列表拼接双井号
     * @param list
     * @return
     */
    public static List<String> packageDoublePound(List<String> list){
        ArrayList<String> result = new ArrayList<String>();
        if(list!=null){
            for (String string:list){
                result.add("#"+string+"#");
            }
        }
        return result;
    }

    /**
     * 切分为整型数组
     * @param str 逗号分隔的字符串
     * @return 整型数组
     */
    public static List<Integer> splitIntegerList(String str) {
        List<Integer> result = new ArrayList<Integer>();
        if (str == null || str.trim().length() == 0) {
            return result;
        }
        String[] arr = str.split(",");
        for (String ar: arr) {
            ar = ar.trim();
            Integer e = Integer.parseInt(ar);
            result.add(e);
        }
        return result;
    }

    /**
     * 切分为整型数组
     * @param str 逗号分隔的字符串
     * @return 整型数组
     */
    public static List<Integer> splitIntegerList(String str, String regex) {
        List<Integer> result = new ArrayList<Integer>();
        if (str == null || str.trim().length() == 0) {
            return result;
        }
        String[] arr = str.split(regex);
        for (String ar: arr) {
            ar = ar.trim();
            Integer e = Integer.parseInt(ar);
            result.add(e);
        }
        return result;
    }

    public static <T> String join(Collection<T> objects) {
        if (objects == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        for (T t : objects) {
            stringBuilder.append(String.valueOf(t)).append(",");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    /**
     * 切分为整型数组
     * @param str 逗号分隔的字符串
     * @return 整型数组
     */
    public static List<Long> splitLongList(String str) {
        List<Long> result = new ArrayList<Long>();
        if (str == null || str.length() == 0) {
            return result;
        }
        String[] arr = str.split(",");
        for (String ar: arr) {
            Long e = Long.parseLong(ar);
            result.add(e);
        }
        return result;
    }

    /**
     * 切分为整型数组
     * @param str 逗号分隔的字符串
     * @return 整型数组
     */
    public static List<Long> splitLongList(String str, String regex) {
        List<Long> result = new ArrayList<Long>();
        if (str == null || str.length() == 0) {
            return result;
        }
        String[] arr = str.split(regex);
        for (String ar: arr) {
            Long e = Long.parseLong(ar);
            result.add(e);
        }
        return result;
    }


    /**
     * 判断是否有特殊字符
     * @param name
     * @return
     */
    public static boolean containSpeChar(String name){
        Pattern p=Pattern.compile(Illegal);

        Matcher m=p.matcher(name);

        boolean rs=m.find();
        if (rs){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断是否包含表情字符
     * @param name
     * @return ture:包含
     */
    public static boolean containEmojiChar(String name){

        //最后一组是制表符如"┌└┐┘─│├┤┬┴┼"
        Pattern p = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\u2500-\u257F]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m=p.matcher(name);
        boolean rs=m.find();
        if (rs){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 判断 字母开头的字符串
     * @param name
     * @return
     */
    public static boolean startWord(String name){
        String regex = "[a-zA-Z]+.*";
        Pattern pattern = Pattern.compile(regex);
        boolean match = pattern.matcher(name).matches();
        return match;
    }

    /**
     * 一种特殊的空白&nbsp;
     * @param name
     * @return
     */
    public static boolean containSpeBlank(String name){
        Pattern p=Pattern.compile(IllegalBlank,Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        Matcher m=p.matcher(name);

        boolean rs=m.find();
        if (rs){
            return true;
        }else{
            return false;
        }
    }

    static public String filterOffUtf8Mb4(String text){
        try{
            byte[] bytes = text.getBytes("UTF-8");
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            int i = 0;
            while (i < bytes.length) {
                short b = bytes[i];
                if (b > 0) {
                    buffer.put(bytes[i++]);
                    continue;
                }
                b += 256;
                if ((b ^ 0xC0) >> 4 == 0) {
                    buffer.put(bytes, i, 2);
                    i += 2;
                }
                else if ((b ^ 0xE0) >> 4 == 0) {
                    buffer.put(bytes, i, 3);
                    i += 3;
                }
                else if ((b ^ 0xF0) >> 4 == 0) {
                    i += 4;
                }
            }
            buffer.flip();
            return new String(buffer.array(), "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return text;
    }
    // 过滤特殊字符
    public static String StringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）+|{}【】‘；：”“’。，、？\\s*|\t|\n" +
                "|\n]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);

        return m.replaceAll("").trim();
    }

    /**
     * 拼接字符串
     * @param strings
     * @return
     */
    public static final String jointString(String...strings){
        StringBuilder stringBuilder = new StringBuilder();
        for (String string:strings){
            stringBuilder.append(string);
        }
        return stringBuilder.toString();
    }

    /**
     * 判断缓存结果是否为空
     * @param value
     * @return
     */
    public static final boolean isCacheEmpty(String value){
        return isEmpty(value)||value.equals("nil");
    }

    /**
     * 从正文中提取@
     * @param content
     * @return
     */
    public static Set<String> extractAt(String content) {
        Set<String> strings = new HashSet<String>();

        if (StringUtil.isEmpty(content))
            return strings;
        content=content+" ";
        String pattern = "@\\S+\\s";
        Matcher matcher = Pattern.compile(pattern).matcher(content);
        while (matcher.find()) {
            strings.add(matcher.group().replace("@","").replace(" ",""));
        }

        return strings;
    }

    /**
     * 解析整形
     */
    public static Integer parseInteger(String str, Integer defaultValue) {
        if (str == null || str.length() == 0) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 解析整形
     */
    public static Integer parseInteger(Object str, Integer defaultValue) {
        try {
            if (str == null || str.toString().trim().length() == 0) {
                return defaultValue;
            }
            return Integer.parseInt(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 解析整形
     */
    public static Integer parseInteger(Object str) {
        try {
            if (str == null || str.toString().trim().length() == 0) {
                return null;
            }
            if (str instanceof Long) {
                return ((Long)str).intValue();
            }
            return Integer.parseInt(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseString(Object str, String defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        return str.toString();
    }

    /**
     * 解析整形
     */
    public static Double parseDouble(String str, Double defaultValue) {
        if (str == null || str.length() == 0 || str.equals("null")) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * 解析整形
     */
    public static Double parseDouble(Object str, Double defaultValue) {
        if (str == null || str.toString().length() == 0) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * long转换
     * @param str
     * @param defaultValue
     * @return
     */
    public static Long parseLong(String str, Long defaultValue) {
        if (str == null || str.trim().length() == 0)
            return defaultValue;
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    /**
     * long转换
     * @param str
     * @param defaultValue
     * @return
     */
    public static Long parseLong(Object str, Long defaultValue) {
        if (str == null || str.toString().trim().length() == 0)
            return defaultValue;
        try {
            return Long.parseLong(str.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

//	/**
//	 * long转换
//	 * @param str
//	 * @return
//	 */
//	public static Long parseLong(Object str) {
//		if (str == null || str.toString().trim().length() == 0)
//			return null;
//		try {
//			return Long.parseLong(str.toString());
//		} catch (NumberFormatException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

    /**
     * 解析整形
     */
    public static List<Integer> parseIntegers(Object[] array) {
        if (array == null || array.length == 0) {
            return new ArrayList<Integer>();
        }
        try {
            List<Integer> ids = new ArrayList<Integer>();
            for (Object key: array) {
                if (key instanceof Number) {
                    ids.add(((Number) key).intValue());
                } else if (key instanceof String){
                    ids.add(Integer.parseInt(key.toString()));
                }
            }
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Integer>();
        }
    }

    /**
     * 解析整形
     */
    public static List<Long> parseLongs(Object[] array) {
        if (array == null || array.length == 0) {
            return new ArrayList<Long>();
        }
        try {
            List<Long> ids = new ArrayList<Long>();
            for (Object key: array) {
                if (key instanceof Number) {
                    ids.add(((Number) key).longValue());
                } else if (key instanceof String){
                    ids.add(Long.parseLong(key.toString()));
                }
            }
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Long>();
        }
    }
    /**
     * 校验字符串是否只包含 中文、数字、字母、中划线和下划线
     *
     **/
    public static boolean nameValid(String name){
        if(StringUtil.isEmpty(name)){
            return false;
        }
        String regex = "^[\\-a-z0-9A-Z_\\u4e00-\\u9fa5]+$";
        if(!name.matches(regex)){
            return false;
        }
        return true;
    }
    public static boolean activityNameValid(String name){
        if(StringUtil.isEmpty(name)){
            return false;
        }
        String regex = "^[!~|,.?:;'\"！，。？：；”“‘’\\-a-z0-9A-Z_\\u4e00-\\u9fa5]+$";
        if(!name.matches(regex)){
            return false;
        }
        return true;
    }
    /**
     * 替换json中的long类型为string
     * @param json
     * @return
     */
    public static String replaceJsonLongValue(String json) {
        if (json == null || !json.startsWith("{")) {
            return json;
        }
        String regex = "\\:\\s*(\\d{15,})\\s*[,\\}]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(json);
        List<String[]> list = new ArrayList<String[]>();
        while(matcher.find()) {
            String target = matcher.group();
            String val = matcher.group(1);
            String replacement = target.replace(val, "\"" + val + "\"");
            list.add(new String[]{target, replacement});
        }
        for (String[] vals: list) {
            json = json.replace(vals[0], vals[1]);
        }
        return json;
    }

    /**
     * 获取子数组
     * @param list
     * @param from
     * @param limit
     * @param <T>
     * @return
     */
    public static <T> List<T> subList(List<T> list, int from, int limit) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return new ArrayList<T>();
        }
        if (from < 0) {
            from = 0;
        }
        if (from >= list.size()) {
            return new ArrayList<T>();
        }
        int to = from + limit;
        if (to > list.size()) {
            to = list.size();
        }
        if (from > to) {
            return list;
        }
        return list.subList(from, to);
    }

    public static String formatNum(Integer num) {
        if (num == null) return null;
        String str = "";
        if (num < 1000) {
            str = num + "";
        } else {
            str  = ((num/100)/10.0)+"k";
        }
        return str;
    }

    public static String formatNum(Long num) {
        if (num == null) return null;
        String str = "";
        if (num < 1000) {
            str = num + "";
        } else {
            str  = ((num/100)/10.0)+"k";
        }
        return str;
    }
    public static String getBriefText(String content) {
        if (content == null || content.length() == 0) {
            return content;
        }
        content = content.replaceAll("</\\s*a>", " </a>");
        String text = content.replaceAll("(</?\\??[\\w\\:]{1,20}\\s*/?\\s*>)|(<\\??[\\w\\:]+\\s*[^\\s>]+\\s*=[^>]+\\s*>)", "");
        text = text.replace("<!", "");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        text = text.replace("\"", "");
        text = text.replace("\n", "");
        text = text.replace("&nbsp;", "");
        return text;
    }


    public static String escapeHtml(String html) {
        if (StringUtil.isEmpty(html))
            return html;

        for (Map.Entry<String, String> entry : ESCAPE_HTML.entrySet()) {
            html = html.replace(entry.getKey(), entry.getValue());
        }

        return html;
    }

    public static <T extends Number> T getNotNull(T val) {
        if (val == null) {
            Number va = 0D;
            return (T)va;
        }
        return val;
    }
}
