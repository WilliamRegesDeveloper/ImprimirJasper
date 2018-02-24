package net.sourceforge.barbecue;

public class BarcodeException extends Exception
{
    public BarcodeException(final String s) {
        super(s);
    }
    
    public BarcodeException(final String s, final Throwable t) {
        super(s, t);
    }
}
