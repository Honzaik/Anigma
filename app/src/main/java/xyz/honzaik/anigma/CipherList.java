package xyz.honzaik.anigma;

/**
 * Enum obsahující seznam používaných šifer.
 */
public enum CipherList {
    AES("AES-128", true), TRIPLEDES("3-DES", true), AES256("AES-256", true);

    private final String name;
    private final boolean hasIV;

    /**
     * Konstruktor
     * @param name Jméno šifry, které se bude zobrazovat v seznamu
     * @param hasIV Používá tato šifra inicializační vektor (šifra se bere společně s šifrovacím módem
     */
    CipherList(String name, boolean hasIV){
        this.name = name;
        this.hasIV = hasIV;
    }

    /**
     * Vratí jméno šifry
     * @return
     */
    public String getName(){
        return this.name;
    }

    /**
     * Vratí zda-li šifra používá IV
     * @return
     */
    public boolean hasIV(){
        return this.hasIV;
    }

}
