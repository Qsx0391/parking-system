package com.qsx.parking.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qsx.parking.dao.entity.ParkingLotDO;
import com.qsx.parking.dto.req.ParkingLotConfigUpdateReqDTO;
import org.apache.ibatis.annotations.Param;

public interface ParkingLotMapper extends BaseMapper<ParkingLotDO> {

    /**
     * 尝试预约车位
     * @return 0:失败 1:成功
     */
    int checkAndReserveSpace();

    /**
     * 释放车位
     */
    void releaseSpace();
}
