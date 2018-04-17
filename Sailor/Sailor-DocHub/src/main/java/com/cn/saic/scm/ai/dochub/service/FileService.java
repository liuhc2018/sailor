package com.cn.saic.scm.ai.dochub.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class FileService {
	
	@Autowired
	GridFsTemplate gridFsTemplate;
	
	public String createFile(String filename, InputStream fileInputStream, String fileContentType) throws Exception  {
		DBObject metaData = new BasicDBObject();
		String newFileId = gridFsTemplate.store(fileInputStream, filename , fileContentType, metaData).getId().toString();
		return newFileId;
	}
	
	public GridFSDBFile getFile(String fileId) {
		return this.gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
	}

	public void deleteFile(String fileId) {
		gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
	}

	public String updateFile(String fileId, String filename, InputStream fileInputStream, String fileContentType)throws Exception {		
		//Delete the imageID from GridFS
		gridFsTemplate.delete(new Query(Criteria.where("_id").is(fileId)));
		//Store into mongoDB GridFS
		DBObject metaData = new BasicDBObject();
		String newFileId = gridFsTemplate.store(fileInputStream, filename , fileContentType, metaData).getId().toString();
		return newFileId;
	}

	public String createFile(String filename, InputStream fileInputStream, String fileContentType, DBObject metadata)	throws Exception {		
		String newFileId = gridFsTemplate.store(fileInputStream, filename , fileContentType, metadata).getId().toString();
		return newFileId;
	}
}
