package logic.sensitive;

import annotation.SensitiveParam;

public class SensitiveObject {

    @SensitiveParam
    private String Phone;

    @SensitiveParam(process = false)
    private String noPhone;

    @SensitiveParam(rule = ObjectRule.class)
    private Test test;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getNoPhone() {
        return noPhone;
    }

    public void setNoPhone(String noPhone) {
        this.noPhone = noPhone;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
