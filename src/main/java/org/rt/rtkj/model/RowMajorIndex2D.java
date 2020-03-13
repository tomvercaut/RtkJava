package org.rt.rtkj.model;

import java.util.Optional;

public class RowMajorIndex2D {
    public int rows;
    public int cols;

    public Optional<Integer> offset(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) return Optional.empty();
        return Optional.of(r * cols + c);
    }

    public int size() {
        return rows * cols;
    }
}
