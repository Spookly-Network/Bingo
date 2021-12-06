package de.nehlen.bingo.data.helper;

import lombok.Getter;
import lombok.Setter;

public class PickList {

    @Getter @Setter private Boolean it0 = false;
    @Getter @Setter private Boolean it1 = false;
    @Getter @Setter private Boolean it2 = false;
    @Getter @Setter private Boolean it3 = false;
    @Getter @Setter private Boolean it4 = false;
    @Getter @Setter private Boolean it5 = false;
    @Getter @Setter private Boolean it6 = false;
    @Getter @Setter private Boolean it7 = false;
    @Getter @Setter private Boolean it8 = false;

    public PickList() {
        this.it0 = false;
        this.it1 = false;
        this.it2 = false;
        this.it3 = false;
        this.it4 = false;
        this.it5 = false;
        this.it6 = false;
        this.it7 = false;
        this.it8 = false;
    }

    public Boolean check() {
        if(it1 && it2 && it3 && it4 && it5 && it6 && it7 && it8 && it0) {
            return true;
        }

        return false;
    }

}
