package com.zj.model;

public enum Lang {
    ZH_HANS("zh-hans"),
    ZH_HANT("zh-hant"),
    EN("en"),
    DE("de"),
    ES("es"),
    FR("fr"),
    IT("it"),
    JA("ja"),
    KO("ko"),
    RU("ru"),
    HI("hi"),
    TH("th"),
    AR("ar"),
    PT("pt"),
    BN("bn"),
    MS("ms"),
    NL("nl"),
    EL("el"),
    LA("la"),
    SV("sv"),
    ID("id"),
    PL("pl"),
    TR("tr"),
    CS("cs"),
    ET("et"),
    VI("vi"),
    FIL("fil"),
    FI("fi"),
    HE("he"),
    IS("is"),
    NB("nb");

    private String code;

    private Lang(String var3) {
        this.code = var3;
    }

    public String getCode() {
        return this.code;
    }
}
