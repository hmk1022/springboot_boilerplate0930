<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<!-- ================== BEGIN PAGE HTML ================== -->
    <ol class="breadcrumb float-xl-end">
        <li class="breadcrumb-item"><a href="javascript:;">Home</a></li>
        <li class="breadcrumb-item active">출고전표</li>
    </ol>

    <h1 class="page-header">출고 <span class="d-block small">자재관리</span></h1>
    <div class="card">
        <div class="card-header">
            <h3 class="mb-0">출고 등록</h3>
        </div>
        <div class="card-body">
   			<form class="form-horizontal form-bordered" data-parsley-validate="true" name="saveForm" id="saveForm" enctype="multipart/form-data">
			<input type="hidden" id="material_bound_no" name="material_bound_no" value="${data.material_bound_no }" >
			<input type="hidden" id="bound_type" name="bound_type" value="02" >
			
            <div class="row">

				<div class="col-md-3 col-12 mt-3">
                    <label class="form-label">출고일<span class="text-danger">*</span></label>
                    <input type="text" class="form-control" 
                    	placeholder="출고일" 
                    	id="bound_date" 
                    	name="bound_date" 
                    	value="${data.bound_date }" 
                    	data-parsley-required="true" 
                    	datepicker
                    	/>
                </div>
				<div class="col-md-3 col-12 mt-3">
                    <label class="form-label">전표ID<span class="text-danger"></span></label>
                    <input type="text" class="form-control" 
                    	placeholder="전표" 
                    	id="material_bound_id" 
                    	readonly
                    	/>
                </div>                
                <div class="col-md-9 col-12 mt-3">
                </div>
                <div class="col-md-6 col-12 mt-3">
                    <label class="form-label">출고위치 <span class="text-danger">*</span></label>
					<tags:code-radio
						codeList="${receive_type }"
						checked="${data.receive_type }"
						name="bound_locate"
						required="true"
						initValue="01"
					/>
                </div>

                <div class="col-md-6 col-12 mt-3">
                    <label class="form-label">출고사유 <span class="text-danger">*</span></label>
					<tags:code-radio
						codeList="${out_type }"
						checked="${data.out_type }"
						name="out_type"
						required="true"
						initValue="01"
					/>
                </div>
                
                <div class="col-md-6 col-12 mt-3" name="out_type_01">
					<div class = "form-group">
						<label class="form-label">작업선택 <span class="text-danger">*</span></label>
						<!-- 						
						<select class="form-select" name="work_no" id="work_no" data-parsley-required="true">
						</select> -->
						 <input class="form-control" 
						 	type="text" 
						 	id="work_name" 
						 	name="work_name" 
						 	value="${data.work_id }${not empty data.material_doc_no?' | ':''}${not empty data.branch_name? data.customer_name.concat(' / ').concat(data.branch_name):data.req_name}${not empty data.material_doc_no?' | ':''}${data.location_name }" 
						 	placeholder="작업일련번호,고객명,작업명을 2자이상 입력하세요." 
						 	data-parsley-required="true" 
						 	data-parsley-error-message="작업명을 입력하세요."
						 	>
						 <input 
						 	type="hidden" 
						 	id="work_no" 
						 	name="work_no" 
						 	value="${data.work_no }" 
						 	data-parsley-required="true" 
						 	data-parsley-error-message="고객(사)명을 입력하세요."
						 	class="d-none"
						 	>						
					</div>
				</div>	
				
				<!-- 매입거래처 추가 -->
				<div class="col-md-3 col-12 mt-3" name="out_type_03" style="display:none">
					<label class="form-label">매입거래처<span class="text-danger" id="span_vendor" >*</span></label>
					<select class="form-select" id="vendor_no" name="vendor_no" data-parsley-error-message="매입거래처를 정확히 입력하세요."
						data-parsley-required="false">
						<option value=''>선택</option>
						<c:forEach items="${vendor }" var="item">
							<option value="${item.vendor_no}"
								data-vendor_hp1="${item.vendor_hp1}"
								data-bank_name="${item.bank_name}"
								data-bank_code="${item.bank_code}" data-acct="${item.acct}"
								data-comp_number="${item.comp_number}"
								${item.vendor_no eq data.vendor_no?" selected":"" }>${item.vendor_name}</option>
						</c:forEach>
							<option value="direct">[기타]</option>
					</select>
				</div>
				<div class="col-md-3 col-12 mt-3" style="display:none" id="div_direct_vendor" name="out_type_03" style="display:none">
                    <label class="form-label">매입거래처(기타)<span class="text-danger" id="span_direct_vendor"></span></label>
                    <input type="text" class="form-control" 
                    	placeholder="매입거래처(직접입력)"
                    	id="direct_vendor"
                    	name="direct_vendor"
                    	value="${data.direct_vendor }" 
                    	data-parsley-error-message="매입거래처를 정확히 입력하세요."
                    	data-parsley-required="false" 
                    	readonly
                   	/>
                </div>
                
                <div class="col-md-12 col-12 mt-3" name="out_type_99_03" style="display:none">
                    <label class="form-label" id="label_etc_remarks">기타사유<span class="text-danger"></span></label>
                    <input type="text" class="form-control" 
                    	placeholder="기타사유"
                    	id="etc_remarks"
                    	name="etc_remarks"
                    	value="${data.etc_remarks }" 
                    	data-parsley-required="false" 
                   	/>
                </div>
                				

				<%--                 
				<div class="col-md-3 col-12 mt-3">
                    <label class="form-label">구매전표<span class="text-danger"></span></label>
                    <input type="text" class="form-control" 
                    	placeholder="구매전표"
                    	id="material_pcs_id"
                    	value="${data.material_pcs_id }" 
                    	data-parsley-required="false" readonly
                   	/>
                </div>
                <div class="col-md-3 col-12 mt-3">
                    <label class="form-label">자재요청서<span class="text-danger"></span></label>
                    <input type="text" class="form-control" 
                    	placeholder="자재요청서"
                    	id="material_doc_id"
                    	value="${data.material_doc_id }" 
                    	data-parsley-required="false" readonly
                   	/>
                </div> 
                --%>
			</div>
            </form>
        </div>
    </div>
			
    <div class="d-flex justify-content-center mt-4">
        <button class="btn btn-gray" name="moveBack">${empty data.material_bound_no ? 'list':'cancel'}</button>
        <button class="btn btn-primary ms-2" id="_dev-save">save</button>
    </div>    
	<!-- end panel -->

	<!-- begin 자재 목록 -->
	<div class="card mt-4" >
		<div class="card-header">
			<div class="d-flex align-item-center justify-content-between">
				<h3 class = "m-0">출고 자재 목록</h3>
				<a href="#" class="btn btn-sm btn-success" id="addMaterial">추가 하기</a>
		 	</div>
		</div>
		<div class="card-body" id = "searchText">
			<!-- <div class="border rounded p-3" id="empty_material_bound_list" style="display:none">등록된 자재가 없습니다. [추가하기] 버튼을 눌러 자재를 추가해보세요.</div> -->
			<table class="table table-striped m-b-0">
			<thead>
				<tr>
					<th>품번</th>
					<th>품명</th>
					<th>브랜드</th>
					<th>수량</th>
					<th>단가</th>
					<th>총금액</th>
					<th width="1%"></th>
				</tr>
			</thead>
			<tbody id="material_bound_item_list">
				<tr>
					<td colspan="7">등록된 자재가 없습니다.</td>
				</tr>
			</tbody>
		</table>
		</div>
	</div>
	

			
