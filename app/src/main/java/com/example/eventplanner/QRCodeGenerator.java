package com.example.eventplanner;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;
/**
 * Utility class for generating QR codes.
 * This class provides a static method to generate a QR code image with the given event ID and type.
 */
public class QRCodeGenerator {
    /**
     * Generates a QR code bitmap image with the specified event ID and type.
     *
     * @param eventID The ID of the event.
     * @param checkin_or_promo The type of QR code ("checkin" or "promo").
     * @param width The width of the QR code image.
     * @param height The height of the QR code image.
     * @return A Bitmap representing the generated QR code.
     * @throws WriterException if there is an error encoding the QR code.
     */
    public static Bitmap generateQRCode(String eventID, String checkin_or_promo, int width, int height) throws WriterException {
        String text = eventID + ":" + checkin_or_promo;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hintMap);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }
}

