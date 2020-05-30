package com.github.byakkili.bim.core.protocol.impl.protobuf;

import com.google.protobuf.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author Guannian Li
 */
@Getter
@AllArgsConstructor
public class ProtobufFrame implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * CMD
     */
    private Integer cmd;
    /**
     * protobuf
     */
    private Message message;
}
