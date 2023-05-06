package no.fun.stuff.engine.game;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class Camera2D extends SceneObject{

	private final Matrix3x3 viewModel = new Matrix3x3();
	private final Matrix3x3 inversViewModel = new Matrix3x3();
	private final Matrix3x3 inversScale = new Matrix3x3();
	private final Matrix3x3 inversTranslate = new Matrix3x3();
	private final Matrix3x3 toCameraSpace = new Matrix3x3();
	private final Matrix3x3 toWorldSpace = new Matrix3x3();
	private final Vector2D position = new Vector2D();
	private SceneObject lookatObject = null;
	private boolean dirty = false;

	public Camera2D() {
		final Vector2D defaultUp = new Vector2D(0, 1.0f);
		viewModel.setUpVector(defaultUp);
		inversViewModel.transpose(viewModel);
		scale.scale(1.0f);
		inversScale.scale(1.0f);
		translate.translate(position);
		inversTranslate.set(translate.getCopy());
		inversTranslate.invertTranslate();
		recalculateCameraSpace();
		recalculateWorldSpace();
	}

	public void lookAt(SceneObject target) {
		this.lookatObject = target;
	}
	@Override
	public void update(SceneObject parent, float dt) {
		if (lookatObject == null) {
			return;
		}
		final Vector2D pos = new Vector2D(lookatObject.getPos().getX(), lookatObject.getPos().getY());
		position.setXY(pos);
		translate.translate(pos);
		inversTranslate.translate(pos);
		inversTranslate.invertTranslate();
		dirty = true;
	}

	@Override
	public void render(SceneObject parent, Renderer r) {
		if(dirty) {
			recalculateCameraSpace();
			recalculateWorldSpace();
			this.localSpace.set(toWorldSpace.getCopy());
		}
		
	}


	public Camera2D(final Vector2D up) {
		setUpVector(up);
//		project
	}

	public void setUpVector(final Vector2D up) {
		up.normaize();
		viewModel.setUpVector(up);
		inversViewModel.transpose(viewModel);
	}

	public void recalculateCameraSpace() {
		toCameraSpace.clear();
		toCameraSpace.set(inversViewModel.fastMulCopy(inversScale).fastMulCopy(inversTranslate).getCopy());
	}

	public void recalculateWorldSpace() {
		toWorldSpace.clear();
		toWorldSpace.set(translate.fastMulCopy(scale).fastMulCopy(viewModel).getCopy());
	}

	public Matrix3x3 getViewModel() {
		return viewModel;
	}

	public Matrix3x3 getInversViewModel() {
		return inversViewModel;
	}

	public Matrix3x3 getScale() {
		return scale;
	}

	public Matrix3x3 getInversScale() {
		return inversScale;
	}

	public Matrix3x3 getTranslate() {
		return translate;
	}

	public Matrix3x3 getInversTranslate() {
		return inversTranslate;
	}

	public Vector2D getPosition() {
		return position;
	}

	public Matrix3x3 getToCameraSpace() {
		return toCameraSpace;
	}

	public Matrix3x3 getToWorldSpace() {
		return toWorldSpace;
	}


}
