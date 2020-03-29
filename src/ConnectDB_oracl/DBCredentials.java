package ConnectDB_oracl;

public final class DBCredentials {

    private String url;
    private String password;
    private String user;
    private int numberN;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getNumberN() { return numberN; }

    public void setNumberN(int numberN) throws NumberNNotFitException {
        if(numberN>0) this.numberN = numberN;
        else throw new NumberNNotFitException("number n does not match");
    }
}
