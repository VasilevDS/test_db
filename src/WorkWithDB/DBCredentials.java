package WorkWithDB;

public final class DBCredentials {

    private String url;
    private String password;
    private String user;


    public DBCredentials(String url, String user, String password) {
        this.url = url;
        this.password = password;
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }
}
