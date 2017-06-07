package com.elong.nb.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.elong.nb.common.biglog.Constants;
import com.elong.nb.common.gson.DateTypeAdapter;
import com.elong.nb.common.gson.GsonUtil;
import com.elong.nb.common.model.ErrorCode;
import com.elong.nb.common.model.RestRequest;
import com.elong.nb.common.model.RestResponse;
import com.elong.nb.common.util.ValidateUtil;
import com.elong.nb.model.bookingdata.BookingDataCondition;
import com.elong.nb.model.bookingdata.BookingDataResult;
import com.elong.nb.model.rateplan.RatePlanCondition;
import com.elong.nb.model.rateplan.RatePlanResult;
import com.elong.nb.service.IBookingDataService;
import com.elong.nb.service.IRatePlansService;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

@Controller
public class HotelDataController {
	@Resource
	private IRatePlansService ratePlansService;
	@Resource
	private IBookingDataService bookingDataService;

	// 获取产品
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/Hotel/GetRatePlans", method = RequestMethod.POST)
	public ResponseEntity<byte[]> getRatePlans(HttpServletRequest request)
			throws IOException {
		// 基本校验
		Map<Class, TypeAdapter> m = new HashMap<Class, TypeAdapter>();
		RestRequest<RatePlanCondition> restRequest = GsonUtil.toReq(request,
				RatePlanCondition.class, m);
		String rst = validateRatePlanRequest(restRequest);
		if (StringUtils.isNotBlank(rst)) {
			RestResponse<RatePlanResult> response = new RestResponse<RatePlanResult>(
					restRequest.getGuid());
			response.setCode(rst);
			return new ResponseEntity<byte[]>(GsonUtil.toJson(
					response,
					restRequest.getVersion() == null ? 0d : restRequest
							.getVersion()).getBytes(), HttpStatus.OK);
		}

		// 调用Service
		RestResponse<RatePlanResult> response = null;
		response = ratePlansService.GetRatePlans(restRequest);
		// 反回JSON
		return new ResponseEntity<byte[]>(GsonUtil.toJson(response,
				restRequest.getVersion()).getBytes(), HttpStatus.OK);

	}

	private String validateRatePlanRequest(
			RestRequest<RatePlanCondition> restRequest) {
		StringBuffer sb = new StringBuffer(
				ValidateUtil.validateRestRequest(restRequest));
		RatePlanCondition req = restRequest.getRequest();
		/**
		 * 付款类型不能为空
		 */
		/**
		 * 代理不传的时候默认为All 不需要判断空
		 */
//		if (req.getPaymentType() == null || req.getPaymentType().getValue() < 0) {
//			sb.append(ErrorCode.Common_PaymentTypeRequired);
//			return sb.toString();
//		}
		if (StringUtils.isBlank(req.getHotelIds())) {
			sb.append(ErrorCode.Common_HotelIdRequired);
			return sb.toString();
		}
		if(req.getHotelIds().split(",").length>10){
			sb.append(ErrorCode.Common_NumberIdsFormatErrorAndLessThanTen);
			return sb.toString();
		}
		return sb.toString();
	}

	// 获取GetBookingData
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/Hotel/GetBookingData", method = RequestMethod.POST)
	public ResponseEntity<byte[]> getBookingData(HttpServletRequest request)
			throws IOException {
		// 基本校验
		Map<Class, TypeAdapter> m = new HashMap<Class, TypeAdapter>();
		RestRequest<BookingDataCondition> restRequest = GsonUtil.toReq(request,
				BookingDataCondition.class, m);
		String rst = validateBookingDataRequest(restRequest);
		if (StringUtils.isNotBlank(rst)) {
			RestResponse<BookingDataResult> response = new RestResponse<BookingDataResult>(
					restRequest.getGuid());
			response.setCode(rst);
			return new ResponseEntity<byte[]>(GsonUtil.toJson(
					response,
					restRequest.getVersion() == null ? 0d : restRequest
							.getVersion()).getBytes(), HttpStatus.OK);
		}

		// 调用Service
		RestResponse<BookingDataResult> response = null;
		response = bookingDataService.getBookingData(restRequest);
		// 反回JSON
		return new ResponseEntity<byte[]>(toJson(response,
				restRequest.getVersion()).getBytes(), HttpStatus.OK);

	}
	@SuppressWarnings("rawtypes")
	public static String toJson(RestResponse<BookingDataResult> resp, double version) {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		String realCode="";
		realCode=resp == null||resp.getCode()==null ? "" : resp.getCode().split("\\|")[0];
		if("0".equals(realCode)){
			realCode=resp.getResult().getRealResponseCode();
		}
		ra.setAttribute(Constants.ELONG_RESPONSE_CODE, realCode,
				ServletRequestAttributes.SCOPE_REQUEST);
		// 增加版本对应的输出设置
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
		if (version > 0)
			gsonBuilder.setVersion(version);
		String json = gsonBuilder.create().toJson(resp, RestResponse.class);
		return json;
	}
	private String validateBookingDataRequest(
			RestRequest<BookingDataCondition> restRequest) {
		StringBuffer sb = new StringBuffer(
				ValidateUtil.validateRestRequest(restRequest));
		restRequest.getRequest();
		// 这里加入相关校验
		return sb.toString();
	}
}
