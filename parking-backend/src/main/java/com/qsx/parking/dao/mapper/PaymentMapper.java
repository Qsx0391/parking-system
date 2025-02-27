package com.qsx.parking.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qsx.parking.dao.entity.PaymentDO;
import com.qsx.parking.dto.req.PaymentPageQueryReqDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface PaymentMapper extends BaseMapper<PaymentDO> {
}
