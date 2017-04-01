package org.arquillian.example.domain;

import java.math.BigDecimal;

public class BeerBuilder {
    String name;
    BigDecimal price;
    BigDecimal alcohol;
    String code;
    Type type;
    Brewery brewery;

    public static BeerBuilder.NameBuilder create() {
        return new BeerBuilder().new NameBuilder();
    }

    public class NameBuilder {
        public PriceBuilder named(String name) {
            BeerBuilder.this.name = name;
            return new PriceBuilder();
        }
    }

    public class PriceBuilder {
        public AlcoholBuilder withPrice(BigDecimal price) {
            BeerBuilder.this.price = price;
            return new AlcoholBuilder();
        }
    }

    public class AlcoholBuilder {
        public BreweryBuilder havingAlcohol(BigDecimal alcohol) {
            BeerBuilder.this.alcohol = alcohol;
            return new BreweryBuilder();
        }
    }

    public class BreweryBuilder {
        public TypeBuilder from(Brewery brewery) {
            BeerBuilder.this.brewery = brewery;
            return new TypeBuilder();
        }
    }

    public class TypeBuilder {
        public CodeBuilder ofType(Type type) {
            BeerBuilder.this.type = type;
            return new CodeBuilder();
        }
    }

    public class CodeBuilder {
        public InstanceCreator withCode(String code) {
            BeerBuilder.this.code = code;
            return new InstanceCreator();
        }
    }

    public class InstanceCreator {
        public Beer build() {
            return new Beer(BeerBuilder.this.name, BeerBuilder.this.brewery,
                BeerBuilder.this.type, BeerBuilder.this.price,
                BeerBuilder.this.alcohol, BeerBuilder.this.code);
        }
    }
}