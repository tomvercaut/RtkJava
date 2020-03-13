package org.rt.rtkj.model;

import lombok.Data;

@Data
public class IPoint implements Point<Integer> {
    private Integer x;
    private Integer y;
    private Integer z;
    private Integer w;
}
