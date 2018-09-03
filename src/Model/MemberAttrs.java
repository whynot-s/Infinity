package Model;


import org.bson.Document;

public enum MemberAttrs {

    MEMBERID(0,"INT","memberId"),
    NAME("","STR","name"),
    GENDER("","STR","gender"),
    RANK(0,"INT","rank"),
    POINT(0.0,"DOU","point"),
    STATUS(0,"INT","status"),
    CHOSE(0,"INT","chose"),
    CHOSENBY(0,"INT","chosenBy"),
    COURTID(0,"INT","courtId");

    private String type;
    private int defaultINT;
    private double defaultDOU;
    private String defaultSTR;
    private String field;


    MemberAttrs(int deafult_int, String type, String field){
        this.defaultINT = deafult_int;
        this.type = type;
        this.field = field;
    }

    MemberAttrs(double default_dou, String type, String field){
        this.defaultDOU = default_dou;
        this.type = type;
        this.field = field;
    }

    MemberAttrs(String default_str, String type, String field){
        this.defaultSTR = default_str;
        this.type = type;
        this.field = field;
    }

    private Object getDefaultValue(){
        if(type == "INT") return defaultINT;
        else if(type == "DOU") return defaultDOU;
        else if(type == "STR") return defaultSTR;
        else return null;
    }

    public String getField() { return field; }

    public static Document defaultDoc(){
        Document document = new Document();
        for(MemberAttrs attr : MemberAttrs.values()){
            if(attr.equals(MemberAttrs.RANK)) continue;
            document.put(attr.field, attr.getDefaultValue());
        }
        return document;
    }

}
