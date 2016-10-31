package com.esllo.ch;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

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
		BufferedImage bo = new BufferedImage(mat.cols(), mat.rows(),
				(mat.channels() < 2) ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR);
		byte[] data = new byte[mat.cols() * mat.rows() * (int) mat.elemSize()];
		mat.get(0, 0, data);
		bo.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		return bo;
	}

}
