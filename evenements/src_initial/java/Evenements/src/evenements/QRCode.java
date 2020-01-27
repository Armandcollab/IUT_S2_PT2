package evenements;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRCode {
    /**
     * Créé le QR code dans le fichier (png) donné en argument
     * 
     * Par exemple: QRCode.ecrireQRCode("qr-code.png", "http://google.com/", 512)
     */
    public static boolean ecrireQRCode(String fichier, String donnees, int taille) {
        try {
            BitMatrix matrix = new QRCodeWriter().encode(donnees, BarcodeFormat.QR_CODE, taille, taille);

            FileOutputStream imageFileStream = new FileOutputStream(fichier);
            MatrixToImageWriter.writeToStream(matrix, "png", imageFileStream);
            imageFileStream.close();
        } catch (IOException | WriterException e) {
            return false;
        }

        return true;
    }

    
    /**
     * Si la taille n'est pas fournie, elle vaudra 512 par défaut
     */
    public static boolean ecrireQRCode(String fichier, String donnees) {
        return ecrireQRCode(fichier, donnees, 512);
    }
}
