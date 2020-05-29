package com.ivo.common.utils;

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
    private static final int ROUND = BigDecimal.ROUND_CEILING;

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
        return bigDecimal1.subtract(bigDecimal2).doubleValue();
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
                return null;
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
            return bigDecimal1.divide(bigDecimal2, SCALE, ROUND).doubleValue();
        }
    }
}

