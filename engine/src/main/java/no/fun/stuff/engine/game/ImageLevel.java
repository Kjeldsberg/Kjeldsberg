package no.fun.stuff.engine.game;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.game.objects.Rect;
import no.fun.stuff.engine.game.objects.Rectangle;
import no.fun.stuff.engine.game.objects.SceneObject;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Vector2D;
public class ImageLevel extends SceneObject {
	private final Image level;
	public ImageLevel(String pathToImage) {
		level = new Image(pathToImage);
 		final float w = Rectangle.TS*0.5f;
 		final float h = Rectangle.TS*0.5f;
		Vector2D size = new Vector2D(w, h);
		for(int y=0;y<level.getH();y++) {
			for(int x =0;x<level.getW();x++) {
				if(level.getP()[x + y *level.getW()] == 0xff000000) {
					final Rect rec = new Rect(size, new Vector2D((x*2+1)*w, (y*2+1)*h));
					rec.setColor(0xff444444);
					getChild().add(rec);
				}
			}
		}
	}

	@Override
	public void update(SceneObject parent, float dt) {
//		this.translate.translate(pos);
//		model.set(translate.fastMulCopy(rotate).fastMulCopy(scale).getCopy());
//		for(int i=0;i<child.size();i++) {
//			child.get(i).update(this, dt);
//		}
		
	}

	@Override
	public void render(SceneObject parent1, Renderer r) {

//		if(parent1 != null) {
//			Matrix3x3 viewModel = parent1.model.fastMulCopy(this.model);
//			this.model.set(viewModel.getCopy());
//		} else {
//			this.model.set(this.model.getCopy());
//		}

		for(int i=0;i<child.size();i++) {
			getChild().get(i).render(parent1, r);
		}
	}

	public int getWidth() {
		return level.getW();
	}
	public int getHeight() {
		return level.getH();
	}
}
