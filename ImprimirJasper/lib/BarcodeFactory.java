package net.sourceforge.barbecue;

import net.sourceforge.barbecue.linear.postnet.PostNetBarcode;
import net.sourceforge.barbecue.linear.codabar.CodabarBarcode;
import net.sourceforge.barbecue.linear.code39.Code39Barcode;
import net.sourceforge.barbecue.twod.pdf417.PDF417Barcode;
import net.sourceforge.barbecue.linear.twoOfFive.Int2of5Barcode;
import net.sourceforge.barbecue.linear.twoOfFive.Std2of5Barcode;
import net.sourceforge.barbecue.linear.upc.UPCABarcode;
import net.sourceforge.barbecue.linear.ean.BooklandBarcode;
import net.sourceforge.barbecue.linear.ean.EAN13Barcode;
import net.sourceforge.barbecue.linear.ean.UCCEAN128Barcode;
import net.sourceforge.barbecue.linear.code128.Code128Barcode;

public final class BarcodeFactory
{
    public static Barcode createCode128(final String s) throws BarcodeException {
        return new Code128Barcode(s, 3);
    }
    
    public static Barcode createCode128A(final String s) throws BarcodeException {
        return new Code128Barcode(s, 0);
    }
    
    public static Barcode createCode128B(final String s) throws BarcodeException {
        return new Code128Barcode(s, 1);
    }
    
    public static Barcode createCode128C(final String s) throws BarcodeException {
        return new Code128Barcode(s, 2);
    }
    
    public static Barcode createUCC128(final String s, final String s2) throws BarcodeException {
        return new UCCEAN128Barcode(s, s2);
    }
    
    public static Barcode createEAN128(final String s) throws BarcodeException {
        return new UCCEAN128Barcode("01", s);
    }
    
    public static Barcode createUSPS(final String s) throws BarcodeException {
        return new UCCEAN128Barcode("420", s);
    }
    
    public static Barcode createShipmentIdentificationNumber(final String s) throws BarcodeException {
        return new UCCEAN128Barcode("402", s);
    }
    
    public static Barcode parseEAN128(final String s) throws BarcodeException {
        return new UCCEAN128Barcode(s);
    }
    
    public static Barcode createSSCC18(final String s) throws BarcodeException {
        return new UCCEAN128Barcode("00", s);
    }
    
    public static Barcode createSCC14ShippingCode(final String s) throws BarcodeException {
        return new UCCEAN128Barcode("01", s);
    }
    
    public static Barcode createGlobalTradeItemNumber(final String s) throws BarcodeException {
        return new UCCEAN128Barcode("01", s);
    }
    
    public static Barcode createEAN13(final String s) throws BarcodeException {
        return new EAN13Barcode(s);
    }
    
    public static Barcode createBookland(final String s) throws BarcodeException {
        return new BooklandBarcode(s);
    }
    
    public static Barcode createUPCA(final String s) throws BarcodeException {
        return new UPCABarcode(s);
    }
    
    public static Barcode createRandomWeightUPCA(final String s) throws BarcodeException {
        return new UPCABarcode(s, true);
    }
    
    public static Barcode createStd2of5(final String s) throws BarcodeException {
        return new Std2of5Barcode(s);
    }
    
    public static Barcode createStd2of5(final String s, final boolean b) throws BarcodeException {
        return new Std2of5Barcode(s, b);
    }
    
    public static Barcode createInt2of5(final String s) throws BarcodeException {
        return new Int2of5Barcode(s);
    }
    
    public static Barcode createInt2of5(final String s, final boolean b) throws BarcodeException {
        return new Int2of5Barcode(s, b);
    }
    
    public static Barcode createPDF417(final String s) throws BarcodeException {
        return new PDF417Barcode(s);
    }
    
    public static Barcode createCode39(final String s, final boolean b) throws BarcodeException {
        return new Code39Barcode(s, b);
    }
    
    public static Barcode create3of9(final String s, final boolean b) throws BarcodeException {
        return new Code39Barcode(s, b);
    }
    
    public static Barcode createUSD3(final String s, final boolean b) throws BarcodeException {
        return new Code39Barcode(s, b);
    }
    
    public static Barcode createCodabar(final String s) throws BarcodeException {
        return new CodabarBarcode(s);
    }
    
    public static Barcode createUSD4(final String s) throws BarcodeException {
        return new CodabarBarcode(s);
    }
    
    public static Barcode createNW7(final String s) throws BarcodeException {
        return new CodabarBarcode(s);
    }
    
    public static Barcode createMonarch(final String s) throws BarcodeException {
        return new CodabarBarcode(s);
    }
    
    public static Barcode create2of7(final String s) throws BarcodeException {
        return new CodabarBarcode(s);
    }
    
    public static Barcode createPostNet(final String s) throws BarcodeException {
        return new PostNetBarcode(s);
    }
}
