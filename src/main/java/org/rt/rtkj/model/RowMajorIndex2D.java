package org.rt.rtkj.model;

import org.rt.rtkj.Option;

public class RowMajorIndex2D {
    public int rows;
    public int cols;

    public Option<Integer> offset(int c, int r) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) return Option.empty();
        return Option.of(r * cols + c);
    }

    public int size() {
        return rows * cols;
    }
}
