import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GZipFilter implements Filter {
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(final ServletRequest servletRequest,
                         final ServletResponse servletResponse,
                         final FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        if (acceptsGZipEncoding(httpRequest)) {
            httpResponse.addHeader("Content-Encoding", "gzip");
            final GZipServletResponseWrapper gzipResponse = new GZipServletResponseWrapper(httpResponse);
            filterChain.doFilter(servletRequest, gzipResponse);
            gzipResponse.close();
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    private boolean acceptsGZipEncoding(final HttpServletRequest request) {
        final String acceptEncoding = request.getHeader("Accept-Encoding");

        return acceptEncoding != null && acceptEncoding.contains("gzip");
    }
}
