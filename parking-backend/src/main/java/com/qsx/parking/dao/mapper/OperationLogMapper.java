package com.qsx.parking.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qsx.parking.dao.entity.OperationLogDO;
import com.qsx.parking.dto.req.LogRecordPageQueryReqDTO;
import com.qsx.parking.dto.resp.LogRecordQueryRespDTO;
import org.apache.ibatis.annotations.Param;

public interface OperationLogMapper extends BaseMapper<OperationLogDO> {

    /**
     * 分页查询操作日志记录
     * @param requestParam 查询条件
     * @return 分页的操作日志记录
     */
    IPage<LogRecordQueryRespDTO> selectLogRecordPage(IPage<OperationLogDO> page,
                                                     @Param("requestParam") LogRecordPageQueryReqDTO requestParam);
}
