package no.fun.stuff.engine;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window {
	private GameContainer gc;
	private JFrame frame;
	private BufferedImage image;
	private Canvas canvas;
	private Graphics g;
	private BufferStrategy bs;
	
	public Window(final GameContainer container) {
		this.gc = container;
		image = new BufferedImage(container.getWith(), container.getHeight(), BufferedImage.TYPE_INT_RGB);
		canvas = new Canvas();
		final Dimension dim = new Dimension((int)(container.getWith() * container.getScale()), (int)(container.getHeight() * container.getScale()));
		canvas.setPreferredSize(dim);
		canvas.setMaximumSize(dim);
		canvas.setMinimumSize(dim);
		
		frame = new JFrame(container.getTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		g = bs.getDrawGraphics();

	}

	public void updata()
	{
		bs.getDrawGraphics().drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
		bs.show();
	}

	public BufferedImage getImage() {
		return image;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public JFrame getFrame() {
		return frame;
	}

	public Graphics getG() {
		return g;
	}
	
}
