package com.qsx.parking.delay;

import com.qsx.parking.common.enums.PaymentStatusEnum;
import com.qsx.parking.common.enums.VehicleStatusEnum;
import com.qsx.parking.dao.entity.PaymentDO;
import com.qsx.parking.dao.entity.VehicleDO;
import com.qsx.parking.dao.mapper.PaymentMapper;
import com.qsx.parking.dao.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

/**
 * 延迟取消支付管理器
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
@Component
@RequiredArgsConstructor
@Slf4j(topic = "delay-cancel-payment-manager")
public class DelayCancelPaymentManager implements CommandLineRunner {

    private final DelayQueue<DelayCancelPaymentTask> delayQueue = new DelayQueue<>();

    private final PaymentMapper paymentMapper;
    private final VehicleMapper vehicleMapper;

    @Lazy
    @Autowired
    private DelayCancelPaymentManager self;

    public void addTask(DelayCancelPaymentTask task) {
        delayQueue.put(task);
    }

    @Override
    public void run(String... args) {
        Executors.newSingleThreadExecutor().execute(() -> {
            while (true) {
                try {
                    self.executeTask(delayQueue.take());
                } catch (InterruptedException ignore) {
                }
            }
        });
    }

    @Transactional
    protected void executeTask(DelayCancelPaymentTask task) {
        log.info("[延迟取消支付] 开始执行任务，支付id：{}", task.getPid());

        PaymentDO paymentDO = paymentMapper.selectById(task.getPid());
        VehicleDO vehicleDO = vehicleMapper.selectById(paymentDO.getVid());

        if (vehicleDO.getStatus() == VehicleStatusEnum.LEFT.getType()) {
            return;
        }

        if (paymentDO.getStatus() == PaymentStatusEnum.PENDING.getType()) {
            PaymentDO updatePayment = PaymentDO.builder()
                    .id(paymentDO.getId())
                    .status(PaymentStatusEnum.CANCELED.getType())
                    .build();
            paymentMapper.updateById(updatePayment);
        } else if (paymentDO.getStatus() == PaymentStatusEnum.PROCESSING.getType() ||
                   paymentDO.getStatus() == PaymentStatusEnum.COMPLETED.getType()) {
            PaymentDO updatePayment = PaymentDO.builder()
                    .id(paymentDO.getId())
                    .status(PaymentStatusEnum.PENDING_REFUND.getType())
                    .build();
            paymentMapper.updateById(updatePayment);
        }

        VehicleDO updateVehicle = VehicleDO.builder()
                .id(vehicleDO.getId())
                .status(VehicleStatusEnum.ENTERED.getType())
                .build();
        vehicleMapper.updateById(updateVehicle);
    }
}
