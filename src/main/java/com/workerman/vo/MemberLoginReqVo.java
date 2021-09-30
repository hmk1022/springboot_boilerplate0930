package com.workerman.vo;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MemberLoginReqVo {

	@ApiModelProperty(value = "회원아이디", required = true, allowEmptyValue = false) @NotNull private String member_id;
	@ApiModelProperty(value = "비밀번호,SNS가입시는 필요없음.", required = false, allowEmptyValue = true) private String password;
	@ApiModelProperty(value = "가입유형 (01:자체가입,02:페이스북,03:카카오,04:네이버)", required = true, allowEmptyValue = false) @NotNull private String join_type;

}
