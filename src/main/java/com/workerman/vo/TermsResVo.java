package com.workerman.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString
public class TermsResVo extends ResultVo{

	@ApiModelProperty(value = "리턴결과") private TermsVo result_data;
	
}
