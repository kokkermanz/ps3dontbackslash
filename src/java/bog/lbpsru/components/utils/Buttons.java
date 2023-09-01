package bog.lbpsru.components.utils;

/**
 * @author Bog
 */
public enum Buttons {

    //offsets shamelessly stolen from racman https://github.com/MichaelRelaxen/racman/blob/master/RaCTrainer/Inputs.cs

    l2(0x1),
    r2(0x2),
    l1(0x4),
    r1(0x8),
    triangle(0x10),
    circle(0x20),
    cross(0x40),
    square(0x80),
    select(0x10000),
    l3(0x20000),
    r3(0x40000),
    start(0x80000),
    up(0x100000),
    right(0x200000),
    down(0x400000),
    left(0x800000);

    private int offset;

    Buttons(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public static Buttons fromOffset(int offset) {
        for (Buttons enumOffset : Buttons.values()) {
            if (enumOffset.getOffset() == offset) {
                return enumOffset;
            }
        }
        throw new IllegalArgumentException("No button with offset " + offset);
    }
}
