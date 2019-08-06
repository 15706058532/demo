package service;

/**
 * 微指数时间参数枚举值
 * <br/>
 * Created in 2019-06-25 9:52
 *
 * @author Zhenfeng Li
 */
public enum MicroindexDateEnum {
    /**
     * ONE_HOUR:一小时,ONE_DAY:24小时 ,ONE_MONTH：30天,THREE_MONTH：90天
     */
    ONE_HOUR,
    ONE_DAY,
    ONE_MONTH,
    THREE_MONTH;

    public static String getEnum(String key) {
        if (ONE_HOUR.name().equals(key)) {
            return "1hour";
        } else if (ONE_DAY.name().equals(key)) {
            return "1day";
        } else if (ONE_MONTH.name().equals(key)) {
            return "1month";
        } else if (THREE_MONTH.name().equals(key)) {
            return "3month";
        }
        throw new IllegalArgumentException("No Enum specified for this string");
    }
}
