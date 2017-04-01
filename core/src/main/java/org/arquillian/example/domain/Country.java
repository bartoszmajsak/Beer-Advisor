package org.arquillian.example.domain;

public enum Country {
    UNKNOWN,
    POLAND,
    BELGIUM,
    SCOTLAND,
    SWITZERLAND,
    SWEDEN,
    NORWAY;

    public String getLabel() {
        return this.name().toLowerCase();
    }
}
