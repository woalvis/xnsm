package tech.xinong.xnsm.pro.base.model;

/**
 * Created by xiao on 2016/12/7.
 */

public enum UnitQuantity {

    JIN("斤",1),KG("",2),TON("吨",2000);
    private String name;
    private int sum;
    UnitQuantity(String name,int sum){
        this.name = name;
        this.sum = sum;
    }
    public static UnitQuantity getByName(String name){
        UnitQuantity[] enumArr =  UnitQuantity.values();
        for(UnitQuantity unitQuantity:enumArr){
            if(unitQuantity.toString().equals(name)){
                return unitQuantity;
            }
        }
        return null;
    }
    public String getName(){
        return name;
    }
    public int getSum(){
        return sum;
    }
}
