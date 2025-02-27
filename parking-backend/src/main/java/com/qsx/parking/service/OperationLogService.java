package com.qsx.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsx.parking.dao.entity.OperationLogDO;
import com.qsx.parking.dto.req.LogRecordPageQueryReqDTO;
import com.qsx.parking.dto.resp.LogRecordQueryRespDTO;
import com.qsx.parking.framework.page.PageQueryRespDTO;

/**
 * 操作日志业务逻辑层
 */
public interface OperationLogService extends IService<OperationLogDO> {

    /**
     * 分页查询操作日志记录
     * @param requestParam 请求参数
     * @return 操作日志记录分页查询结果
     */
    PageQueryRespDTO<LogRecordQueryRespDTO> pageQueryOperationLogRecord(LogRecordPageQueryReqDTO requestParam);
}
