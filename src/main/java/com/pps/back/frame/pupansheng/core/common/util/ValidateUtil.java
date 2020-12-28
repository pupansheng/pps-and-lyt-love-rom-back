package com.pps.back.frame.pupansheng.core.common.util;

/**
 * @author
 * @discription;
 * @time 2020/5/14 14:35
 */
import java.util.Collection;
import java.util.Map;

/*
  用户主动校检 不抛错误 返回布尔值
 */
public class ValidateUtil {
    public static final String ContiguousUSStateCodes = "AL|AZ|AR|CA|CO|CT|DE|DC|FL|GA|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY";

    public ValidateUtil() {
    }

    public static boolean areEqual(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        } else {
            return obj.equals(obj2);
        }
    }

    public static boolean areEqualIgnoreCase(String obj, String obj2) {
        if (obj == null) {
            return obj2 == null;
        } else {
            return obj.equalsIgnoreCase(obj2);
        }
    }

    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        } else {
            if (value instanceof String) {
                if (((String)value).length() == 0) {
                    return true;
                }
            } else if (value instanceof Collection) {
                if (((Collection)value).size() == 0) {
                    return true;
                }
            } else if (value instanceof Map) {
                if (((Map)value).size() == 0) {
                    return true;
                }
            } else if (value instanceof String[] && ((String[])((String[])value)).length == 0) {
                return true;
            }

            return false;
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isEmpty(Collection c) {
        return c == null || c.size() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public static boolean isNotEmpty(Collection c) {
        return c != null && c.size() > 0;
    }

    public static boolean isString(Object obj) {
        return obj != null && obj instanceof String;
    }
}
