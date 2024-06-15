package org.example;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class Request {

    private final String method;
    private final String fullPath;
    private  String path;

    private List<NameValuePair> pairs;

    public Request(String requestMethod, String requestPath) {
        this.method = requestMethod;
        this.fullPath = requestPath;
        List<NameValuePair> pairs = URLEncodedUtils.parse(this.fullPath, StandardCharsets.UTF_8);

    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return fullPath;
    }

    public String getQueryParam(String name)
    {
        for(int i = 0; i < pairs.size(); i++)
        {
            if (name.equals(pairs.get(i).getName()))
                return pairs.get(i).getValue();
        }
        return "";
    }

    public List<NameValuePair> getQueryParam()
    {
        return pairs;
    }

}
