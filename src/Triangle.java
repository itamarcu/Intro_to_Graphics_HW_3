public class Triangle extends Shape {
    final Vec3[] vertices; // size = 3
    //extras
    final Plane plane; // is defined from vertices. Material is the same.
    final Vec3[] sideNormals; // all pointing into center

    public Triangle(Vec3 vertex1, Vec3 vertex2, Vec3 vertex3, int materialIndex) {
        super(materialIndex);
        this.vertices = new Vec3[]{vertex1, vertex2, vertex3};

        Vec3 normal = vertex1.minus(vertex2).cross(vertex2.minus(vertex3)).normalized();
        double offset = vertex1.projectedOn(normal).magnitude();
        this.plane = new Plane(normal, offset, materialIndex);

        this.sideNormals = new Vec3[3];
        for (int side = 0; side < 3; side++) {
            Vec3 side1 = vertices[side].minus(vertices[(side + 1) % 3]);
            Vec3 side2 = vertices[(side + 1) % 3].minus(vertices[(side + 2) % 3]);
            sideNormals[side] = side2.projectionPerpendicularTo(side1).scaledBy(-1).normalized();
        }
    }

    public String toString() {
        return "Triangle(" + vertices[0] + ", " + vertices[1] + ", " + vertices[2] + ", " + materialIndex + ")";
    }

//    @Override
//    public Intersection findRayIntersection(Vec3 origin, Vec3 direction) {
//        Intersection planeIntersection = plane.findRayIntersection(origin, direction);
//        if (planeIntersection == null)
//            return null;
//        Vec3 point = planeIntersection.position;
//        // check that the point is inside the triangle
//        for (int side = 0; side < 3; side++) {
//            double d1 = origin.scaledBy(-1).dot(sideNormals[side]);
//            double projOnNormToSide = point.dot(sideNormals[side]) + d1;
//            if (projOnNormToSide < 0)
//                return null;
//        }
//
//        return planeIntersection;
//    }

    @Override
    public Intersection findRayIntersection(Vec3 origin, Vec3 direction)
    {
        Intersection planeIntersection = plane.findRayIntersection(origin, direction);
        if (planeIntersection == null)
            return null;
        Vec3 point = planeIntersection.position;
        Vec3 lineToPoint = point.minus(origin);
        // check that the point is inside the triangle
        for (int side = 0; side < 3; side ++) {
            Vec3 V1 = vertices[side].minus(point);
            Vec3 V2 = vertices[(side + 1) % 3].minus(point);
            Vec3 normalToSide = V2.cross(V1).normalized();
            double projOnNormToSide = lineToPoint.dot(normalToSide);
            if (projOnNormToSide < 0)
                return null;
        }

        return planeIntersection;
    }
}
