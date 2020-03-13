package org.rt.rtkj.model;

import lombok.Data;

@Data
public class DPoint implements Point<Double> {
    private Double x;
    private Double y;
    private Double z;
    private Double w;
}
