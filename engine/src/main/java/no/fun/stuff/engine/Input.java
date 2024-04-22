package no.fun.stuff.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

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

	public Input(final GameContainer container) {
		this.gc = container;
		mouseX = 0;
		mouseY = 0;
		scroll = 0;
		container.getWindow().getCanvas().addKeyListener(this);
		container.getWindow().getCanvas().addMouseListener(this);
		container.getWindow().getCanvas().addMouseMotionListener(this);
		container.getWindow().getCanvas().addMouseWheelListener(this);
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

	public boolean isButton(int keycode) {
		return buttons[keycode];
	}

	public boolean isButtonUp(int button) {
		return !buttons[button] && buttonsLast[button];
	}

	public boolean isButtonDown(int button) {
		return buttons[button] && !buttonsLast[button];
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = (int)(e.getX() /gc.getScale());
		mouseY = (int)(e.getY() /gc.getScale());
//		System.out.println("Dragged x: " + mouseX + "y: " + mouseY);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = (int)(e.getX() /gc.getScale());
		mouseY = (int)(e.getY() /gc.getScale());
//		System.out.println("Mouse moved x: " + mouseX + "y: " + mouseY);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int tset = 0;

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
//		buttonsLast[button] = buttons[button];
		buttons[button] = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
//		buttonsLast[button] = buttons[button];
		buttons[button] = false;
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
