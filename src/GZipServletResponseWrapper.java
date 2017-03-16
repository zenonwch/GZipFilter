import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class GZipServletResponseWrapper extends HttpServletResponseWrapper {
    private GZipServletOutputStream gzipOS;
    private PrintWriter printWriter;

    public GZipServletResponseWrapper(final HttpServletResponse response)
            throws IOException {
        super(response);
    }

    public void close() throws IOException {
        //PrintWriter.close does not throw exceptions.
        //Hence no try-catch block.
        if (printWriter != null) {
            printWriter.close();
        }

        if (gzipOS != null) {
            gzipOS.close();
        }
    }

    /**
     * Flush OutputStream or PrintWriter
     *
     * @throws IOException
     */
    @Override
    public void flushBuffer() throws IOException {
        //PrintWriter.flush() doesn't throw exception
        if (printWriter != null) {
            printWriter.flush();
        }

        IOException exception1 = null;
        try {
            if (gzipOS != null) {
                gzipOS.flush();
            }
        } catch (final IOException e) {
            exception1 = e;
        }

        IOException exception2 = null;
        try {
            super.flushBuffer();
        } catch (final IOException e) {
            exception2 = e;
        }

        if (exception1 != null) throw exception1;
        if (exception2 != null) throw exception2;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (printWriter != null) {
            throw new IllegalStateException("PrintWriter obtained already - cannot get OutputStream");
        }
        if (gzipOS == null) {
            final OutputStream os = getResponse().getOutputStream();
            gzipOS = new GZipServletOutputStream(os);
        }
        return gzipOS;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (printWriter == null && gzipOS != null) {
            throw new IllegalStateException("OutputStream obtained already - cannot get PrintWriter");
        }
        if (printWriter == null) {
            final OutputStream os = getResponse().getOutputStream();
            final String charEncoding = getResponse().getCharacterEncoding();
            gzipOS = new GZipServletOutputStream(os);
            printWriter = new PrintWriter(new OutputStreamWriter(gzipOS, charEncoding));
        }
        return printWriter;
    }

    @Override
    public void setContentLength(final int len) {
        //ignore, since content length of zipped content
        //does not match content length of unzipped content.
    }
}
