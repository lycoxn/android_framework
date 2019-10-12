package com.android.example.github.proutil;

import android.content.Context;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 李世民 on 2017/1/17.
 * 字符串操作工具包
 */

public class StringUtils {


    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };


    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }


    /**
     * 返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);

        return df.format(new Date());
    }

    /**
     * 返回当前系统时间
     */
    public static String getDataTime(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);

        return df.format(date);
    }

    public static String getDataTime(long time, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date(time));
    }


    /**
     * 验证手机号
     * 2016年查得共有以下几个手机号段
     * <p/>
     * 移动:139 138 137 136 135 134 1391 1390
     * 147
     * 159 158 157 152 151 150
     * 1705 178
     * 188 187 184 183 182
     * <p/>
     * 联通:132 131 130
     * 145
     * 156 155
     * 176 1709
     * 186 185 1860
     * <p/>
     * 电信:133 1330
     * 153
     * 177 1700
     * 189 181 180 1890
     */
    public static boolean checkPhone(String phone) {
        if (phone == null || "".equals(phone)) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(14[5,7])|(17[0,6,7,8])|(18[0-9]))\\d{8}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }


    public static boolean isChaineseName(String str) {
        String regEx = "[^\u4E00-\u9FA5 ]";
        Pattern p = Pattern.compile(regEx);

        Matcher m = p.matcher(str);
        return "".equals(m.replaceAll("").trim());
    }

    public static String getCurrentTimeStr() {
        Calendar calendar = Calendar.getInstance();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String hour = decimalFormat.format(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = decimalFormat.format(calendar.get(Calendar.MINUTE));
        String second = decimalFormat.format(calendar.get(Calendar.SECOND));
        return hour + minute + second;
    }

    public static String FormatAmount(Double amount) {
        amount = amount * 100;
        DecimalFormat decimalFormat = new DecimalFormat("000000000000");
        return decimalFormat.format(amount);
    }

    public static String[] FormatDouble(double s) {
        String[] da = new String[2];
        da[0] = ((int) s) + "";
        String str = s + "";
        int l = str.length() - str.indexOf(".") - 1;
        if (l > 1) {
            da[1] = str.substring(str.indexOf(".") + 1, str.indexOf(".") + 3);
        } else if (l == 1) {
            da[1] = str.substring(str.indexOf(".") + 1, str.length()) + "0";
        } else {
            da[1] = "00";
        }
        return da;
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public static String getKeys() throws Exception {

        String str = "0123456789ABCDEF";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 48; ++i) {
            int number = random.nextInt(16);
            sb.append(str.charAt(number));
        }
        return ByteUtils.bytesToHexString(parityOfOdd(ByteUtils.convertHexString(sb.toString()), 1));
    }

    public static byte[] parityOfOdd(byte[] bytes, int parity) throws Exception {
        if (bytes == null || bytes.length % 8 != 0) {
            throw new Exception("数据错误!");
        }
        if (!(parity == 0 || parity == 1)) {
            throw new Exception("参数错误!");
        }
        byte[] _bytes = bytes;
        String s; // 字节码转二进制字符串
        char[] cs; // 二进制字符串转字符数组
        int count; // 为1的总个数
        boolean lastIsOne; // 最后一位是否为1
        for (int i = 0; i < _bytes.length; i++) {
            // 初始化参数
            s = Integer.toBinaryString((int) _bytes[i]); // 字节码转二进制字符串
            cs = s.toCharArray();// 二进制字符串转字符数组
            count = 0;// 为1的总个数
            lastIsOne = false;// 最后一位是否为1
            for (int j = 0; j < s.length(); j++) {
                if (cs[j] == '1') {
                    count++;
                }
                if (j == (cs.length - 1)) { // 判断最后一位是否为1
                    if (cs[j] == '1') {
                        lastIsOne = true;
                    } else {
                        lastIsOne = false;
                    }
                }
            }
            // 偶数个1时
            if (count % 2 == parity) {
                // 最后一位为1,变为0
                if (lastIsOne) {
                    _bytes[i] = (byte) (_bytes[i] - 0x01);
                } else {
                    // 最后一位为0,变为1
                    _bytes[i] = (byte) (_bytes[i] + 0x01);
                }
            }
        }
        return _bytes;
    }

    /**
     * 屏蔽姓名
     *
     * @param accountStr
     * @return
     */
    public static String hiddenName(String accountStr) {
        try {
            if (!isEmpty(accountStr)) { // 手机号
                accountStr = accountStr.substring(0, accountStr.length() - 1) + "*";
            } else {
                return "**";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "**";
        }
        return accountStr;
    }

    /**
     * 屏蔽手机号 中间4位
     *
     * @param accountStr
     * @return
     */
    public static String hiddenMobile(String accountStr) {
        try {
            if (ExpresssoinValidateUtil.isMobilePhoneall(accountStr)) { // 手机号
                accountStr = accountStr.substring(0, 3) + "****"
                        + accountStr.substring(accountStr.length() - 4);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "--";
        }
        return accountStr;
    }

    /**
     * 屏蔽身份证号码 保留前3后4
     *
     * @param accountStr
     * @return
     */
    public static String hiddenIdcard(String accountStr) {
        try {
            accountStr = accountStr.substring(0, 3) + "*******"
                    + accountStr.substring(accountStr.length() - 4);
        } catch (Exception e) {
            e.printStackTrace();
            return "--";
        }
        return accountStr;
    }


    /**
     * 屏蔽银行卡号中间数字
     *
     * @param cardNoStr
     * @return
     */
    public static String hiddenCardNo(String cardNoStr) {
        try {
            cardNoStr = cardNoStr.substring(0, 6) + "******"
                    + cardNoStr.substring(cardNoStr.length() - 4);
        } catch (Exception e) {
            return "--";
        }
        return cardNoStr;
    }

    /**
     * 从资源里读取String[]
     *
     * @param context
     * @param count
     * @param s
     * @return
     */
    public static String[] getString(Context context, int count, int... s) {
        String[] strings = new String[count];
        for (int i = 0; i < count; i++) {
            strings[i] = context.getString(s[i]);
        }
        return strings;
    }

    /**
     * 电话号码中间四位替换*号处理
     */
    public static String phoneNumberFormat(String number) {
        return replaceStr(3, 7, number, "****");
    }

    /**
     * 替换指定位置的字符串
     */
    public static String replaceStr(int start, int end, String str, String replace) {
        if (isEmpty(str)) return "";
        StringBuffer buffer = new StringBuffer(str);
        buffer.replace(start, end, replace);
        return buffer.toString();
    }

    /**
     * 为null 返回""
     */
    public static String noNull(Object s) {
        String ss = String.valueOf(s);
        if (ss.equals("null"))
            return "";
        return ss;
    }

    /**
     * 为null 返回"0"
     */
    public static String noNullZero(Object s) {
        String ss = String.valueOf(s);
        if (StringUtils.isEmpty(ss) || ss.equals("0.00"))
            return "0";
        return ss;
    }

    /**
     * 三位一逗号 format
     */
    public static String setThreeCommaFormat(String o) {
        if (isEmpty(o)) return "0";
        if (o.equals("0") || o.equals("0.00")) return "0";
        String s = String.valueOf((o.contains(".") ? new DecimalFormat("#,###.00") : new DecimalFormat("#,###")).format(new BigDecimal(o)));
        return s.startsWith(".") ? new StringBuffer(s).insert(0, "0").toString() : s;
    }

    /**
     * 是否是数字
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 二进制数组转十六进制字符串
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * 判断字符串是否相等
     * 被比较数 不支持null
     *
     * @param s
     */
    public static boolean equal(String a, String... s) {
        if (isEmpty(a)) return false;
        for (String ss : s)
            if (a.equals(ss)) return true;
        return false;
    }
}
