package com.qsx.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsx.parking.dao.entity.PaymentDO;
import com.qsx.parking.dto.req.PaymentPageQueryReqDTO;
import com.qsx.parking.dto.resp.PaymentInfoRespDTO;
import com.qsx.parking.framework.page.PageQueryRespDTO;

/**
 * 支付服务逻辑层
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-26
 */
public interface PaymentService extends IService<PaymentDO> {

    /**
     * 创建线下支付订单
     * @param licensePlate 车牌号
     * @return 操作结果，包含停车费用信息
     */
    PaymentInfoRespDTO createOfflinePayment(String licensePlate);

    /**
     * 取消支付订单 | 将车辆状态恢复为已入场
     * @param vid 车辆 ID
     * @param pid 支付 ID
     */
    void cancelPayment(Long vid, Long pid);

    /**
     * 处理线下支付 | 更新车辆状态为已完成，记录支付时间和支付方式
     * @param vid 车辆 ID
     * @param pid 支付 ID
     */
    void payOffline(Long vid, Long pid);

    /**
     * 获取分页支付记录
     * @param requestParam 请求参数
     * @return 支付记录列表
     */
    PageQueryRespDTO<Object> pageQueryPaymentRecord(PaymentPageQueryReqDTO requestParam);
}
