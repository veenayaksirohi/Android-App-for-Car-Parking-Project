package com.example.carparkingapp.google_map;

public class Place {
    private String name;
    private Geometry geometry;

    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getSlotsAvailable() {
        return name;
    }

    public static class Geometry {
        private Location location;

        public Location getLocation() {
            return location;
        }

        public static class Location {
            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public double getLng() {
                return lng;
            }
        }
    }
}
