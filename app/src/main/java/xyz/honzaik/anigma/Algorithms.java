package xyz.honzaik.anigma;

public enum Algorithms {
    AES("AES-128", true), RSA("RSA", false), TRIPLEDES("3-DES", true);

    private final String name;
    private final boolean hasIV;

    Algorithms(String s, boolean hasIV){
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
