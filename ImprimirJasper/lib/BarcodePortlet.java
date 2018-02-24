package net.sourceforge.barbecue;

import java.io.OutputStream;
import net.sourceforge.barbecue.output.OutputException;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.PortletRequest;
import javax.portlet.ActionResponse;
import javax.portlet.ActionRequest;
import java.io.IOException;
import javax.portlet.PortletException;
import java.io.PrintWriter;
import javax.portlet.RenderResponse;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceServingPortlet;
import javax.portlet.GenericPortlet;

public class BarcodePortlet extends GenericPortlet implements ResourceServingPortlet
{
    private static final String PARAM_BARCODE_DATA = "barcode_data";
    private static final String SESSION_KEY = "barcode_data";
    
    protected String getTitle(final RenderRequest renderRequest) {
        return "Barcode portlet";
    }
    
    protected void doEdit(final RenderRequest renderRequest, final RenderResponse renderResponse) throws PortletException, IOException {
        renderResponse.setContentType("text/html");
        final PrintWriter writer = renderResponse.getWriter();
        writer.println("<form method=\"POST\" action=\"" + renderResponse.createActionURL() + "\">");
        writer.println("Enter string: <input name=\"barcode_data\" type=text size=30></input>");
        writer.println("<input type=submit value=\"Submit\"></input>");
        writer.println("</form>");
    }
    
    protected void doView(final RenderRequest renderRequest, final RenderResponse renderResponse) throws PortletException, IOException {
        renderResponse.setContentType("text/html");
        renderResponse.getWriter().println("<img src=\"" + renderResponse.createResourceURL() + "\" />");
    }
    
    protected void doHelp(final RenderRequest renderRequest, final RenderResponse renderResponse) throws PortletException, IOException {
        renderResponse.setContentType("text/html");
        renderResponse.getWriter().println("<a target=\"_blank\" href=\"http://en.wikipedia.org/wiki/Barcode\">What is a barcode?</a>");
    }
    
    public void processAction(final ActionRequest actionRequest, final ActionResponse actionResponse) throws PortletException, IOException {
        storeDataInSession((PortletRequest)actionRequest);
    }
    
    private static void storeDataInSession(final PortletRequest portletRequest) {
        portletRequest.getPortletSession(true).setAttribute("barcode_data", (Object)portletRequest.getParameter("barcode_data"));
    }
    
    private static Barcode createBarcode(String s) {
        if (s == null) {
            s = "Barcode";
        }
        try {
            return BarcodeFactory.createCode128(s);
        }
        catch (BarcodeException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static Barcode createBarcode(final PortletRequest portletRequest) {
        return createBarcode((String)portletRequest.getPortletSession(true).getAttribute("barcode_data"));
    }
    
    public void serveResource(final ResourceRequest resourceRequest, final ResourceResponse resourceResponse) throws PortletException, IOException {
        Barcode barcode = createBarcode((PortletRequest)resourceRequest);
        if (barcode == null) {
            barcode = createBarcode("Barcode");
        }
        resourceResponse.setContentType("image/png");
        final OutputStream portletOutputStream = resourceResponse.getPortletOutputStream();
        try {
            BarcodeImageHandler.writePNG(barcode, portletOutputStream);
            portletOutputStream.flush();
        }
        catch (OutputException ex) {
            throw new PortletException((Throwable)ex);
        }
    }
}
