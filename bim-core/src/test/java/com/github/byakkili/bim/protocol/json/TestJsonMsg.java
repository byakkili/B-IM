package com.github.byakkili.bim.protocol.json;

import com.github.byakkili.bim.protocol.json.JsonMsg;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Guannian Li
 */
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TestJsonMsg implements JsonMsg {
    private Integer cmd;
    private Long seq;
    private String content;
}