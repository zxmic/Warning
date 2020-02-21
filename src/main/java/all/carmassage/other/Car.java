package all.carmassage.other;

public class Car {
    private String 类型;
    private String 颜色;
    private String 型号;
    private String cph;
    private String 乘员数;
    private String 制造年月;
    private String 品牌;

    public Car() {
    }

    public Car(String 类型, String 颜色, String 型号, String cph, String 乘员数, String 制造年月, String 品牌){
        this.类型=类型;
        this.颜色=颜色;
        this.型号=型号;
        this.cph =cph;
        this.乘员数=乘员数;
        this.制造年月=制造年月;
        this.类型=品牌;

    }


    public String get类型() {
        return 类型;
    }

    public void set类型(String 类型) {
        this.类型 = 类型;
    }

    public String get颜色() {
        return 颜色;
    }

    public void set颜色(String 颜色) {
        this.颜色 = 颜色;
    }

    public String get型号() {
        return 型号;
    }

    public void set型号(String 型号) {
        this.型号 = 型号;
    }

    public String getCph() {
        return cph;
    }

    public void setCph(String cph) {
        this.cph = cph;
    }

    public String get乘员数() {
        return 乘员数;
    }

    public void set乘员数(String 乘员数) {
        this.乘员数 = 乘员数;
    }

    public String get制造年月() {
        return 制造年月;
    }

    public void set制造年月(String 制造年月) {
        this.制造年月 = 制造年月;
    }

    public String get品牌() {
        return 品牌;
    }

    public void set品牌(String 品牌) {
        this.品牌 = 品牌;
    }
}
