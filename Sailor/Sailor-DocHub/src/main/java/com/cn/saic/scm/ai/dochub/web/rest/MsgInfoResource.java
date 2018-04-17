package com.cn.saic.scm.ai.dochub.web.rest;

import java.net.URISyntaxException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cn.saic.scm.ai.dochub.domain.MessageInfo;
import com.cn.saic.scm.ai.dochub.repository.MsgInfoRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api/ai/dochub/msgInfo")
@Api(value = "/api/ai/dochub/msgInfo", description = "消息相关的Apis")
public class MsgInfoResource {
	@Autowired
	private MsgInfoRepository msgInfoRepository;
	
	@RequestMapping(value = "test",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "创建一个测试")
	public ResponseEntity<?> test(
			@RequestBody HashMap<String,Object> m) throws URISyntaxException, Exception {
		MessageInfo msgInfo=new MessageInfo();
		msgInfo.setTitle("testTile");
		msgInfo.setMsgType("testType");
		msgInfo.setData(m);
		MessageInfo msgResult=msgInfoRepository.save(msgInfo);
		return ResponseEntity.ok().body(msgInfoRepository.findById(msgResult.getId()));
	}
}
