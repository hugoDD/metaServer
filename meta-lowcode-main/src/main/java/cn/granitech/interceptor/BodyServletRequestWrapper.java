package cn.granitech.interceptor;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class BodyServletRequestWrapper extends HttpServletRequestWrapper {
    private byte[] requestBody = null;

    public BodyServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.requestBody = StreamUtils.copyToByteArray(request.getInputStream());
    }

    public byte[] getRequestBody() {
        return this.requestBody;
    }

    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.requestBody);
        return new ServletInputStream() {
            public boolean isFinished() {
                return false;
            }

            public boolean isReady() {
                return false;
            }

            public void setReadListener(ReadListener readListener) {
            }

            public int read() {
                return byteArrayInputStream.read();
            }
        };
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
