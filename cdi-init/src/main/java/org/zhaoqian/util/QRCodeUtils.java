
package org.zhaoqian.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeUtils {

	/**
	 * 生成QuickResponseCode[二维码]
	 */
	public static byte[] generateQuickResponseCode(String mangroveToken, int width, int height) throws WriterException, IOException {

		Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
		hints.put(EncodeHintType.MARGIN, 0);
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		BitMatrix matrix = new QRCodeWriter().encode(mangroveToken, BarcodeFormat.QR_CODE, width, height, hints);
		ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(matrix, "PNG", pngOut);
		return pngOut.toByteArray();
	}
}
