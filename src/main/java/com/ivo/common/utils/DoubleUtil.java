package com.ivo.common.utils;

import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * Double类型计算工具
 * @author wj
 * @version 1.0
 */
public class DoubleUtil {

    // 保留小数
    private static final int SCALE = 0;
    // 向上模式
    private static final int ROUND = BigDecimal.ROUND_UP;

    /**
     * 数字精度保留，采用的是向上取整
     * @param num 数字
     * @param scale 精度
     * @return Double
     */
    public static Double upPrecision(Double num, int scale) {
        if(num == null) return null;
        BigDecimal bigDecimal = new BigDecimal(num);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * 数字精度保留，采用四舍五入
     * @param num 数字
     * @param scale 精度
     * @return Double
     */
    public static Double rounded(Double num, int scale) {
        if(num == null) return null;
        BigDecimal bigDecimal = new BigDecimal(num);
        return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 求和
     * @param values 求和项
     * @return 和
     */
    public static Double sum(Double... values) {
        Double d = null;
        BigDecimal bigDecimal = new BigDecimal(0);
        for (Double v : values) {
            if (v == null) {
                continue;
            }
            bigDecimal = bigDecimal.add(new BigDecimal(v));
            d = bigDecimal.setScale(SCALE, ROUND).doubleValue();
        }
        return d;
    }

    /**
     * 求差
     * @param number1 被减数
     * @param number2 减数
     * @return 差
     */
    public static Double subtract(Double number1, Double number2) {
        if(number1 == null) {
            return null;
        }
        if(number2 == null) {
            return number1;
        }
        BigDecimal bigDecimal1 = new BigDecimal(number1);
        BigDecimal bigDecimal2 = new BigDecimal(number2);
        BigDecimal d = bigDecimal1.subtract(bigDecimal2);
        return d.setScale(SCALE, ROUND).doubleValue();
    }

    /**
     * 相乘
     * @param values 相乘因数
     * @return 相乘结果
     */
    public static Double multiply(Double... values) {
        Double d = null;
        BigDecimal bigDecimal = new BigDecimal(1);
        for (Double v : values) {
            if (v == null) {
                return 0D;
            } else {
                bigDecimal = bigDecimal.multiply(new BigDecimal(v));
                d = bigDecimal.setScale(SCALE, ROUND).doubleValue();
            }
        }
        return d;
    }

    /**
     * 相除
     * @param numerator 分子
     * @param denominator 分母
     * @return 商
     */
    public static Double divide(Double numerator, Double denominator) {
        if(numerator == null || denominator == null) {
            return null;
        } else {
            BigDecimal bigDecimal1 = new BigDecimal(numerator);
            BigDecimal bigDecimal2 = new BigDecimal(denominator);
            return bigDecimal1.divide(bigDecimal2, 3, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
    }

    /**
     * 将对象转换为Double
     * @param o
     * @return Double
     */
    public static Double converDouble(Object o) {
        if(o == null) return null;
        if(o instanceof Double) return (Double) o;
        if(o instanceof String) {
            if(StringUtils.isEmpty(o)) return null;
            return Double.valueOf((String)o);
        }
        if(o instanceof BigDecimal) return ((BigDecimal) o).doubleValue();
        return (Double) o;
    }

    public static void main(String[] args) {
        System.out.println(upPrecision(0.015*0.025*40*1000, 0));
        System.out.println(multiply(0.015, 0.025, 40D, 1000D));
    }
}

