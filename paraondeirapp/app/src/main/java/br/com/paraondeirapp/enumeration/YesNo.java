package br.com.paraondeirapp.enumeration;

public enum YesNo {
    S("Sim"),
    N("Não");

    private String label;

    YesNo(String label) {
        this.label = label;
    }

    public String getLabel(){
        return this.label;
    }
}
