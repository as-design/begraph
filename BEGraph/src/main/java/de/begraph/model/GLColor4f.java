package de.begraph.model;

public class GLColor4f {
	private float red_, green_, blue_, transparency_;

	/**
	 * Class constructor specifying a rgb-color with transparency
	 * 
	 * @param r
	 *            Red (0..1)
	 * @param g
	 *            Green (0..1)
	 * @param b
	 *            Blue (0..1)
	 * @param t
	 *            Transparency (0..1)
	 */
	public GLColor4f(float r, float g, float b, float t) {
		setRed(r);
		setGreen(g);
		setBlue(b);
		setTransparency(t);
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

	public void setTransparency(float transparency_) {
		this.transparency_ = transparency_;
	}

	public float getTransparency() {
		return transparency_;
	}
}
