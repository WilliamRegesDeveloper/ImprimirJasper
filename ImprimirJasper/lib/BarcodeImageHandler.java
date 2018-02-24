package net.sourceforge.barbecue;

import java.util.Collections;
import java.util.HashSet;
import java.awt.image.RenderedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.OutputStream;
import net.sourceforge.barbecue.output.OutputException;
import java.awt.image.BufferedImage;
import java.util.Set;

public final class BarcodeImageHandler
{
    private static Set formats;
    
    public static Set getImageFormats() {
        return BarcodeImageHandler.formats;
    }
    
    public static BufferedImage getImage(final Barcode barcode) throws OutputException {
        final BufferedImage bufferedImage = new BufferedImage(barcode.getWidth(), barcode.getHeight(), 13);
        barcode.draw(bufferedImage.createGraphics(), 0, 0);
        bufferedImage.flush();
        return bufferedImage;
    }
    
    public static void writeJPEG(final Barcode barcode, final OutputStream outputStream) throws OutputException {
        writeImage(barcode, "jpeg", outputStream);
    }
    
    public static void saveJPEG(final Barcode barcode, final File file) throws OutputException {
        saveImage(barcode, "jpeg", file);
    }
    
    public static void savePNG(final Barcode barcode, final File file) throws OutputException {
        saveImage(barcode, "png", file);
    }
    
    public static void saveGIF(final Barcode barcode, final File file) throws OutputException {
        saveImage(barcode, "gif", file);
    }
    
    private static void saveImage(final Barcode barcode, final String s, final File file) throws OutputException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            writeImage(barcode, s, outputStream);
            outputStream.flush();
        }
        catch (IOException ex) {
            throw new OutputException(ex);
        }
        finally {
            try {
                ((FileOutputStream)outputStream).close();
            }
            catch (IOException ex2) {}
        }
    }
    
    public static void writePNG(final Barcode barcode, final OutputStream outputStream) throws OutputException {
        writeImage(barcode, "png", outputStream);
    }
    
    public static void writeGIF(final Barcode barcode, final OutputStream outputStream) throws OutputException {
        writeImage(barcode, "gif", outputStream);
    }
    
    private static void writeImage(final Barcode barcode, final String s, final OutputStream outputStream) throws OutputException {
        final BufferedImage image = getImage(barcode);
        try {
            ImageIO.write(image, s, outputStream);
        }
        catch (IOException ex) {
            throw new OutputException("exception while writing image", ex);
        }
    }
    
    static {
        final HashSet<String> set = new HashSet<String>();
        set.add("gif");
        set.add("jpeg");
        set.add("png");
        BarcodeImageHandler.formats = Collections.unmodifiableSet((Set<?>)set);
    }
}
