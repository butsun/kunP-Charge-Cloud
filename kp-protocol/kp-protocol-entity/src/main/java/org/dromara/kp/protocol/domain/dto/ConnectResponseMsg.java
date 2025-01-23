package org.dromara.kp.protocol.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectResponseMsg {
    private ConnectResponseCode responseCode;
    private String errorMsg;

    enum ConnectResponseCode {
        ACCEPTED,
        REFUSE
    }

}


