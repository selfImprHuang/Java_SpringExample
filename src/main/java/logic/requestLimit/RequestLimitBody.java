

package logic.requestLimit;

public class RequestLimitBody {

    /**
     * 点击次数
     */
    private Integer clickCount;

    /**
     * 上一次操作的时间点
     */
    private long lastClick;

    /**
     * 设置延迟点击的最后时间点
     */
    private Long delayClickUtilTIme;

    public Long getDelayClickUtilTIme() {
        return delayClickUtilTIme;
    }

    public void setDelayClickUtilTIme(Long delayClickUtilTIme) {
        this.delayClickUtilTIme = delayClickUtilTIme;
    }

    public Integer getClickCount() {
        return clickCount;
    }

    public void setClickCount(Integer clickCount) {
        this.clickCount = clickCount;
    }

    public long getLastClick() {
        return lastClick;
    }

    public void setLastClick(long lastClick) {
        this.lastClick = lastClick;
    }
}
