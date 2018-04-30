package xyz.honzaik.anigma;

public class Algorithm {

    public String id;

    public Algorithm(String id){
        this.id = id;
    }

    public boolean hasIV(){
        return !id.contains("ECB");
    }

}
