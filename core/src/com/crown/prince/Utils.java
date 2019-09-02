package com.crown.prince;

import com.crown.prince.components.BoundsComponent;
import com.crown.prince.components.PositionComponent;

public class Utils {
    public static boolean isTouching(int touching, int side) {
        return (touching & side) == side;
    }

    public static boolean doesAllow(int touching, int side) {
        return (touching & side) == side;
    }

    public static boolean overlaps(PositionComponent pos1, BoundsComponent bounds1, PositionComponent pos2, BoundsComponent bounds2) {
            if (pos1.x + bounds1.w < pos2.x ||
                pos1.x > pos2.x + bounds2.w ||
                pos1.y > pos2.y + bounds2.h ||
                pos1.y + bounds1.h < pos2.y) return false;
        return true;
    }
}
