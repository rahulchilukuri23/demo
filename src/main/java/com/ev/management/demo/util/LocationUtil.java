package com.ev.management.demo.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class LocationUtil {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    // Convert Point to WKT string (e.g., "POINT(-122.3321 47.6062)")
    public static String getLocation(Point point) {
        if (point == null) return null;
        return point.toText();  // JTS Point provides WKT string
    }

    // Convert WKT string to Point object
    public static Point getLocation(String text) {
        if (text == null || text.isEmpty()) return null;
        text = text.trim();

        if (!text.startsWith("POINT")) {
            throw new IllegalArgumentException("Invalid WKT format for Point");
        }

        // Extract coordinates from WKT string: "POINT(x y)"
        int start = text.indexOf('(');
        int end = text.indexOf(')');
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException("Invalid WKT format for Point");
        }
        String coordText = text.substring(start + 1, end).trim();
        String[] coords = coordText.split("\\s+");
        if (coords.length != 2) {
            throw new IllegalArgumentException("Invalid coordinate format in Point");
        }

        double x = Double.parseDouble(coords[0]);
        double y = Double.parseDouble(coords[1]);

        Coordinate coordinate = new Coordinate(x, y);
        return geometryFactory.createPoint(coordinate);
    }
}
