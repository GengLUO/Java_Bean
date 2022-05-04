package be.kuleuven.javabean.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class CoffeeOrder implements Parcelable {
    private final int MAX_NUMBER_OF_COFFEES = 5;
    private String name;
    private String coffee;
    private boolean sugar;
    private boolean whipCream;
    private int quantity;

    public CoffeeOrder(String name, String coffee, boolean sugar, boolean whipCream, int quantity) {
        this.name = name;
        this.coffee = coffee;
        this.sugar = sugar;
        this.whipCream = whipCream;
        this.quantity = Math.min(quantity, MAX_NUMBER_OF_COFFEES);
    }

    //在经过修改更新以后，以下我们需要提供一种新的基于parcel的constructor
    public CoffeeOrder(Parcel in) {
//        name = in.readString();
//        coffee = in.readString();
//        sugar = in.readByte() != 0;
//        whipCream = in.readByte() != 0;
//        quantity = in.readInt();
//  以上是一种通过新赋值的方法，但更方便的是直接调用this()
        this(
                in.readString(),
                in.readString(),
                in.readByte()!=0,
                in.readByte()!=0,
                in.readInt()
        );
    }

    //再次，需要引入第三种方法，即通过Json
    public CoffeeOrder(JSONObject o) {
        try {
            //这里就不可能用this()了，因为JSON不张那样
            name = o.getString("customer");
            coffee = o.getString("coffee");
            String toppings = o.getString("toppings");
            sugar = toppings.contains("sugar");
            whipCream = toppings.contains("cream");
            quantity = Math.min(o.getInt("quantity") , MAX_NUMBER_OF_COFFEES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<CoffeeOrder> CREATOR = new Creator<CoffeeOrder>() {//该方法是该接口必须需要的，不用做任何修改
        @Override
        public CoffeeOrder createFromParcel(Parcel in) {
            return new CoffeeOrder(in);
        }

        @Override
        public CoffeeOrder[] newArray(int size) {
            return new CoffeeOrder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(coffee);
        parcel.writeByte(sugar ? (byte) 1 : (byte) 0);
//因为我们用的api不支持writeBoolean，另一种写法是：writeString(Boolean.valueOf(sugar).toString());
        parcel.writeByte(whipCream ? (byte) 1 : (byte) 0);
        parcel.writeInt(quantity);
    }

    public String getRequestURL(){
        StringBuilder sb = new StringBuilder(name);
        sb.append("/" + coffee);
        sb.append("/" + getToppingsURL());
        sb.append("/" + quantity);
        return sb.toString();
    }
    //给上面的方法下一个辅助
    private String getToppingsURL(){
        String toppings = "-";
        if(hasToppings())
            return (sugar ? "+sugar" : "") + (whipCream ? "+cream" : "");
        else
            return "-";
    }
    private boolean hasToppings(){
        return sugar || whipCream;
    }
}
