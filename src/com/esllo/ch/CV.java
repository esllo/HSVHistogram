package com.esllo.ch;

import static org.opencv.core.Core.inRange;
import static org.opencv.core.Core.split;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2HSV;
import static org.opencv.imgproc.Imgproc.cvtColor;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import sun.management.HotspotThreadMBean;

public class CV {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	// BufferedImage to Mat
	public static Mat toMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		// BufferedImage -> Mat
		mat.put(0, 0, ((DataBufferByte) bi.getRaster().getDataBuffer()).getData());
		Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2BGR);
		return mat;
	}

	// Mat to BufferedImage
	public static BufferedImage toBufferedImage(Mat mat) {
		BufferedImage bo = new BufferedImage(mat.cols(), mat.rows(), (mat.channels() < 2) ? BufferedImage.TYPE_BYTE_GRAY
				: BufferedImage.TYPE_3BYTE_BGR);
		byte[] data = new byte[mat.cols() * mat.rows() * (int) mat.elemSize()];
		mat.get(0, 0, data);
		bo.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		return bo;
	}

	public static int hRange(double hValue) {
		for (int h = 0; h < 6; h++) { // 6
			if (hValue > h * 30 && hValue < (h + 1) * 30)
				return h;
		}
		return 0;
	}

	public static int sRange(double sValue) {
		for (int s = 0; s < 8; s++) { // 8
			if (sValue > s * 32 && sValue < (s + 1) * 32)
				return s;
		}
		return 0;
	}

	public static int vRange(double vValue) {
		for (int v = 0; v < 8; v++) { // 8
			if (vValue > v * 32 && vValue < (v + 1) * 32)
				return v;
		}
		return 0;
	}

	public static void hsSeparation(Mat mat) {
		long start = System.currentTimeMillis();
		int[][][] hsv = new int[6][8][8];
		for (int[][] dim2 : hsv)
			for (int[] dim1 : dim2)
				Arrays.fill(dim1, 0);
		int allCount = 0;
		float avg;
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {
				double[] px = mat.get(row, col);
				hsv[hRange(px[0])][sRange(px[1])][vRange(px[2])]++;
				allCount++;
			}
		}
		avg = (float) allCount / (float) (6 * 8 * 8);
		avg *= 0.2;
		for (int h = 0; h < 6; h++) {
			for (int s = 0; s < 8; s++) {
				for (int v = 0; v < 8; v++) {
					if (hsv[h][s][v] > avg) {
						Mat dim = new Mat(mat.size(), CvType.CV_8U, new Scalar(255d));
						inRange(mat, new Scalar(h * 30, s * 32, v * 32), new Scalar((h + 1) * 30, (s + 1) * 32, (v + 1)
								* 32), dim);
						try {
							ImageIO.write(CV.toBufferedImage(dim), "png", new File("mat" + h + "" + s + "" + v
									+ ".png"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		System.out.println(System.currentTimeMillis() - start);
		// Mat msk;
		// inRange(mat, new Scalar(val, val2, val3), new Scalar(val4, val5,
		// val6), msk); // 범위 값 처리
		// return msk;
	}

}
