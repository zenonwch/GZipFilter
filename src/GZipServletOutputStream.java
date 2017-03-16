import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class GZipServletOutputStream extends ServletOutputStream {
    private final GZIPOutputStream gzipOS;

    public GZipServletOutputStream(final OutputStream os) throws IOException {
        gzipOS = new GZIPOutputStream(os);
    }

    @Override
    public void flush() throws IOException {
        gzipOS.flush();
    }

    @Override
    public void write(final byte[] b) throws IOException {
        gzipOS.write(b);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        gzipOS.write(b, off, len);
    }

    @Override
    public void write(final int b) throws IOException {
        gzipOS.write(b);
    }

    @Override
    public void close() throws IOException {
        gzipOS.close();
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(final WriteListener writeListener) {

    }
}
