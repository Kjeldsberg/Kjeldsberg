package no.fun.stuff.engine.game;

import no.fun.stuff.engine.Renderer;
import no.fun.stuff.engine.gfx.Image;
import no.fun.stuff.engine.matrix.Vector2D;

public class ImageLevel extends SceneObject {
	public ImageLevel(String pathToImage) {
		Image level = new Image(pathToImage);
 		final float w = GameManager.TS*0.5f;
 		final float h = GameManager.TS*0.5f;
 		for(int y=0;y<level.getH();y++) {
			for(int x =0;x<level.getW();x++) {
				if(level.getP()[x + y *level.getW()] == 0xff000000) {
					final Rect rec = new Rect(new Vector2D((x*2+1)*w, (y*2+1)*h));
					rec.setColor(0xff444444);
					getChild().add(rec);
				}
			}
		}
	}

	@Override
	public void update(SceneObject parent, float dt) {
		this.translate.translate(pos);
		localSpace.set(translate.fastMulCopy(rotate).fastMulCopy(scale).getCopy());
		for(int i=0;i<child.size();i++) {
			child.get(i).update(this, dt);
		}
		
	}

	@Override
	public void render(SceneObject parent, Renderer r) {
		if(parent != null) {
			modelView.set(parent.localSpace.fastMulCopy(this.localSpace).getCopy());
		} else {
			modelView.set(this.localSpace.getCopy());
		}
		for(int i=0;i<child.size();i++) {
			getChild().get(i).render(this, r);
		}
	}

}
