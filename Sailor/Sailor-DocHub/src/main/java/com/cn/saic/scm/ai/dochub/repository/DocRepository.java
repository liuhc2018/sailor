package com.cn.saic.scm.ai.dochub.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cn.saic.scm.ai.dochub.domain.Doc;



public interface DocRepository extends PagingAndSortingRepository<Doc, String> {
	
	Optional<Doc> findOneById(String id);	
	
	@Query("{$or:[{'fileName':{$regex:?0}},{'fileType':{$regex:?0}},{'id':{$regex:?0}},{'createdBy':{$regex:?0}}]}")
	List<Doc> searchDoc(String keyword, Pageable paramPageable);
	
	@Query("{'dateCreated':{$gte:?0}}")
	List<Doc> listAllDoc(Date tempDate,Pageable paramPageable);
	
	long count();
}
