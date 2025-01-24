package org.dromara.kp.protocol.yunkuaichong.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class GunRunStatusProto {
    private long ts;
    private String pileCode;
    private String gunCode;
    private GunRunStatus gunRunStatus;
    private List<String> faultMessages;
    private String additionalInfo;


    @Getter
    public enum GunRunStatus {
        IDLE,
        INSERTED,
        CHARGING,
        CHARGE_COMPLETE,
        DISCHARGE_READY,
        DISCHARGING,
        DISCHARGE_COMPLETE,
        RESERVED,
        FAULT,
        UNKNOWN
    }
}


