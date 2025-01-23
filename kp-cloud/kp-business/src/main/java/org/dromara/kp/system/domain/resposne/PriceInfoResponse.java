package org.dromara.kp.system.domain.resposne;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dromara.kp.system.domain.vo.PriceInfoData;
import org.dromara.kp.system.domain.vo.PriceTypeInfoData;

import java.io.Serializable;
import java.util.List;

@Schema(description = "价格信息返回")
@Data
public class PriceInfoResponse implements Serializable {


    private static final long serialVersionUID = 1L;

    @Schema(description = "站点ID")
    Long stationId;

    @Schema(description = "价格编码")
    Long priceCode;

    @Schema(description = "备注")
    String remark;

    @Schema(description = "价格列表")
    List<PriceInfoData> priceList;

    @Schema(description = "尖峰平谷价格信息")
    List<PriceTypeInfoData> priceTypeList;

}

