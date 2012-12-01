package de.begraph.model;

public class GLColor3f {
	private float red_, green_, blue_;

	/**
	 * Class constructor specifying a rgb-color
	 * 
	 * @param r
	 *            Red (0..1)
	 * @param g
	 *            Green (0..1)
	 * @param b
	 *            Blue (0..1)
	 */
	public GLColor3f(float r, float g, float b) {
		setRed(r);
		setGreen(g);
		setBlue(b);
	}

	public void setRed(float red_) {
		this.red_ = red_;
	}

	public float getRed() {
		return red_;
	}

	public void setGreen(float green_) {
		this.green_ = green_;
	}

	public float getGreen() {
		return green_;
	}

	public void setBlue(float blue_) {
		this.blue_ = blue_;
	}

	public float getBlue() {
		return blue_;
	}
}