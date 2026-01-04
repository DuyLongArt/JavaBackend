package backend.DataLayer.protocol.Url;

import backend.DataLayer.protocol.TypeGetSet;
import jakarta.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;

@Entity
@Table(name = "urls")
public class UrlEntity implements UrlStructure
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "url_id")
    private Integer urlID;

    @Column(name = "url_value", nullable = false, length = 2048)
    private String urlValue;

    @Column(name = "host", length = 255)
    private String host;

    @Column(name = "path", length = 1024)
    private String path;

    @Column(name = "protocol", length = 10)
    private String protocol;

    @Column(name = "port")
    private Integer port;

    // Default constructor for JPA
    public UrlEntity() {}

    // Constructor with URL parsing
    public UrlEntity(String urlValue) {
        this.setUrlValue(urlValue);
    }

    @Override
    public int getId() {
        return urlID != null ? urlID : 0;
    }

    public void setId(Integer urlID) {
        this.urlID = urlID;
    }

    public Integer getUrlID() {
        return urlID;
    }

    public void setUrlID(Integer urlID) {
        this.urlID = urlID;
    }

    @Override
    public String getUrlValue() {
        return urlValue;
    }

    @Override
    public void setUrlValue(String urlValue) {
        this.urlValue = urlValue;
        // Automatically parse URL components when setting the URL value
        parseUrlComponents();
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
        // Rebuild URL when host changes
        rebuildUrl();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
        // Rebuild URL when path changes
        rebuildUrl();
    }

    // Additional getters and setters for completeness
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
        rebuildUrl();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
        rebuildUrl();
    }

    /**
     * Parse URL components from the urlValue
     */
    private void parseUrlComponents() {
        if (urlValue == null || urlValue.trim().isEmpty()) {
            return;
        }

        try {
            URL url = new URL(urlValue);
            this.host = url.getHost();
            this.path = url.getPath();
            this.protocol = url.getProtocol();
            this.port = url.getPort() != -1 ? url.getPort() : null;
        } catch (MalformedURLException e) {
            // If URL is malformed, try to extract basic components
            parseBasicComponents();
        }
    }

    /**
     * Basic URL parsing for malformed URLs
     */
    private void parseBasicComponents() {
        if (urlValue == null) {
            return;
        }

        if (urlValue.contains("://")) {
            String[] protocolSplit = urlValue.split("://", 2);
            this.protocol = protocolSplit[0];

            if (protocolSplit.length > 1) {
                String remainder = protocolSplit[1];

                // Handle port in host
                String hostPart;
                if (remainder.contains("/")) {
                    String[] hostPath = remainder.split("/", 2);
                    hostPart = hostPath[0];
                    this.path = "/" + hostPath[1];
                } else {
                    hostPart = remainder;
                    this.path = "/";
                }

                // Extract port if present
                if (hostPart.contains(":")) {
                    String[] hostPort = hostPart.split(":", 2);
                    this.host = hostPort[0];
                    try {
                        this.port = Integer.parseInt(hostPort[1]);
                    } catch (NumberFormatException e) {
                        this.port = null;
                    }
                } else {
                    this.host = hostPart;
                    this.port = null;
                }
            }
        }
    }

    /**
     * Rebuild URL from components
     */
    private void rebuildUrl() {
        if (protocol != null && host != null) {
            StringBuilder url = new StringBuilder();
            url.append(protocol).append("://").append(host);

            if (port != null && port > 0) {
                url.append(":").append(port);
            }

            if (path != null && !path.isEmpty()) {
                if (!path.startsWith("/")) {
                    url.append("/");
                }
                url.append(path);
            } else {
                url.append("/");
            }

            this.urlValue = url.toString();
        }
    }

    /**
     * Validate if the URL is well-formed
     */
    public boolean isValidUrl() {
        if (urlValue == null || urlValue.trim().isEmpty()) {
            return false;
        }

        try {
            new URL(urlValue);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    /**
     * Get the domain from host (removes subdomain)
     */
    public String getDomain() {
        if (host == null) return null;

        String[] parts = host.split("\\.");
        if (parts.length >= 2) {
            return parts[parts.length - 2] + "." + parts[parts.length - 1];
        }
        return host;
    }

    /**
     * Get base URL (protocol + host + port)
     */
    public String getBaseUrl() {
        if (protocol == null || host == null) return null;

        StringBuilder baseUrl = new StringBuilder();
        baseUrl.append(protocol).append("://").append(host);

        if (port != null && port > 0) {
            baseUrl.append(":").append(port);
        }

        return baseUrl.toString();
    }

    @Override
    public String toString() {
        return "UrlEntity{" +
                "urlID=" + urlID +
                ", urlValue='" + urlValue + '\'' +
                ", host='" + host + '\'' +
                ", path='" + path + '\'' +
                ", protocol='" + protocol + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UrlEntity urlEntity = (UrlEntity) obj;
        return urlID != null ? urlID.equals(urlEntity.urlID) : urlEntity.urlID == null;
    }

    @Override
    public int hashCode() {
        return urlID != null ? urlID.hashCode() : 0;
    }
}