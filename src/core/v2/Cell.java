package core.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Cell implements Serializable {
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
        Cell cell = (Cell) o;
        return x == cell.x && y == cell.y && z == cell.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public ArrayList<Cell> neighbours() {
        ArrayList<Cell> output = new ArrayList<>(18);
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++){
                for (int z = -1; z < 2; z++) {
                    output.add(new Cell(this.x - x, this.y - y, this.z - z));
                }
            }
        }

        return output;
    }
}
