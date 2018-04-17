package com.cn.saic.scm.ai.dochub.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cn.saic.scm.ai.dochub.domain.Doc;
import com.cn.saic.scm.ai.dochub.repository.DocRepository;




@Service
public class DocService {
	
	@Autowired
	private DocRepository docRepository;
	
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	MongoTemplate myMongoTemplateWrite;	
	
	/**
	 * Create a document in the doc-Hub
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public Doc createDoc(MultipartFile file, String userName) throws IOException, Exception{
		
		String docId=null;
		Doc doc = null;
		
		
		
		docId = fileService.createFile(file.getOriginalFilename(), file.getInputStream(),file.getContentType());
		doc = new Doc();
		doc.setId(docId);
		doc.setFileType(file.getContentType());
		doc.setFileName(file.getOriginalFilename());
		//doc.setFileName(new String(file.getOriginalFilename().getBytes("ISO-8859-1"),"UTF-8"));
		doc.setFileSize(file.getSize());
		doc.setCreatedBy((userName==null||userName.trim().equals(""))?"unknown":userName);
		doc.setDateCreated(new Date());
		
		return docRepository.save(doc);
	}

	/**
	 * Delete a specific document
	 * @param docId
	 */
	public void deleteDoc(String docId)
	{
		fileService.deleteFile(docId);
		docRepository.delete(docRepository.findOneById(docId).get());
	}
	
	/**
	 * Get the Doc metaData based on the docId
	 * @param docId
	 * @return
	 */
	public Doc getDocMetadata(String docId)
	{
		return docRepository.findOneById(docId).get();
	}
	
	public List<Doc> getDocMetadataListByKeyword(String keyword, int page, int pageSize)
	{
		Sort tempSort = new Sort(Sort.Direction.DESC,"dateCreated");
		Pageable pagingAndSortRequest = new PageRequest(page, pageSize,tempSort);
		return docRepository.searchDoc(keyword, pagingAndSortRequest);
	}
	
	public List<Doc> getDocMetadataList(int page, int pageSize) throws Exception
	{
		Sort tempSort = new Sort(Sort.Direction.DESC,"dateCreated");
		Pageable pagingAndSortRequest = new PageRequest(page, pageSize,tempSort);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		return docRepository.listAllDoc(sdf.parse("1900-01-01 00:00:00"),pagingAndSortRequest);
	}
	
	public long count(){		
		return docRepository.count();
	}
}
