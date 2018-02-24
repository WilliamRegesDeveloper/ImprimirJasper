package net.sourceforge.barbecue;

import javax.servlet.ServletOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import net.sourceforge.barbecue.linear.code39.Code39Barcode;
import net.sourceforge.barbecue.output.OutputException;
import java.io.IOException;
import net.sourceforge.barbecue.env.EnvironmentFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;

public class BarcodeServlet extends HttpServlet
{
    static /* synthetic */ Class class$net$sourceforge$barbecue$BarcodeFactory;
    
    public String getServletName() {
        return "barbecue";
    }
    
    public void doPost(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException {
        this.doRequest(httpServletRequest, httpServletResponse);
    }
    
    public void doGet(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException {
        this.doRequest(httpServletRequest, httpServletResponse);
    }
    
    private void doRequest(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) throws ServletException {
        final String requiredParameter = this.getRequiredParameter(httpServletRequest, "data");
        final String parameter = this.getParameter(httpServletRequest, "type");
        final String parameter2 = this.getParameter(httpServletRequest, "appid");
        final Integer parameterAsInteger = this.getParameterAsInteger(httpServletRequest, "width");
        final Integer parameterAsInteger2 = this.getParameterAsInteger(httpServletRequest, "height");
        final Integer parameterAsInteger3 = this.getParameterAsInteger(httpServletRequest, "resolution");
        final boolean parameterAsBoolean = this.getParameterAsBoolean(httpServletRequest, "checksum", false);
        final boolean parameterAsBoolean2 = this.getParameterAsBoolean(httpServletRequest, "headless", true);
        boolean parameterAsBoolean3 = false;
        if (parameterAsBoolean2) {
            EnvironmentFactory.setHeadlessMode();
        }
        else {
            parameterAsBoolean3 = this.getParameterAsBoolean(httpServletRequest, "drawText", false);
        }
        final Barcode barcode = this.getBarcode(parameter, requiredParameter, parameter2, parameterAsBoolean);
        barcode.setDrawingText(parameterAsBoolean3);
        if (parameterAsInteger != null) {
            barcode.setBarWidth(parameterAsInteger);
        }
        if (parameterAsInteger2 != null) {
            barcode.setBarHeight(parameterAsInteger2);
        }
        if (parameterAsInteger3 != null) {
            barcode.setResolution(parameterAsInteger3);
        }
        try {
            this.outputBarcodeImage(httpServletResponse, barcode);
        }
        catch (IOException rootCause) {
            throw new ServletException("Could not output barcode", rootCause);
        }
        catch (OutputException rootCause2) {
            throw new ServletException("Could not output barcode", rootCause2);
        }
    }
    
    private String getRequiredParameter(final HttpServletRequest httpServletRequest, final String s) throws ServletException {
        final String parameter = this.getParameter(httpServletRequest, s);
        if (parameter == null) {
            throw new ServletException("Parameter " + s + " is required");
        }
        return parameter;
    }
    
    private boolean getParameterAsBoolean(final HttpServletRequest httpServletRequest, final String s, final boolean b) {
        final String parameter = this.getParameter(httpServletRequest, s);
        if (parameter == null) {
            return b;
        }
        return Boolean.valueOf(parameter);
    }
    
    private Integer getParameterAsInteger(final HttpServletRequest httpServletRequest, final String s) {
        final String parameter = this.getParameter(httpServletRequest, s);
        if (parameter == null) {
            return null;
        }
        return new Integer(parameter);
    }
    
    private String getParameter(final HttpServletRequest httpServletRequest, final String s) {
        return httpServletRequest.getParameter(s);
    }
    
    protected Barcode getBarcode(final String s, final String s2, final String s3, final boolean b) throws ServletException {
        Label_0030: {
            if (s != null) {
                if (s.length() != 0) {
                    break Label_0030;
                }
            }
            try {
                return BarcodeFactory.createCode128B(s2);
            }
            catch (BarcodeException rootCause) {
                throw new ServletException("BARCODE ERROR", rootCause);
            }
        }
        if (this.isType(s, new String[] { "UCC128" })) {
            if (s3 == null) {
                throw new ServletException("UCC128 barcode type requires the appid parameter");
            }
            try {
                return BarcodeFactory.createUCC128(s3, s2);
            }
            catch (BarcodeException rootCause2) {
                throw new ServletException("BARCODE ERROR", rootCause2);
            }
        }
        if (this.isType(s, Code39Barcode.TYPES)) {
            try {
                return BarcodeFactory.createCode39(s2, b);
            }
            catch (BarcodeException rootCause3) {
                throw new ServletException("BARCODE ERROR", rootCause3);
            }
        }
        try {
            return (Barcode)this.getMethod(s).invoke(null, s2);
        }
        catch (NoSuchMethodException ex) {
            throw new ServletException("Invalid barcode type: " + s);
        }
        catch (SecurityException ex2) {
            throw new ServletException("Could not create barcode of type: " + s + "; Security exception accessing BarcodeFactory");
        }
        catch (IllegalAccessException ex3) {
            throw new ServletException("Could not create barcode of type: " + s + "; Illegal access to BarcodeFactory");
        }
        catch (InvocationTargetException ex4) {
            throw new ServletException("Could not create barcode of type: " + s + "; Could not invoke BarcodeFactory");
        }
    }
    
    private boolean isType(final String s, final String[] array) {
        for (int i = 0; i < array.length; ++i) {
            if (s.equalsIgnoreCase(array[i])) {
                return true;
            }
        }
        return false;
    }
    
    private Method getMethod(final String s) throws NoSuchMethodException {
        final Method[] methods = ((BarcodeServlet.class$net$sourceforge$barbecue$BarcodeFactory == null) ? (BarcodeServlet.class$net$sourceforge$barbecue$BarcodeFactory = class$("net.sourceforge.barbecue.BarcodeFactory")) : BarcodeServlet.class$net$sourceforge$barbecue$BarcodeFactory).getMethods();
        for (int i = 0; i < methods.length; ++i) {
            final Method method = methods[i];
            if (method.getParameterTypes().length == 1 && this.matches(method, s)) {
                return method;
            }
        }
        throw new NoSuchMethodException();
    }
    
    private boolean matches(final Method method, final String s) {
        return method.getName().startsWith("create") && method.getName().substring(6).equalsIgnoreCase(s);
    }
    
    private void outputBarcodeImage(final HttpServletResponse httpServletResponse, final Barcode barcode) throws IOException, OutputException {
        httpServletResponse.setContentType("image/png");
        final ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        BarcodeImageHandler.writePNG(barcode, outputStream);
        outputStream.flush();
        outputStream.close();
    }
    
    static /* synthetic */ Class class$(final String s) {
        try {
            return Class.forName(s);
        }
        catch (ClassNotFoundException ex) {
            throw new NoClassDefFoundError(ex.getMessage());
        }
    }
}
