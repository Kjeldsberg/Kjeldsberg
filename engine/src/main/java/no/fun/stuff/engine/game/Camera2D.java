package no.fun.stuff.engine.game;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class Camera2D extends SceneObject{

	private final Matrix3x3 view = new Matrix3x3();
	private final Matrix3x3 viewModel = new Matrix3x3();
	private final Matrix3x3 inversViewModel = new Matrix3x3();
	private final Matrix3x3 inversScale = new Matrix3x3();
	private final Matrix3x3 inversTranslate = new Matrix3x3();
	private final Matrix3x3 toCameraSpace = new Matrix3x3();
	private final Matrix3x3 toWorldSpace = new Matrix3x3();
	private final Vector2D position = new Vector2D();
	private SceneObject lookatObject = null;
	private boolean dirty = false;
	private final Vector2D screenSize;
	public Camera2D(final Vector2D screenSize) {
		this.screenSize = screenSize;
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
	public Camera2D(final Vector2D screenSize, final Vector2D up) {
		setUpVector(up);
		this.screenSize = screenSize;
//		project
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
//		translate.translate(pos);
		dirty = true;
	}

	@Override
	public void render(SceneObject parent, Renderer r) {
		if (lookatObject == null) {
			return;
		}
		model.setIdentity();
		Vector2D halfScreen = new Vector2D(screenSize.getX() * 0.5f, screenSize.getY() * 0.5f);
		final Vector2D minus = halfScreen.minus(position);
//		final Vector2D minus = position.minus(halfScreen);
		translate.translate(minus);

		model.set(translate.fastMulCopy(rotate).fastMulCopy(scale));
			int test = 0;
//		final Matrix3x3 modelview = view.fastMulCopy(model);
//		this.model.set(modelview.getCopy());
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
