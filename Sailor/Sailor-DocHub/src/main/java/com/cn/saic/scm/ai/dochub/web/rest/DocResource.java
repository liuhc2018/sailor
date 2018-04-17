package com.cn.saic.scm.ai.dochub.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cn.saic.scm.ai.dochub.domain.Doc;
import com.cn.saic.scm.ai.dochub.domain.MessageInfo;
import com.cn.saic.scm.ai.dochub.repository.MsgInfoRepository;
import com.cn.saic.scm.ai.dochub.service.DocService;
import com.cn.saic.scm.ai.dochub.service.FileService;
import com.cn.saic.scm.ai.dochub.web.rest.util.HeaderUtil;
import com.mongodb.gridfs.GridFSFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("/api/ai/dochub/docs")
@Api(value = "/api/ai/dochub/docs", description = "文件对象存储相关的Apis")
public class DocResource {
	
private final Logger log = LoggerFactory.getLogger(DocResource.class);
		
	@Autowired
	private FileService fileService;
	
	@Autowired
	private DocService docService;
	
	
	
	/**
	 * Update a single Doc
	 * @param file
	 * @return
	 * @throws URISyntaxException
	 * @throws Exception
	 */
	@RequestMapping(value="/single",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "上传一个文件")
	public ResponseEntity<Doc> createDoc(
			@RequestHeader("Authorization") String Authorization,	
			@RequestParam("file") MultipartFile file
		)throws URISyntaxException, Exception{
		
			
		log.debug("REST request to upload single doc...");
		
		Doc doc = docService.createDoc(file,null);
		return ResponseEntity.created(new URI("/api/docs/"+doc.getId()))
                .headers(HeaderUtil.createAlert( "docs.created", doc.getId()))
                .body(doc); 
	}	
	
	/**
	 * Upload one or more Docs
	 * @param files
	 * @return
	 * @throws URISyntaxException
	 * @throws Exception
	 */
	@RequestMapping(value = "/multiple",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "上传多个文件")
	public ResponseEntity<List<Doc>> createOneOrMultipleDocs(			
			@RequestHeader("Authorization") String Authorization,	
			@RequestParam("file") List<MultipartFile> files
		)throws URISyntaxException, Exception{
		
		log.debug("REST request to upload multiple docs ...");
		
		List<Doc> docResultList = new ArrayList<Doc>();
		for (MultipartFile file : files) {
			docResultList.add(docService.createDoc(file,null));
		}		
		return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert( "docs.created", String.valueOf(files.size())+" files"))
                .body(docResultList);
	}	
	
	/**
	 * Delete a Doc
	 * @param docId
	 * @return
	 * @throws URISyntaxException
	 * @throws Exception
	 */
	@RequestMapping(value = "/{docId}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "删除某个文件")
	public ResponseEntity<?> delelteDoc(			
			@RequestHeader("Authorization") String Authorization,	
			@PathVariable String docId
		)throws URISyntaxException, Exception{	
			
		log.debug("REST request to delete single doc ...");
		
		
		docService.deleteDoc(docId);
		return ResponseEntity.ok().body("Deletion done..");
	}

	/**
	 * Update a single Doc
	 * @param docId
	 * @param file
	 * @return
	 * @throws URISyntaxException
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/{docId}",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "更新一个文件")
	public ResponseEntity<Doc> updateDoc(			
			@RequestHeader("Authorization") String Authorization,	
			@PathVariable String docId,
			@RequestParam("file") MultipartFile file
		)throws URISyntaxException, Exception{
		
		log.debug("REST request to update single doc...");
		
		
		//Delete the file Id
		docService.deleteDoc(docId);		
		//Save the file uploaded
		Doc doc = docService.createDoc(file,null);
		
		return ResponseEntity.ok()
			.body(doc);
	}
	
	/**
	 * Get a specific Doc
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/{docId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })	
	@ApiOperation(value = "显示或者获取某个文件")
	public ResponseEntity<?> getDoc(
			@PathVariable String docId) throws Exception{
			
		log.debug("REST request to get single doc...");
		
		GridFSFile tempGridFSFile = fileService.getFile(docId);		
		return ResponseEntity.ok().header("Cache-Control", "max-age=315360000")
				.contentLength(tempGridFSFile.getLength())
				.contentType(MediaType.parseMediaType(tempGridFSFile.getContentType()))
				.body(new InputStreamResource(fileService.getFile(docId).getInputStream()));
	}
	
	/**
	 * Download a specific file
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/download/{docId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })	
	@ApiOperation(value = "Force to download a specific Doc")
	public ResponseEntity<?> downloadDoc(
			@PathVariable String docId) throws Exception{
			
		log.debug("REST request to get single doc ...");
		
		GridFSFile tempGridFSFile = fileService.getFile(docId);		
		return ResponseEntity.ok().header("Cache-Control", "max-age=315360000")
				.header("Content-Disposition", "attachment;filename="+tempGridFSFile.getFilename())
				.contentLength(tempGridFSFile.getLength())
				.contentType(MediaType.parseMediaType(tempGridFSFile.getContentType()))
				.body(new InputStreamResource(fileService.getFile(docId).getInputStream()));
	}

	/**
	 * Get metadata of a specific Doc
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/metadatas/{docId}",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })	
	@ApiOperation(value = "获取某个文件的相关信息")
	public ResponseEntity<Doc> getDocMetadata(			
			@RequestHeader("Authorization") String Authorization,	
			@PathVariable String docId) throws Exception{
		
		log.debug("REST request to get single doc metadata ...");
		
		return ResponseEntity.ok().body(docService.getDocMetadata(docId));
	}
	
	/**
	 * Count the total docs in docHub
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/count",method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })	
	@ApiOperation(value = "统计文件总数")
	public ResponseEntity<?> count(			
			@RequestHeader("Authorization") String Authorization
			) throws Exception{
		
	
		log.debug("REST request to get single doc metadata...");
		return ResponseEntity.ok().body(docService.count());
	}
	
	/**
	 * Search metadata of all Docs by Keyword
	 * @param docId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/search",params={"keyword","page","pageSize"},method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })	
	@ApiOperation(value = "基于某个关键字的文件查询与分页列表显示")
	public ResponseEntity<List<Doc>> searchDocMetadataByKeyword(			
			@RequestHeader("Authorization") String Authorization,	
			@RequestParam(value="keyword",required=false) String keyword,
			@RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize) throws Exception{
		
		log.debug("REST request to search docs by keyword with pagination...");
		
		return ResponseEntity.ok().body(docService.getDocMetadataListByKeyword(keyword, page, pageSize));
	}
	
	/**
	 * List all metadata of Docs
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/metadatas",params={"page","pageSize"},method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })	
	@ApiOperation(value = "分页列出所有文件")
	public ResponseEntity<List<Doc>> getDocMetadataList(			
			@RequestHeader("Authorization") String Authorization,	
			@RequestParam("page") int page,
			@RequestParam("pageSize") int pageSize) throws Exception{
		
		log.debug("REST request to list all docs with pagination ...");
		
		return ResponseEntity.ok().body(docService.getDocMetadataList(page, pageSize));
	}
}
