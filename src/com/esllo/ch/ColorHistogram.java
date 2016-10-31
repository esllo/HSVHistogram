package com.esllo.ch;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

public class ColorHistogram {

	public static void main(String[] args) {
		new ColorHistogram();
	}

	JFrame frame;

	public ColorHistogram() {
		initGUI();
		histogram();
	}

	public void initGUI() {
		frame = new JFrame("HSV-Histogram");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public BufferedImage openImage() {
		JFileChooser jc = new JFileChooser();
		if (jc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			File img = jc.getSelectedFile();
			try {
				return (BufferedImage) ImageIO.read(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void histogram() {
		Mat image = CV.toMat(openImage());
		// Mat src = new Mat(image.height(), image.width(), CvType.CV_8UC2);

		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);

		List<Mat> hsv_planes = new ArrayList<Mat>();
		Core.split(image, hsv_planes);

		MatOfInt histSize = new MatOfInt(256);

		final MatOfFloat histRange = new MatOfFloat(0f, 256f);

		boolean accumulate = false;

		Mat h_hist = new Mat();
		Mat s_hist = new Mat();
		Mat v_hist = new Mat();

		// error appear in the following sentences

		Imgproc.calcHist(hsv_planes, new MatOfInt(0), new Mat(), h_hist, histSize, histRange, accumulate);
		Imgproc.calcHist(hsv_planes, new MatOfInt(1), new Mat(), s_hist, histSize, histRange, accumulate);
		Imgproc.calcHist(hsv_planes, new MatOfInt(2), new Mat(), v_hist, histSize, histRange, accumulate);

		int hist_w = 1024;
		int hist_h = 1024;
		long bin_w = Math.round((double) hist_w / 256);
		// bin_w = Math.round((double) (hist_w / 256));

		Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC3);
		Core.normalize(h_hist, h_hist, 3, histImage.rows(), Core.NORM_MINMAX);
		Core.normalize(s_hist, s_hist, 3, histImage.rows(), Core.NORM_MINMAX);
		Core.normalize(v_hist, v_hist, 3, histImage.rows(), Core.NORM_MINMAX);

		for (int i = 1; i < 256; i++) {
			Point p1 = new Point(bin_w * (i - 1), hist_h - Math.round(h_hist.get(i - 1, 0)[0]));
			Point p2 = new Point(bin_w * (i), hist_h - Math.round(h_hist.get(i, 0)[0]));
			Core.line(histImage, p1, p2, new Scalar(255, 0, 0), 2, 8, 0);

			Point p3 = new Point(bin_w * (i - 1), hist_h - Math.round(s_hist.get(i - 1, 0)[0]));
			Point p4 = new Point(bin_w * (i), hist_h - Math.round(s_hist.get(i, 0)[0]));
			Core.line(histImage, p3, p4, new Scalar(0, 255, 0), 2, 8, 0);

			Point p5 = new Point(bin_w * (i - 1), hist_h - Math.round(v_hist.get(i - 1, 0)[0]));
			Point p6 = new Point(bin_w * (i), hist_h - Math.round(v_hist.get(i, 0)[0]));
			Core.line(histImage, p5, p6, new Scalar(0, 0, 255), 2, 8, 0);

		}

		Highgui.imwrite("histogram.jpg", histImage);
	}

}
