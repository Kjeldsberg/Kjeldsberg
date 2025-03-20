package no.fun.stuff.game.spaceship;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.Body;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.game.objects.Triangle;
import no.fun.stuff.engine.game.util.Util;
import no.fun.stuff.engine.matrix.Vector2D;
import no.fun.stuff.game.tool.ploygon.plotter.WritePlottedPoints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level1 extends Body {
    private final String levelFile = "save_4.txt";
    public List<Triangle> getTriangleList() {
        return triangleList;
    }

    final List<Triangle> triangleList = new ArrayList<>();
    final List<Vector2D> centerList = new ArrayList<>();

    public Level1() {
        List<Vector2D> list = new WritePlottedPoints().readPoints(levelFile);
        List<Vector2D> treePointList = makeTriangles(list);
        localCoordinate = new Vector2D[treePointList.size()];
        worldCoordinate = new Vector2D[treePointList.size()];
        treePointList.toArray(localCoordinate);
        for (int i = 0; i < localCoordinate.length; i++) {
            worldCoordinate[i] = new Vector2D();
        }
        midPoint(localCoordinate);
        setUpMap(localCoordinate);
    }
    @Override
    public void scale(float scale) {
        super.scale(scale);
        for(int i=0;i<centerList.size();i++) {
            triangleList.get(i).moveTo(centerList.get(i).mul(scale));
            triangleList.get(i).scale(scale);
        }
    }
    public void setUpMap(final Vector2D[] treePoints) {
        for(int i = 0; i < treePoints.length - 3; i += 3) {
            Vector2D[] po = {treePoints[i], treePoints[i+1], treePoints[i+2] };
            centerList.add(Triangle.getCenter(po));
            Triangle e = new Triangle(new Vector2D(po[0]), new Vector2D(po[1]), new Vector2D(po[2]), 0xff442288);
            triangleList.add(e);
            e.setStatic(true);
            e.setWireFrame(true);
        }
    }

    public List<Vector2D> makeTriangles(final List<Vector2D> toPointList) {
        List<Vector2D> allPoints = new ArrayList<>();
        for (int i = 0; i < toPointList.size() - 1; i++) {
            int j = i + 1;
            final Vector2D pi = new Vector2D(toPointList.get(i));
            final Vector2D pj = new Vector2D(toPointList.get(j));
            allPoints.addAll(Arrays.asList(pi, pj, new Vector2D(calckPerpendicular(pi, pj))));
        }
        return allPoints;
    }

    private static Vector2D calckPerpendicular(Vector2D pi, Vector2D pj) {
        final Vector2D midPoint = new Vector2D();
        final Vector2D perpendicular = new Vector2D();
        final Vector2D line = pj.minus(pi);
        Util.lerp(0.5f, pi, pj, midPoint);
        perpendicular.setXY(line.getY(), -line.getX());
        perpendicular.normaize();
        final Vector2D lastPoint = midPoint.add(perpendicular.mul(0.5f));
        return lastPoint;
    }

    public void midPoint(final Vector2D[] points) {
        Vector2D center = Body.getCenter(points);
        Vector2D thsub = new Vector2D();
        thsub.sub(center);
        for (Vector2D p : points) {
            p.pluss(thsub);
        }
    }

    @Override
    public Vector2D applyForces() {
        return Vector2D.ZERO;
    }

    @Override
    public void update(SceneObject parent, float dt) {

    }

    @Override
    public void render(SceneObject parent, Renderer r) {
//        calculateModel();
        for(int i = 0;i<triangleList.size();i++) {
            triangleList.get(i).render(parent, r);
        }
        calculateViewModel(parent);
        for (int i = 0; i < localCoordinate.length - 1; i++) {
            r.drawLine(getViewModel().mul(localCoordinate[i]), getViewModel().mul(localCoordinate[i + 1]), 0xff5599ff);
        }
    }
}
