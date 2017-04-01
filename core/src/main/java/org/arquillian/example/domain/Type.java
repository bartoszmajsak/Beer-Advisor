package org.arquillian.example.domain;

public enum Type {

    LAGER("Lager"),
    PALE_ALE("Pale Ale"),
    BLOND_ALE("Blond Ale"),
    QUADRUPEL_IPA("Quadrupel India Pale Ale"),
    IPA("India Pale Ale"),
    AMBER("Amber"),
    VIENNA("Vienna"),
    SCHWARZBIER("Schwarzbier"),
    RUSSIAN_IMPERIAL_STOUT("Russian Imperial Stout");

    private final String label;

    private Type(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
