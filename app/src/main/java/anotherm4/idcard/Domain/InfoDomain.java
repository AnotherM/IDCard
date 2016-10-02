package anotherm4.idcard.Domain;

/**
 * Created by anotherm4 on 2016/10/2.
 */

public class InfoDomain {
    public Integer ErrorCode;
    public String Reason;
    public String Area;
    public String Sex;
    public String Birthday;

    public InfoDomain(){
        super();
    }
    public InfoDomain(Integer errorCode,String reason,String area,String sex,String birthday){
        this.ErrorCode=errorCode;
        this.Reason=reason;
        this.Area=area;
        this.Sex=sex;
        this.Birthday=birthday;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }
}