<!-- begin modal -->
	<!-- 자재 추가 -->
	<div class="modal fade" id="modal-dialog" >
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">자재 추가하기</h4>
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				</div>
				<div class="modal-body">
					<!--  -->
					<form class="form-horizontal form-bordered" data-parsley-validate="true" name="materialFrm" id="materialFrm" novalidate="">
					
					<!-- <input type="hidden" id="material_no" name="material_no" > -->
					<input type="hidden" name="material_bound_no" value="${data.material_bound_no }">
					
					<input type="hidden" id="req_stat" name="01" >
					
					<!-- <input type="hidden" id="ware_house_cnt" name="ware_house_cnt" > -->
					<!-- <div class="form-row">
					    <div class="col-md-4 mb-3">
					      <label for="cate1">분야</label>
					      <div class="">
					        <select class="form-select" name="cate1" id="cate1" data-parsley-required="true">
					        	<option value="">선택</option>
							</select>
					      </div>
					    </div>
					    <div class="col-md-4 mb-3">
					      <label for="cate2">중분류</label>
					      <div class="">
					        <select class="form-select" name="cate2" id="cate2" data-parsley-required="true">
					        	<option value="">선택</option>
							</select>
					      </div>
					    </div>
					    <div class="col-md-4 mb-3">
					      <label for="cate3">소분류</label>
					      <div class="">
					        <select class="form-select" name="cate3" id="cate3" data-parsley-required="true">
					        	<option value="">선택</option>
							</select>
					      </div>
					    </div>
				  	</div> -->
					
					<!-- <div class="form-row">
					    <div class="col-md-8 mb-3">
					      <label for="material_list">자재선택</label>
					      <div class="validationTooltip01">
					        <select class="form-select" name="material_no" id="material_list" data-parsley-validate="true" data-parsley-required="true">
					        	<option value="">선택</option>
							</select>
					      </div>
					    </div>
				  	</div> -->
				  	<div class="form-row">
					    <div class="col-md-8 mb-3">
					      <label for="material_name_tmp">자재명</label>
					      <input type="text" class="form-control" name="material_name" id="material_name" placeholder="자재명" 
					      	data-parsley-required="true"
					      	data-parsley-error-message="자재를 선택해주세요."
							data-parsley-errors-container="#validation_material_name_error"
					      >
					      <input type="hidden" class="form-control" name="material_no" id="material_no" 
					      	data-parsley-required="true"
					   		data-parsley-error-message="자재를 선택해주세요."
							data-parsley-errors-container="#validation_material_name_error"
					      >
					      <div id="validation_material_name_error"></div>
					    </div>
				  	</div>
				  	<div class="form-row">
					    <div class="col-md-8 mb-3">
					      <label for="material_name_tmp">모델명</label>
					      <input type="text" class="form-control" name="model_name" id="model_name" placeholder="모델명" readonly>
					    </div>
				  	</div>
				  					  		
				  	<div class="form-row" name="div_all_material" >
					    <div class="col-md-6 mb-3">
					      <label for="brand_no">브랜드</label>
					      <div class="validationTooltip01">
					        <select class="form-select" name="brand_no" id="brand_no" disabled>
							</select>
					      </div>
					    </div>
					    <div class="col-md-3 mb-3">
					      <label for="unit_cd">단위</label>
					      <div class="validationTooltip01">
					        <select class="form-select" id="unit_cd" name="unit_cd" disabled>
					        <option value="">선택</option>
					        <tags:code-select
								codeList="${unit_cd }" 
								selected="${data.unit_cd }"
							/>
							</select>
					      </div>
					    </div>
					    <div class="col-md-3 mb-3">
					      <label for="purchased_price">단가</label>
					      <input type="text" class="form-control" id="purchased_price" name="purchased_price" placeholder="단가" data-parsley-required="true" disabled comma>
					    </div>
				  	</div>
					<!-- 자재갯수 -->	
					<div class="form-row">
						<div class="col-md-3 mb-3">
							<label for="house_cnt">물류센터</label>
							<input type="text" class="form-control" id="house_cnt" placeholder="0" comma readonly>
						</div>
						<div class="col-md-3 mb-3">
							<label for="sba_cnt">성수SBA</label>
							<input type="text" class="form-control" id="sba_cnt" placeholder="0" comma readonly>
						</div>
						<div class="col-md-3 mb-3">
							<label for="vehicle_cnt">르노(마포)</label>
							<input type="text" class="form-control" id="vehicle_cnt" placeholder="0" comma readonly>
						</div>
						<div class="col-md-3 mb-3">
						</div>
					</div>
					<!-- 자재갯수 -->	
					<div class="form-row mt-3">
					    <div class="col-md-3 mb-3">
					      <label for="inout_cnt">수량</label>
					      <input type="text" class="form-control" id="inout_cnt" name="inout_cnt" value="0" placeholder="수량" 
					      	min="0" max="10000"
					      	data-parsley-required="true"
					      	data-parsley-min="1"
					      	data-parsley-error-message="수량을 선택해주세요."
							data-parsley-errors-container="#validation_material_cnt_error"
					      >
					      <div id="validation_material_cnt_error"></div>
					    </div>
					    <div class="col-md-3 mb-3">
					      <label for="total_material_price">금액</label>
					      <input type="text" class="form-control" id="total_material_price" name="total_material_price" value="0" placeholder="금액" readonly>
					    </div>
				  	</div>
				  	
					</form>
			
				</div>
				<div class="modal-footer">
					<a href="javascript:;" class="btn btn-white" data-dismiss="modal" id="close_material">닫기</a>
					<a href="javascript:;" class="btn btn-success" id="add_material">저장</a>
				</div>
			</div>
		</div>
	</div>
	
