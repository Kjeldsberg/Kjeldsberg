package no.fun.stuff.engine;


public class GameContainer implements Runnable{
	private Window window;
	private Thread thread;
	private Renderer renderer;
	private Input input;
	private AbstractGame game;
	private boolean running = false;
	private double UPDATE_CAP = 1/60.0;
	private int with = 320, height = 240;
	private float scale = 3f;
	private String title = "Spaceship landing";
	
	public GameContainer(final AbstractGame game) {
		this.game = game;
	}

	public void start() {
		window = new Window(this);
		renderer = new Renderer(this);
		input = new Input(this);
		thread = new Thread(this);
		thread.run();
	}
	
	public void stop() {
		
	}
	
	@Override
	public void run() {
		boolean render = false;	
		running = true;
		double firstTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0d;
		double passedTime = 0;
		double processedTime = 0;
		double unprocessedTime = 0;
		double frameTime = 0	;
		int frames = 0;
		int fps = 0;
		
		game.init(this);
		while(running) {
			render = true;
			firstTime = System.nanoTime() / 1000000000.0d;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;
			unprocessedTime += passedTime;
			frameTime += passedTime;
			
			while(unprocessedTime >= UPDATE_CAP) {
				unprocessedTime -= UPDATE_CAP;
				render = true;
				game.update(this, (float)UPDATE_CAP);
				if(frameTime >= 1.0) {
					frameTime = 0;
					fps = frames;
					frames = 0;					
				}
				
			}
			if(render) {
				renderer.clear();
				game.render(this, renderer);
				renderer.process();
				renderer.setCamX(0);
				renderer.setCamY(0);
				renderer.drawText(" FPS: " + fps, 0,0,0xffffffff);
				window.updata();
				frames++;
					
			}else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				
		}
		dispose();
	}
	public void dispose() {
		
	}
	public int getWith() {
		return with;
	}
	public void setWith(int with) {
		this.with = with;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Window getWindow() {
		return window;
	}

	public Input getInput() {
		return input;
	}

	public Renderer getRenderer() {
		return renderer;
	}
	
}
