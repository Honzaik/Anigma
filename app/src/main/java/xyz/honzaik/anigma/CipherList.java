package xyz.honzaik.anigma;

public enum CipherList {
    AES("AES-128", true), TRIPLEDES("3-DES", true), AES256("AES-256", true);

    private final String name;
    private final boolean hasIV;

    CipherList(String s, boolean hasIV){
        this.name = s;
        this.hasIV = hasIV;
    }

    public String getName(){
        return this.name;
    }

    public boolean hasIV(){
        return this.hasIV;
    }

}
