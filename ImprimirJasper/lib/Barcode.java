package net.sourceforge.barbecue;

import java.awt.print.PrinterException;
import java.awt.print.PageFormat;
import java.awt.FontMetrics;
import net.sourceforge.barbecue.output.SizingOutput;
import net.sourceforge.barbecue.env.HeadlessEnvironment;
import java.text.StringCharacterIterator;
import java.awt.Insets;
import java.awt.Graphics;
import net.sourceforge.barbecue.output.OutputException;
import net.sourceforge.barbecue.output.Output;
import net.sourceforge.barbecue.output.GraphicsOutput;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import net.sourceforge.barbecue.env.EnvironmentFactory;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.print.Printable;
import javax.swing.JComponent;

public abstract class Barcode extends JComponent implements Printable
{
    private static final int DEFAULT_BAR_HEIGHT = 50;
    protected String data;
    protected String label;
    protected boolean drawingText;
    protected boolean drawingQuietSection;
    protected int barWidth;
    protected int barHeight;
    private Font font;
    private Dimension size;
    private int x;
    private int y;
    private int resolution;
    
    protected Barcode(final String data) throws BarcodeException {
        super();
        this.drawingQuietSection = true;
        this.barWidth = 2;
        this.resolution = -1;
        if (data == null || data.length() == 0) {
            throw new BarcodeException("Data to encode cannot be empty");
        }
        this.data = data;
        final int calculateMinimumBarHeight = this.calculateMinimumBarHeight(this.getResolution());
        if (calculateMinimumBarHeight > 0) {
            this.barHeight = calculateMinimumBarHeight;
        }
        else {
            this.barHeight = 50;
        }
        this.font = EnvironmentFactory.getEnvironment().getDefaultFont();
        this.drawingText = true;
        this.setBackground(Color.white);
        this.setForeground(Color.black);
        this.setOpaque(true);
        this.invalidateSize();
    }
    
    public String getData() {
        return this.data;
    }
    
    public void setFont(final Font font) {
        this.font = font;
        this.invalidateSize();
    }
    
    public void setDrawingText(final boolean drawingText) {
        this.drawingText = drawingText;
        this.invalidateSize();
    }
    
    public boolean isDrawingText() {
        return this.drawingText;
    }
    
    public void setDrawingQuietSection(final boolean drawingQuietSection) {
        this.drawingQuietSection = drawingQuietSection;
        this.invalidateSize();
    }
    
    public boolean isDrawingQuietSection() {
        return this.drawingQuietSection;
    }
    
    public void setBarWidth(final int barWidth) {
        if (barWidth >= 1) {
            this.barWidth = barWidth;
        }
        else {
            this.barWidth = 1;
        }
        this.invalidateSize();
    }
    
    public void setBarHeight(final int barHeight) {
        if (barHeight > this.calculateMinimumBarHeight(this.getResolution())) {
            this.barHeight = barHeight;
            this.invalidateSize();
        }
    }
    
    public void setResolution(final int resolution) {
        if (resolution > 0) {
            this.resolution = resolution;
            final int calculateMinimumBarHeight = this.calculateMinimumBarHeight(this.getResolution());
            if (calculateMinimumBarHeight > this.barHeight) {
                this.barHeight = calculateMinimumBarHeight;
            }
            this.invalidateSize();
        }
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return (int)this.getActualSize().getWidth();
    }
    
    public int getHeight() {
        return (int)this.getActualSize().getHeight();
    }
    
    public Rectangle getBounds() {
        return this.getBounds(new Rectangle());
    }
    
    public Rectangle getBounds(final Rectangle rectangle) {
        rectangle.setBounds(this.getX(), this.getY(), (int)this.getActualSize().getWidth() + this.getX(), (int)this.getActualSize().getHeight() + this.getY());
        return rectangle;
    }
    
    public Dimension getPreferredSize() {
        return this.getActualSize();
    }
    
    public Dimension getMinimumSize() {
        return this.getActualSize();
    }
    
    public Dimension getMaximumSize() {
        return this.getActualSize();
    }
    
    public Dimension getSize() {
        return this.getActualSize();
    }
    
    public void draw(final Graphics2D graphics2D, final int x, final int y) throws OutputException {
        this.x = x;
        this.y = y;
        this.size = this.draw(new GraphicsOutput(graphics2D, this.font, this.getForeground(), this.getBackground()), x, y, this.barWidth, this.barHeight);
    }
    
    public void output(final Output output) throws OutputException {
        this.draw(output, 0, 0, this.barWidth, this.barHeight);
    }
    
    protected abstract Module[] encodeData();
    
    protected abstract Module calculateChecksum();
    
    protected abstract Module getPreAmble();
    
    protected abstract Module getPostAmble();
    
    protected abstract Dimension draw(final Output p0, final int p1, final int p2, final int p3, final int p4) throws OutputException;
    
    public String getLabel() {
        if (this.label != null) {
            return this.label;
        }
        return this.beautify(this.data);
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    protected int calculateMinimumBarHeight(final int n) {
        return 0;
    }
    
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final Insets insets = this.getInsets();
        try {
            this.draw((Graphics2D)graphics, insets.left, insets.top);
        }
        catch (OutputException ex) {}
    }
    
    protected int getResolution() {
        if (this.resolution > 0) {
            return this.resolution;
        }
        return EnvironmentFactory.getEnvironment().getResolution();
    }
    
    protected int drawModule(final Module module, final Output output, final int n, final int n2, final int n3, final int n4) throws OutputException {
        if (module == null) {
            return 0;
        }
        return module.draw(output, n, n2, n3, n4);
    }
    
    protected String beautify(final String s) {
        final StringBuffer sb = new StringBuffer();
        final StringCharacterIterator stringCharacterIterator = new StringCharacterIterator(s);
        for (char c = stringCharacterIterator.first(); c != '\uffff'; c = stringCharacterIterator.next()) {
            if (Character.isDefined(c) && !Character.isISOControl(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    private void invalidateSize() {
        this.size = null;
    }
    
    private Dimension getActualSize() {
        if (this.size == null) {
            this.size = this.calculateSize();
        }
        return this.size;
    }
    
    private Dimension calculateSize() {
        Dimension dimension = new Dimension();
        if (EnvironmentFactory.getEnvironment() instanceof HeadlessEnvironment) {
            try {
                dimension = this.draw(new SizingOutput(this.font, this.getForeground(), this.getBackground()), 0, 0, this.barWidth, this.barHeight);
            }
            catch (OutputException ex) {}
        }
        else {
            try {
                FontMetrics fontMetrics = null;
                if (this.font != null) {
                    fontMetrics = this.getFontMetrics(this.font);
                }
                dimension = this.draw(new SizingOutput(this.font, fontMetrics, this.getForeground(), this.getBackground()), 0, 0, this.barWidth, this.barHeight);
            }
            catch (OutputException ex2) {}
        }
        return dimension;
    }
    
    public int print(final Graphics graphics, final PageFormat pageFormat, final int n) throws PrinterException {
        if (n >= 1) {
            return 1;
        }
        try {
            this.draw((Graphics2D)graphics, 0, 0);
            return 0;
        }
        catch (OutputException ex) {
            throw new PrinterException(ex.getMessage());
        }
    }
    
    public String toString() {
        return this.getData();
    }
}
