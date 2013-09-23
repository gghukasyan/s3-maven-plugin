package com.garegin.s3.maven.plugin.info;

import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: gareginghukasyan
 * Date: 9/23/13
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileUploadInfo {

	private String key;
	private File inputDirectory;
	private File file;
	private CannedAccessControlList permission;

	public FileUploadInfo(
		String key, File inputDirectory,
		CannedAccessControlList permission
	){
		this.key = key;
		this.inputDirectory = inputDirectory;
		this.file = new File(inputDirectory.getAbsolutePath() + "/" +  key);
		this.permission = permission;
	}

	public FileUploadInfo(
		String key, File file, File inputDirectory
	){
		this.key = key;
		this.inputDirectory = inputDirectory;
		this.file = file;
		this.permission = permission;
	}

	public String getKey(){ return key; }
	public File getInputDirectory(){ return inputDirectory; }
	public File getFile(){ return file; }
	public CannedAccessControlList getPermission(){ return permission; }
}
