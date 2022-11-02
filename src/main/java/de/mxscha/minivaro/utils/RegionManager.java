package de.mxscha.minivaro.utils;

import org.bukkit.Location;

public class RegionManager {

    public boolean isInRegion(Location location, Location inlocationA, Location inlocationB) {
        Double maxX = (inlocationA.getX() > inlocationB.getX() ? inlocationA.getX() : inlocationB.getX());
        Double minX = (inlocationA.getX() < inlocationB.getX() ? inlocationA.getX() : inlocationB.getX());
        Double maxY = (inlocationA.getY() > inlocationB.getY() ? inlocationA.getY() : inlocationB.getY());
        Double minY = (inlocationA.getY() < inlocationB.getY() ? inlocationA.getY() : inlocationB.getY());
        Double maxZ = (inlocationA.getZ() > inlocationB.getZ() ? inlocationA.getZ() : inlocationB.getZ());
        Double minZ = (inlocationA.getZ() < inlocationB.getZ() ? inlocationA.getZ() : inlocationB.getZ());

        if (location.getX() <= maxX && location.getX() >= minX) {
            if (location.getY() <= maxY && location.getY() >= minY) {
                if (location.getZ() <= maxZ && location.getZ() >= minZ) {
                    return true;
                }
            }
        }

        return false;
    }
}
