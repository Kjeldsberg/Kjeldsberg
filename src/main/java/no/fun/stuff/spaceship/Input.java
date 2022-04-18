package no.fun.stuff.spaceship;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	private GameContainer gc;
	private static final int NUM_KEYS = 256;
	private boolean[] keys = new boolean[NUM_KEYS];
	private boolean[] keysLast = new boolean[NUM_KEYS];
	
	private static final int NUM_BUTTONS = 15;
	private boolean[] buttons = new boolean[NUM_BUTTONS];
	private boolean[] buttonsLast = new boolean[NUM_BUTTONS];
	
	private int mouseX, mouseY;
	private int scroll;
	public Input(final GameContainer spaceship) {
		this.gc = spaceship;
		mouseX = 0;
		mouseY = 0;
		scroll = 0;
		spaceship.getWindow().getCanvas().addKeyListener(this);
		spaceship.getWindow().getCanvas().addMouseListener(this);
		spaceship.getWindow().getCanvas().addMouseMotionListener(this);
		spaceship.getWindow().getCanvas().addMouseWheelListener(this);
	}

	public void updata() {
		scroll = 0;
		for(int i=0;i<NUM_KEYS;i++) {
			keysLast[i] = keys[i];
		}
		for(int i=0;i<NUM_BUTTONS;i++) {
			buttonsLast[i] = buttons[i];
		}
	}
	public boolean isKey(int keycode) {
		return keys[keycode];
	}
	public boolean isKeyUp(int keycode) {
		return !keys[keycode] && keysLast[keycode];
	}
	public boolean isKeyDown(int keycode) {
		return keys[keycode] && !keysLast[keycode];
	}
	public boolean isBotton(int keycode) {
		return buttons[keycode];
	}

	public boolean isButtonUp(int button) {
		return buttons[button] && !buttonsLast[button];
	}
	public boolean isButtonDown(int button) {
		return buttons[button] && !buttonsLast[button];
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = (int)(e.getX() /gc.getScale());
		mouseY = (int)(e.getY() /gc.getScale());
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = (int)(e.getX() /gc.getScale());
		mouseY = (int)(e.getY() /gc.getScale());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		scroll = e.getWheelRotation();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public int getScroll() {
		return scroll;
	}

}
