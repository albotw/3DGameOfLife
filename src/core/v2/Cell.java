package core.v2;

import java.util.Objects;

public class Cell {
    public final int x;
    public final int y;
    public final int z;

    public Cell(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y && z == cell.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
