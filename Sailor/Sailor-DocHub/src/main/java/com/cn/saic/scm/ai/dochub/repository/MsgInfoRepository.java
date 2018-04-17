package com.cn.saic.scm.ai.dochub.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cn.saic.scm.ai.dochub.domain.Doc;
import com.cn.saic.scm.ai.dochub.domain.MessageInfo;

public interface MsgInfoRepository extends PagingAndSortingRepository<MessageInfo, String>{
	public MessageInfo findById(String Id);
}