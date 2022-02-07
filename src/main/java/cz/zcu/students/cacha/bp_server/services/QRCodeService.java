package cz.zcu.students.cacha.bp_server.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * Class represent service which is responsible for QR code generation
 */
@Service
public class QRCodeService {

    @Autowired
    private QRCodeWriter qrCodeWriter;

    /**
     * Generates QR code with given text
     * @param text text to encode
     * @return image of QR code
     * @throws Exception exception
     */
    public BufferedImage generateQRCodeImage(String text) throws Exception {
        // generate image matrix
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
        // return matrix as image
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
