package tech.xinong.xnsm.pro.base.model;

import java.math.BigDecimal;

/**
 * Created by xiao on 2017/1/17.
 */

public enum WeightUnit implements Unit{

    JIN("斤",1) {
        public BigDecimal toJin(BigDecimal weight) {
            return weight;
        }
        public BigDecimal toKg(BigDecimal weight) {
            return weight.divide(BigDecimal.valueOf(KG.getConversionFactor()));
        }
        public BigDecimal toTon(BigDecimal weight) {
            return weight.divide(BigDecimal.valueOf(TON.getConversionFactor()));
        }
        public BigDecimal convert(BigDecimal srcWeight, WeightUnit srcUnit) {
            return srcUnit.toJin(srcWeight);
        }
        @Override
        public String getName() {
            return this.name();
        }
    },
    KG("公斤",2) {
        public BigDecimal toJin(BigDecimal weight) {
            return weight.multiply(BigDecimal.valueOf(KG.getConversionFactor()));
        }
        public BigDecimal toKg(BigDecimal weight) {
            return weight;
        }
        public  BigDecimal toTon(BigDecimal weight) {
            return weight.multiply(BigDecimal.valueOf(KG.getConversionFactor()))
                    .divide(BigDecimal.valueOf(TON.getConversionFactor()));
        }
        public BigDecimal convert(BigDecimal srcWeight, WeightUnit srcUnit) {
            return srcUnit.toKg(srcWeight);
        }
        @Override
        public String getName() {
            return this.name();
        }
    },
    TON("吨",2000) {
        public BigDecimal toJin(BigDecimal weight) {
            return weight.multiply(BigDecimal.valueOf(TON.getConversionFactor()));
        }
        public BigDecimal toKg(BigDecimal weight) {
            return weight.divide(BigDecimal.valueOf(TON.getConversionFactor()/KG.getConversionFactor()));
        }
        public  BigDecimal toTon(BigDecimal weight) {
            return weight;
        }
        public BigDecimal convert(BigDecimal srcWeight, WeightUnit srcUnit) {
            return srcUnit.toTon(srcWeight);
        }
        @Override
        public String getName() {
            return this.name();
        }
    };

    private String displayName;
    private int conversionFactor;//换算因子

    private WeightUnit(String displayName, int conversionFactor) {
        this.displayName = displayName;
        this.conversionFactor = conversionFactor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getConversionFactor() {
        return conversionFactor;
    }

    public BigDecimal toJin(BigDecimal weight) {
        throw new AbstractMethodError();
    }

    public BigDecimal toKg(BigDecimal weight) {
        throw new AbstractMethodError();
    }

    public BigDecimal toTon(BigDecimal weight) {
        throw new AbstractMethodError();
    }

    public BigDecimal convert(BigDecimal srcWeight, WeightUnit srcUnit) {
        throw new AbstractMethodError();
    }
}
