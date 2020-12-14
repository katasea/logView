package cn.katasea.bean.po;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserInfo {

    @NotNull
    private String mindexId;

    private String userName;

    private String userSex;

    private String userBirthday;

    private String idType;

    private String idNo;

    private String mobilePhone;

    private String telephone;

    private String nation;

    private String citizen;

    private String workUnit;

    private String address;

    private String status;

    private String crtDatetime;

    private String crtAppId;

    private String crtTermId;
}
