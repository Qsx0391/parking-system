package com.qsx.parking.delay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时更新支付任务
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DelayCancelPaymentTask implements Delayed {

    /**
     * 支付记录ID
     */
    private Long pid;

    /**
     * 任务延时时间，单位毫秒
     */
    private long expire;


    @Override
    public long getDelay(@NotNull TimeUnit unit) {
        return unit.convert(expire - System.currentTimeMillis(), unit);
    }

    @Override
    public int compareTo(@NotNull Delayed o) {
        return (int) (getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }
}