<!-- end modal -->
	

<!-- ================== END PAGE LEVEL JS ================== -->
	
<script>

	$("#inout_cnt").inputSpinner(); // spinner.

	var curPage;
	
	/* init page */
	$(function(){
		
		/* 수정 화면일 경우 처리 */
		if(!Utils.isEmpty($("#material_bound_no").val())){
			$("#work_no").prop("disabled", true);
		} else {
			$("#work_no").prop("disabled", false);
		}
		
		/* 자재삭제 */
		$("#material_bound_item_list").on("click", "#del-material", function (e) {
			e.preventDefault();
			removeMaterialBoundItem($(this).data("material_inout_no"), $(this).data("material_no"));
		});
		
		/* 추가하기 */
		$("#addMaterial").on("click", function (event) {
			
			if(Utils.isEmpty($("#material_bound_no").val())) {
				alert("기본정보 등록 후 제품등록을 추가 하세요.");
				return;
			}
			
			resetForm();	// reset material form 
			$('#modal-dialog').modal({backdrop: 'static', keyboard: false}) ;
			$("#modal-dialog").modal("show");

			/*
			$('#newModal').modal({backdrop: 'static', keyboard: false}) ;
			<button data-target="#newModal" data-toggle="modal" data-backdrop="static" data-keyboard="false">
			새로운 모달
			</button>
			*/
		});
		
		$("#inout_cnt").on("change", function (event) {
		    let inout_cnt = 0;
		    let purchased_price = 0;
		    let total_material_price = 0;
		    
		    inout_cnt = Utils.remove($("#inout_cnt").val(),',') ;
		    purchased_price = Utils.remove($("#purchased_price").val(),',') ;
		    total_material_price = inout_cnt * purchased_price;
		    $("#total_material_price").val(Utils.comma(total_material_price));
		});
		
		/* save data */
		$("#_dev-save").on("click", function(e){
			const _name = "전표등록 저장";	// ajax name
			const _url = "/material/bound/save/ajax";	// ajax url
			const _form = "saveForm";	// form name
			
			/* validate */
			let validate = $("#saveForm").parsley().validate();
			
			if(!validate) {
				console.log('# validate false !!!');
				return;
			}
			/* multi form insert */
			Utils.requestMultiAjax(_name, _url, _form, {
	            success:  function(data) {
		          	alert(Message.SAVE);
		          	console.log("data:", data);
		          	if(Utils.isEmpty($("[name=material_bound_no]").val())){
			          	$("[name=material_bound_no]").val(data.material_bound_no);
			          	$("#material_bound_id").val(data.material_bound_id);
			          	$("#_dev-save").html('edit');
		          	}
		          	// listMaterialBoundItem();
	            },
	            complete:  function(response) {
	            	console.log('complete!!!');
	            }
			});

		});
		
		
		/* set mask optional */
	    setInputMask();
		
	    /* 작업목록 조회 */
//	    listWork();
	    
	    /* 작업선택 */
/* 	    $("#work_no").on("change", function(){
	    	let data = $("#work_no > option:selected").data("item");
			$("#req_name").val(data.req_name);
			$("#director_no").val(data.director_no);
			$("#work_addr").val(data.work_addr1 +" "+ data.work_addr2);
	    }); */
	    
	    /* 자재 추가 */
	    $("#add_material").on("click", function(){
			saveMaterialBoundItem();
	    });	    

	    $("#close_material").on("click", function(){
	    	listMaterialBoundItem();
	    });	    
	    
	    
	    /* 카테고리 조회 */
	    //listCategory("1", "");
	    
	    $("#cate1").on("change", function(){
	    	listCategory("2", $(this).val());
	    	$("#cate3").html('<option value="">선택</option>');
	    	$("#material_list").html('<option value="">선택</option>');
	    });
	    $("#cate2").on("change", function(){
	    	listCategory("3", $(this).val());
	    });

	    /* 자재조회 */
	    $("#cate3").on("change", function(){
	    	listMaterial($(this).val());
	    });
	    
	    /* 자재선택 */
	    $("#material_list").on("change", function(){
				
	    	if($(this).val() == 'ADD'){ // 상품요청.
	    		initMaterialFrom('ADD');	// init form
	    	}
	    	else {	// 상품선택.
		    	let data = $("#material_list > option:selected").data("item");
	    		initMaterialFrom('SELECT', data);	// init form
	    	}
	    });

	    /* 작업선택 */
/* 	    $("#work_no").on("change", function(){
	    	listAdminWork();
	    }); */
	    
	    
	    /* 자재목록 조회 */
	    listMaterialBoundItem();
	    
	    /* 매입거래처 선택 */
	    $("#vendor_no").on("change", function(){
	    	let _data = $(this).find("option:selected").data();
			$("#vendor_hp1").val(_data.vendor_hp1);
			$("#acct_info").val(_data.bank_name + "/"+ _data.acct);
			$("#evidence").val(_data.comp_number);

			$("#bank_code").val(_data.bank_code);
			$("#acct").val(_data.acct);
			
			
			// direct write vendor 
	    	let _value = $(this).val();
			console.log("_value:", _value);
			
			//$("#vendor_no").val('');
			$("#direct_vendor").val('');
			
			if("direct" === _value){
				//$("#div_direct_vendor").show();
				$("#direct_vendor").attr("data-parsley-required", true);
				$("#direct_vendor").attr("readonly", false);
				$("#vendor_no").attr("data-parsley-required", false);
				$("#span_direct_vendor").html('*');
				$("#span_vendor").html('');
				
			} else {
				//$("#div_direct_vendor").hide();
				$("#direct_vendor").attr("data-parsley-required", false);
				$("#direct_vendor").attr("readonly", true);
				$("#vendor_no").attr("data-parsley-required", true);
				$("#span_direct_vendor").html('');
				$("#span_vendor").html('*');
			}
	    });

	    /* 구매사유 선택 */
	    $("[name=bound_reason]").on("change", function(){
	    	$("[name^=bound_reason_div]").hide();
	    	$("[name^=bound_reason_div_"+$(this).val()+"]").show();
	    });

	    /* 부가세 금액 확인 */
	    $("[name=vat_yn]").on("change", function(){
	    	let _vat_yn = $(this).val();
	    	$("#vat_yn_total_price").html();
	    });

	    /* 출고사유 */
	    $("[name=out_type]").on("change", function(){
	    	let _out_type = $("[name=out_type]:checked").val();
			$("[name^=out_type_]").each(function(idx, item){
				let _name = $(this).attr("name");
				
				
				if(_name.indexOf(_out_type) > -1) {
					$(this).show();
					
					console.log("_name_sho:", _name);
				}
				else {
					console.log("_name_hide:", _name);
					$(this).hide();
				}	
			});

			console.log("_out_type:", _out_type);
			
			// 반품추가
			$('#label_etc_remarks').html('기타사유');
			$('#etc_remarks').attr('placeholder', '기타사유');
			$("#vendor_no").val('');
			$("#direct_vendor").val('');
			$("#vendor_no").attr("data-parsley-required", false);
			$("#direct_vendor").attr("data-parsley-required", false);
			
			if(_out_type == '01'){ // 작업시공자재
				$('#work_no').attr('data-parsley-required', 'true');
				$('#work_name').attr('data-parsley-required', 'true');
				$('#out_admin_no').attr('data-parsley-required', 'false');
			}
			else if(_out_type == '02'){ // 워커맨차량재고
				$('#out_admin_no').attr('data-parsley-required', 'true');
				$('#work_no').attr('data-parsley-required', 'false');
				$('#work_name').attr('data-parsley-required', 'false');
			}
			else if(_out_type == '03'){ // 반품
				$('#out_admin_no').attr('data-parsley-required', 'true');
				$('#work_no').attr('data-parsley-required', 'false');
				$('#work_name').attr('data-parsley-required', 'false');
				$('#label_etc_remarks').html('반품사유');
				$('#etc_remarks').attr('placeholder', '반품사유');
				
				$("#vendor_no").attr("data-parsley-required", true);
				$("#direct_vendor").attr("data-parsley-required", false);
				
			}
			else if(_out_type == '99'){ // 기타
				$('#out_admin_no').attr('data-parsley-required', 'false');
				$('#work_no').attr('data-parsley-required', 'false');
				$('#work_name').attr('data-parsley-required', 'false');
			}
			
			$("#saveForm").parsley().reset();
	    });
	    
	    /* 수령방법 trigger */
	    if('${data.bound_reason}' != '') {
		    $("[name='bound_reason']").trigger("change");
		    $("[name='bound_reason']").not(":checked").prop("disabled", true);
		    $("[name=out_type]").trigger("change")
	    }
	    /* 매입거래처 trigger */
	    if('${data.vendor_no}' != '') {
		    $("[name='vendor_no']").trigger("change");
	    }
	    
	    /* 브랜드목록 조회 */
	    listBrand();
	    
	    /* 자재 검색 */
	    $("#material_name").autocomplete({
	        source: function( request, response ) {
	        	let param = {};
	        	param.s_material_name = $("#material_name").val();
                $.ajax({
                    type: 'POST',
                    url: "/material/list/ajax",
                    data: param,
//                    dataType: "json",
//                    data: JSON.stringify(param),
//                    contentType: "application/json",
    		    	beforeSend : function(xhr){
    		    		xhr.setRequestHeader("Authorization", getAuthToken());
    		    	},
                    success: function(data) {
                        response(
                            $.map(data.data, function(item) {    //json[i] 번째 에 있는게 item 임.
                            	let _material_name = item.material_name;
                            		if(item.brand_name != '' && item.brand_name != undefined){
                            			_material_name += "("+item.brand_name+")";
                            		}
                                return {
                                	label: _material_name,    //UI 에서 보여지는 글자, 실제 검색어랑 비교 대상
                                    value: item.material_name,    //사용자 설정값
                                    model_name : item.model_name,
                                    brand_name : item.brand_name,
                                    purchased_price : item.purchased_price,
                                    unit_name : item.unit_name,
                                    unit_cd : item.unit_cd,
                                    brand_no : item.brand_no,
                                    material_no : item.material_no,
                                    house_cnt : item.house_cnt,
                                    sba_cnt : item.sba_cnt,
                                    vehicle_cnt : item.vehicle_cnt
                                }
                            })
                        );
                    },
               });
            },
	        select: function(event, ui) {
	        	console.log("SELECT:", ui.item);
	        	let _form = $("#materialFrm");
	        	let _data = ui.item;
	        	_form.find("[name=brand_no]").val(_data.brand_no);
	        	_form.find("[name=model_name]").val(_data.model_name);
	        	_form.find("[name=unit_cd]").val(_data.unit_cd);
	        	_form.find("[name=purchased_price]").val(Utils.comma(_data.purchased_price));
	        	_form.find("[name=material_no]").val(_data.material_no);
	        	// 자재수량표시.
	        	_form.find("#house_cnt").val(_data.house_cnt);
	        	_form.find("#sba_cnt").val(_data.sba_cnt);
	        	_form.find("#vehicle_cnt").val(_data.vehicle_cnt);
	        },
	        minLength: 2,
	        autoFocus: true,
	        classes: {"ui-autocomplete": "highlight"},
	        delay: 150,
	        focus: function(event, ui) {
	            return false;
	        },
	        close : function(event){
            }
	    });
		
	    
		// 작업선택
	    $("#work_name").autocomplete({
	        source: function( request, response ) {
	        	let param = {};
	        	param.s_work_name = $("#work_name").val();
                $.ajax({
                    type: 'POST',
                    url: "/work/list/auto/ajax",
                    data: param,
    		    	beforeSend : function(xhr){
    		    		xhr.setRequestHeader("Authorization", getAuthToken());
    		    	},
                    success: function(data) {
                        response(
                            $.map(data.data, function(item) {    //json[i] 번째 에 있는게 item 임.
                            	let _work_name = item.work_id;
                            		if(!Utils.isEmpty(item.branch_name)){
                            			_work_name += " | "+item.customer_name+" / "+item.branch_name+" | "+Utils.nvl(item.location_name);
                            		} else {
                            			_work_name += " | "+item.req_name+" | "+Utils.nvl(item.location_name);
                            		}
                                return {
                                	label: _work_name,    //UI 에서 보여지는 글자, 실제 검색어랑 비교 대상
                                    value: _work_name,    //사용자 설정값
                                    work_no : item.work_no,
                                    work_id : item.work_id,
                                    location_name : item.location_name,
                                    req_name : item.req_name,
                                    customer_name : item.customer_name,
                                    branch_name : item.branch_name,
                                    work_addr1 : item.work_addr1,
                                    work_addr2 : item.work_addr2
                                }
                            })
                        );
                    },
               });
            },
	        select: function(event, ui) {
	        	console.log("SELECT:", ui.item);
	        	let _data = ui.item;
	        	$("#work_no").val(_data.work_no);
	        	$("#req_name").val(_data.req_name);
	        	$("#work_addr").val(_data.work_addr1 +" "+ _data.work_addr2);
	        	listAdminWork();	// 담당자명.
	        },
	        minLength: 2,
	        autoFocus: true,
	        classes: {"ui-autocomplete": "highlight"},
	        delay: 150,
	        focus: function(event, ui) {
	            return false;
	        },
	        close : function(event){
            }
	    });
		
		// init form
		/* 	    
		$("#material_name").on("change", function(e){
	    	let _form = $("#materialFrm");
        	_form.find("[name=brand_no]").val('');
        	_form.find("[name=model_name]").val('');
        	_form.find("[name=unit_cd]").val('');
        	_form.find("[name=purchased_price]").val('');
        	_form.find("[name=material_no]").val('');
        	_form.find("[name=total_material_price]").val('');
	    });	    
		*/ 
	});
	
	// 자재 선택에 따른 form init
	function initMaterialFrom(str, data){
			
		if(Utils.isEmpty(data)) {
			resetMaterialFrom();
			return;
		}
	
		$("#material_name").val(data.material_name);
		$("#brand_no").val(data.brand_no);
		$("#unit_cd").val(data.unit_cd);
		$("#purchased_price").val(data.purchased_price);
		
		// 상품수
		let inout_cnt = 0;
		if($("[name='receive_type']:checked").val() =='01'){
			inout_cnt = data.ware_house_cnt;
		} else if($("[name='receive_type']:checked").val() =='02'){
			inout_cnt = data.company_cnt;
		} else if($("[name='receive_type']:checked").val() =='03'){
			inout_cnt = data.sba_cnt_cnt;
		} else {
			inout_cnt = 0;
		}
		
		$("#inout_cnt").val(inout_cnt);
		
		$("#inout_cnt").trigger("change");
	}

	function resetMaterialFrom(){
		$("#material_name").val('');
		$("#brand_no").val('');
		$("#unit_cd").val('');
		$("#purchased_price").val('');
		$("#inout_cnt").val('');
	}
	
	/* 작업 목록 */
	function listWork(){

		const _name = "작업목록 조회";	// ajax name
		const _url = "/work/list/ajax";	// ajax url
		
		/* multi form insert */
		Utils.requestAjax(_name, _url, {}, {
            success:  function(data) {
	          	
	          	if(data != undefined){
	          		let work_list = '<option value="">작업 선택</option>';
	          		$.each(data.data, function(index, item){
	          			//console.log("item >>>> ", (item));
	          			
	          			let temp = {};
	          			//TODO : 데이터 없음.
	          			//temp['manager_admin_no'] = item.manager_admin_no;
	          			temp['director_no'] = '104';
	          			temp['work_addr1'] 	= item.work_addr1;
	          			temp['work_addr2'] 	= item.work_addr2;
	          			temp['work_zip'] 	= item.work_zip;
	          			temp['req_name'] 	= item.req_name;
	          			// B2B 기업구분
	          			let _req_name = item.req_name;
	          			if(item.work_target == '04'){
	          				_req_name = item.customer_name+"/"+item.branch_name;
	          				temp['req_name'] 	= _req_name;
	          			}
	          			work_list += '<option value="'+item.work_no+'" data-item=\''+JSON.stringify(temp)+'\' >'+item.work_id+' | '+_req_name+' | '+item.location_name+'</option>';
	          		});
	          		$("#work_no").html(work_list);
	          	}
	          	/* selected work_no */
	    		if('${data.work_no}' != '') {
	    			$("#work_no").val('${data.work_no}');
	    		}
	          	
	    		listAdminWork();
            },
            complete:  function(response) {
            	console.log('complete!!!');
            }
		});
	}
	
	/* 작업자 목록 */
	function listAdminWork(){

		const _name = "작업자목록 조회";	// ajax name
		const _url = "/work/admin/list/ajax";	// ajax url
		
		let _param = {};
		_param.work_no = $("#work_no").val();
		
		/* multi form insert */
		Utils.requestAjax(_name, _url, _param, {
            success:  function(data) {
	          	
	          	if(data != undefined){
	          		let work_list = '<option value="">선택</option>';
	          		$.each(data.data, function(index, item){
	          			let temp = {};
	          			work_list += '<option value="'+item.admin_no+'" >'+item.admin_name+'</option>';
	          		});
	          		$("#manager_no").html(work_list);
	          	}
	          	/* selected work_no */
	    		if('${data.manager_no}' != '') {
	    			$("#manager_no").val('${data.manager_no}');
	    		}
            },
            complete:  function(response) {
            	console.log('complete!!!');
            }
		});
	}
	
	/* 카테고리 목록 */
	function listCategory(category_level, p_category_id){

		const _name = "카테고리조회 조회";	// ajax name
		const _url = "/material/category/list/ajax";	// ajax url
		let _param = {};	// ajax param
		
		_param.category_level = category_level;
		_param.p_category_id = p_category_id;
		
		/* multi form insert */
		Utils.requestAjax(_name, _url, _param, {
            success:  function(data) {
	          	
	          	if(data != undefined){
	          		let temp_list = '<option value="">선택</option>';
	          		$.each(data.data, function(index, item){
	          			temp_list += '<option value="'+ item.category_id +'" data-category_no="'+ item.category_no +'" >'+item.name+ '</option>';
	          		});
	          		
	          		$("#cate"+category_level).html(temp_list);
	          	}
            },
            complete:  function(response) {
            	console.log('complete!!!');
            }
		});
	}
	
	/* 자재 목록 */
	function listMaterial(){

		const _name = "자재목록 조회";	// ajax name
		const _url = "/material/select/list/ajax";	// ajax url
		let _param = {};	// ajax param
		
		_param['category_no'] = $("#cate3 option:selected").data('category_no');
		
		/* multi form insert */
		Utils.requestAjax(_name, _url, _param, {
            success:  function(data) {
	          	
	          	if(data != undefined){
	          		let temp_list = '<option value="">선택</option>';
	          		$.each(data.data, function(index, item){
	          			let temp = {};
	          			// add brand_name
	          			let _materila_name = item.material_name;
	          			if(!Utils.isEmpty(item.brand_name)){
	          				_materila_name += '(' + item.brand_name +')';
	          			}
	          			temp['material_no'] = item.material_no;
	          			temp['material_name'] = item.material_name;
	          			temp['brand_no'] = item.brand_no;
	          			temp['unit_cd'] = item.unit_cd;
	          			temp['ware_house_cnt'] = item.ware_house_cnt;
	          			temp['company_cnt'] = item.company_cnt;
	          			temp['sba_cnt'] = item.sba_cnt;
	          			temp['purchased_price'] 	= item.purchased_price;
	          			temp_list += '<option value="'+item.material_no+'" data-item=\''+JSON.stringify(temp)+'\' >'+ _materila_name +'</option>';
	          			//temp_list += '<option value="'+ item.material_no +'" >'+item.material_name+ '</option>';
	          		});
	          		
	          		// temp_list += '<option value="ADD">[상품추가]</option>';
	          		
	          		$("#material_list").html(temp_list);
	          	}
            },
            complete:  function(response) {
            	console.log('complete!!!');
            }
		});
	}
	
	function resetForm(){
		$('#materialFrm')[0].reset();
		$("#cate2").html('<option value="">선택</option>');
		$("#cate3").html('<option value="">선택</option>');
	}

	function saveMaterialBoundItem(){
		const _name = "자재구매요청 저장";	// ajax name
		const _url = "/material/bound/inout/save/ajax";	// ajax url
		
		
		//$("#materialFrm #material_bound_no").val($("#material_bound_no").val());
		
		//_param['material_bound_no'] = $("#material_bound_no").val();
		
		/* validate */
		let validate = $("#materialFrm").parsley().validate();
		
		if(!validate) {
			console.log('# validate false !!!');
			return;
		}
		
		let _param = {};	// ajax param
		_param['material_bound_no'] = $("#material_bound_no").val();
		_param['material_no'] = $("#material_no").val();
		_param['inout_cnt'] = $("#inout_cnt").val();
		
		
		/* multi form insert */
		Utils.requestAjax(_name, _url, _param, {
            success:  function(data) {
	          	alert(Message.SAVE);
	          	//resetForm();	// reset material form
            },
            complete:  function(response) {
            	console.log('complete!!!');
            }
		});
	}
	
	
	function listMaterialBoundItem(){
		
		if(Utils.isEmpty($("#material_bound_no").val())) return ;
		
		const _name = "자재구매요청 조회";	// ajax name
		const _url = "/material/bound/inout/list/ajax";	// ajax url
		let _param = {};	// ajax param
		_param['material_bound_no'] = $("#material_bound_no").val();
		
		Utils.requestAjax(_name, _url, _param, {
            success:  function(data) {
	          	
	          	let tmp = '';
	          	if(Utils.isEmpty(data.data)){
	          		//tmp = '<tr><td colspan="7">등록된 데이터가 없습니다.</td></tr>';
	          		$("#empty_material_bound_list").show();
	          	} else {
	          		let _total_purchased_price = 0;
	          		$.each(data.data, function(idx, item){
	          			tmp += '<tr>'+ 
	    				'<td>'+Utils.nvl(item.material_id)+'</td>'+
	    				'<td style="text-align:left">'+Utils.nvl(item.material_name)+'</td>'+
	    				'<td>'+Utils.nvl(item.brand_name)+'</td>'+
	    				'<td>'+Utils.nvl(item.inout_cnt)+" "+getCodeNm('unit_cd',item.unit_cd)+'</td>'+
	    				'<td>'+Utils.comma(Utils.nvl(item.purchased_price))+'</td>'+
	    				'<td>'+Utils.comma(Utils.nvl(item.total_purchased_price))+'</td>'+
	    				'<td class="with-btn" nowrap=""><a href="#" class="btn btn-sm btn-white width-60" id="del-material" data-material_no="'+item.material_no+'" data-material_inout_no="'+item.material_inout_no+'">삭제</a></td>'+
	    				'</tr>';
	          			_total_purchased_price += item.total_purchased_price;
	          		});
	          		
	          		tmp +='<tr class="table-success"><td colspan="5" class="fw-bold">소계</td><td colspan="2" class="text-end fw-bold">'+Utils.comma(_total_purchased_price)+' 원</td></tr>';
	          	}
	          	
	          	$("#empty_material_bound_list").hide();
    			$("#material_bound_item_list").html(tmp);
            },
            complete:  function(response) {
            	console.log('complete!!!');
            }
		});
	}



	function removeMaterialBoundItem(material_inout_no, material_no){
		if(!confirm(Message.CONFIRM.DELETE)){
			return;
		}
		const _name = "자재구매요청 삭제";	// ajax name
		const _url = "/material/bound/inout/delete/ajax";	// ajax url
		let _param = {};	// ajax param
		
		_param['material_inout_no'] = material_inout_no;
		_param['bound_type'] = "01";	// 입고
		_param['material_no'] = material_no;	// material_no
		
		/* multi form insert */
		Utils.requestAjax(_name, _url, _param, {
            success:  function(data) {
	          	//alert(Message.DELETE);
	          	listMaterialBoundItem();
            },
            complete:  function(response) {
            	console.log('complete!!!');
            }
		});
	}
	
	function saveMaterial(){
		const _name = "자재마스터 저장";	// ajax name
		const _url = "/material/save/ajax";	// ajax url
		let _param = $("#materialFrm").serializeObject();	// ajax param
		
		_param['work_no'] = $("#work_no").val();
		_param['use_date'] = Utils.remove($("#use_date").val(), "-");
		_param['use_hh'] = $("#use_hh").val();
		_param['use_mm'] = $("#use_mm").val();
		_param['receive_type'] = $("[name=receive_type]:checked").val();
		_param['material_bound_no'] = $("#material_bound_no").val();
		
		/* validate */
		let validate1 = $("[name=saveForm]").parsley().validate();
		let validate2 = $("[name=materialFrm]").parsley().validate();
		
		if(!validate1 || !validate2) {
			console.log('# validate false !!!');
			return;
		}
		
		/* multi form insert */
		Utils.requestAjax(_name, _url, _param, {
            success:  function(data) {
	          	alert(Message.SAVE);
	          	resetForm();	// reset material form
	          	listMaterialBoundItem();
            },
            complete:  function(response) {
            	console.log('complete!!!');
            }
		});
	}
	
	/* 브랜드 목록 */
	function listBrand(){

		const _name = "브랜드목록 조회";	// ajax name
		const _url = "/brand/list/ajax";	// ajax url
		let _param = {};	// ajax param
		
		/* multi form insert */
		Utils.requestAjax(_name, _url, _param, {
            success:  function(data) {
	          	
	          	if(data != undefined){
	          		let temp_list = '<option value="">선택</option>';
	          		$.each(data.data, function(index, item){
	          			temp_list += '<option value="'+ item.brand_no +'" >'+item.brand_name+ '</option>';
	          		});
	          		
	          		$("#brand_no").html(temp_list);
	          	}
            },
            complete:  function(response) {
            	console.log('complete!!!');
            }
		});
	}
</script>