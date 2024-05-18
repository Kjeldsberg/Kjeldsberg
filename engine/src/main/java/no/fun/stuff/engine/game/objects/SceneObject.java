package no.fun.stuff.engine.game.objects;

import java.util.ArrayList;
import java.util.List;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.BodyType;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public abstract class SceneObject {
    protected BodyType bodyType;
    protected String target;
    protected Matrix3x3 rotate = new Matrix3x3();
    protected Matrix3x3 scale = new Matrix3x3();
    protected Matrix3x3 translate = new Matrix3x3();
    protected Matrix3x3 model = new Matrix3x3();
    protected Matrix3x3 viewModel = new Matrix3x3();

    protected List<SceneObject> child = new ArrayList<SceneObject>();
    protected SceneObject parent;
	protected Vector2D pos = new Vector2D();
	protected Vector2D motionVector = new Vector2D();
	protected Vector2D scaleValue = new Vector2D();
	protected Matrix3x3 forInverseRotate = new Matrix3x3();
	protected float angle;
    protected boolean dead = false;

    public abstract void update(final SceneObject parent, float dt);

    public abstract void render(final SceneObject parent, final Renderer r);

    public void addChild(final SceneObject e) {
        child.add(e);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public Vector2D getPos() {
        return pos;
    }

    public void setPos(Vector2D pos) {
        this.pos = pos;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Matrix3x3 getRotate() {
        return rotate;
    }

    public void setRotate(Matrix3x3 rotate) {
        this.rotate = rotate;
    }

    public Matrix3x3 getScale() {
        return scale;
    }

    public void setScale(Matrix3x3 scale) {
        this.scale = scale;
    }

    public Matrix3x3 getTranslate() {
        return translate;
    }

    public void setTranslate(Matrix3x3 translate) {
        this.translate = translate;
    }

    public List<SceneObject> getChild() {
        return child;
    }

    public void setChild(List<SceneObject> child) {
        this.child = child;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public SceneObject getParent() {
        return parent;
    }

    public void setParent(SceneObject parent) {
        this.parent = parent;
    }

    public Matrix3x3 getModel() {
        return model;
    }

    public Matrix3x3 getViewModel() {
        return viewModel;
    }
    public void setModel(Matrix3x3 model) {
        this.model = model;
    }


    public void calculateModel() {
		forInverseRotate.set(rotate);
        scaleValue = scale.getScale();
        motionVector.setXY(translate.getMotionVector());
        translate.translate(pos);
        rotate.rotate(angle);
        model.fastMul(translate, rotate, scale);
    }
    public void calculateViewModel(final SceneObject parent) {
        calculateModel();
        if (parent != null) {
            this.viewModel.set(parent.getModel());
            viewModel.fastMul(this.getModel());
        } else {
            viewModel.set(getModel());
        }

    }
	Matrix3x3 translateInv = new Matrix3x3();
	Matrix3x3 rotateInv = new Matrix3x3();
	Matrix3x3 scaleInv = new Matrix3x3();
	Matrix3x3 modelInv = new Matrix3x3();
    public Matrix3x3 calculateInverseModel() {
        translateInv.translate(motionVector);
		translateInv.invertTranslate();
		scaleInv.scale(scaleValue);
		scaleInv.invertScale();
		rotateInv.set(forInverseRotate);
		rotateInv.transpose();
        modelInv.fastMul(scaleInv, rotateInv, translateInv);
        return modelInv;
    }
}
