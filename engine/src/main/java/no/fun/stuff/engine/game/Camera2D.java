package no.fun.stuff.engine.game;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.matrix.Matrix3x3;
import no.fun.stuff.engine.matrix.Vector2D;

public class Camera2D extends SceneObject {

	private final Vector2D position = new Vector2D();
	private SceneObject lookatObject = null;
	private boolean dirty = false;
	private final Vector2D screenSize;
	private final Vector2D viewPort;
	private float zoom = 1.0f;
	public float getZoom() {
		return zoom;
	}
	public void setZoom(float zoom) {
		Vector2D pluss = scale.getScale().pluss(zoom, zoom);
		scale.scale(pluss);
		dirty = true;
	}

//	public Camera2D(final Vector2D screenSize) {
//		this.screenSize = screenSize;
//		this.viewPort = new Vector2D();
//		final Vector2D defaultUp = new Vector2D(0, 1.0f);
//		viewModel.setUpVector(defaultUp);
//		inversViewModel.transpose(viewModel);
//		scale.scale(1.0f);
//		inversScale.scale(1.0f);
//		translate.translate(position);
//		inversTranslate.set(translate.getCopy());
//		inversTranslate.invertTranslate();
//		recalculateCameraSpace();
//		recalculateWorldSpace();
//	}
	public Camera2D(final Vector2D screenSize, final Vector2D viewPort) {
		this.screenSize = screenSize;
		this.viewPort = viewPort;
		float scaleX = screenSize.getX() / viewPort.getX();
		float scaleY = screenSize.getY() / viewPort.getY();
		getScale().scale(new Vector2D(scaleX, scaleY));
		calculateModel();
	}

	public void lookAt(SceneObject target) {
		this.lookatObject = target;
	}
	@Override
	public void update(SceneObject parent, float dt) {
	}

	@Override
	public void render(SceneObject parent, Renderer r) {
	}




//	public void recalculateCameraSpace() {
//		toCameraSpace.clear();
//		toCameraSpace.set(inversViewModel.fastMulCopy(inversScale).fastMulCopy(inversTranslate).getCopy());
//	}
//
//	public void recalculateWorldSpace() {
//		toWorldSpace.clear();
//		toWorldSpace.set(translate.fastMulCopy(scale).fastMulCopy(viewModel).getCopy());
//	}

	public Matrix3x3 getViewModel() {
		return viewModel;
	}

//	public Matrix3x3 getInversViewModel() {
//		return inversViewModel;
//	}

	public Matrix3x3 getScale() {
		return scale;
	}

//	public Matrix3x3 getInversScale() {
//		return inversScale;
//	}

	public Matrix3x3 getTranslate() {
		return translate;
	}

//	public Matrix3x3 getInversTranslate() {
//		return inversTranslate;
//	}

	public Vector2D getPosition() {
		return position;
	}

//	public Matrix3x3 getToCameraSpace() {
//		return toCameraSpace;
//	}

//	public Matrix3x3 getToWorldSpace() {
//		return toWorldSpace;
//	}


}
