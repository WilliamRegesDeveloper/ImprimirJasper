package net.sourceforge.barbecue;

import net.sourceforge.barbecue.output.OutputException;
import net.sourceforge.barbecue.output.Output;

public class BlankModule extends Module
{
    public BlankModule(final int n) {
        super(new int[] { n });
    }
    
    protected int draw(final Output output, final int n, final int n2, final int n3, final int n4) throws OutputException {
        output.toggleDrawingColor();
        final int draw = super.draw(output, n, n2, n3, n4);
        output.toggleDrawingColor();
        return draw;
    }
    
    public String getSymbol() {
        return "";
    }
}
