package com.esllo.ch;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class CVDraw extends JPanel {
	private static final long serialVersionUID = 20165126L;
	BufferedImage image = null;

	public CVDraw() {
		image = new BufferedImage(400, 300, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = image.createGraphics();
		g.setPaint(new Color(0, 0, 0));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
	}

	public void update(BufferedImage image) {
		this.image = image;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null)
			g.drawImage(image, 0, 0, 400, 300, null);
	}
}