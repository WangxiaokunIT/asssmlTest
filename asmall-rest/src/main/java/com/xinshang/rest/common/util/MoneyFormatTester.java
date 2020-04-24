package com.xinshang.rest.common.util;

import java.math.BigDecimal;

/**
 * 金钱数据类型转换
 * Long类型金额和BigDecimal类型金额互转
 *
 * @author lyk
 */
public class MoneyFormatTester {

    private static final BigDecimal DIV_HUNDRED = new BigDecimal(100);
    private static final BigDecimal MULTIPLY_HUNDRED = new BigDecimal(100);


    public static void main(String[] args) {
        testLong2BigDecimal();
        testBigDecimal2Long();
    }

    /**
     * Long类型金额转化成Bigdecimal类型金额
     */
    public static void testLong2BigDecimal() {
        System.out.println("\nLong类型金额转化成Bigdecimal类型金额");
        // 假设金额格式的类型为Long，单位为分。那么1000则表示10块钱，即10.00
        // 将其转化成BigDecimal，预期结果应该为10.00
        Long money1 = 1000L;
        BigDecimal targetMoney1 = long2BigDecimal(money1);
        System.out.println("Long类型金额1000L，转化成BigDecimal:" + targetMoney1.toString());

        Long money2 = 1234L;
        BigDecimal targetMoney2 = long2BigDecimal(money2);
        System.out.println("Long类型金额1234L，转化成BigDecimal:" + targetMoney2.toString());
    }

    /**
     * 测试Bigcimal类型的金额转化Long类型的金额
     */
    public static void testBigDecimal2Long() {
        System.out.println("\n测试Bigcimal类型的金额转化Long类型的金额");
        // BigDecimal格式的金钱（以分为单位，即保留2位小数点）
        // 假设BigDecimal格式金额为10.00，转化成Long类型，结果应为1000
        BigDecimal money1 = new BigDecimal("10.00");
        Long targetMoney1 = bigDecimal2Long(money1);
        System.out.println("BigDecimal格式金额为10.00，转化成Long类型:" + targetMoney1);
        BigDecimal money2 = new BigDecimal("123456.78");
        Long targetMoney2 = bigDecimal2Long(money2);
        System.out.println("BigDecimal格式金额为123456.78，转化成Long类型:" + targetMoney2);
    }


    /**
     * Long金额转化成BigDecimal类型的金额，保留小数点后两位
     * Long类型金额单位为分
     *
     * @param money
     * @return
     */
    public static BigDecimal long2BigDecimal(Long money) {
        BigDecimal returnBalance = null;
        if (money != null) {
            if (BigDecimal.ZERO.longValue() == money) {
                returnBalance = BigDecimal.ZERO;
            } else {
                BigDecimal temp = new BigDecimal(money);
                returnBalance = temp.divide(DIV_HUNDRED);
            }
        }
        return returnBalance;
    }


    /**
     * BigDecimal类型的金额转化Long类型的金额
     * Long类型金额单位为分
     *
     * @param money
     * @return
     */
    public static Long bigDecimal2Long(BigDecimal money) {
        if (money == null) {
            return 0L;
        }
        return money.multiply(MULTIPLY_HUNDRED).longValue();
    }


}