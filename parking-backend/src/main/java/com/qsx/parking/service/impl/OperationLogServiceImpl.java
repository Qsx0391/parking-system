package com.qsx.parking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsx.parking.dao.mapper.OperationLogMapper;
import com.qsx.parking.dao.entity.OperationLogDO;
import com.qsx.parking.dto.req.LogRecordPageQueryReqDTO;
import com.qsx.parking.dto.resp.LogRecordQueryRespDTO;
import com.qsx.parking.framework.page.PageQueryRespDTO;
import com.qsx.parking.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLogDO> implements OperationLogService {

    private final OperationLogMapper operationLogMapper;

    private final static List<String> ORDER_BY_FIELDS = List.of("id", "username", "duration", "created_at");

    @Override
    public PageQueryRespDTO<LogRecordQueryRespDTO> pageQueryOperationLogRecord(LogRecordPageQueryReqDTO requestParam) {
        requestParam.checkOrders(ORDER_BY_FIELDS);
        return PageQueryRespDTO.of(operationLogMapper.selectLogRecordPage(requestParam.getPage(), requestParam));
    }
}
