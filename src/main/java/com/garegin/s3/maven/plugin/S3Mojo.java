package com.garegin.s3.maven.plugin;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.garegin.s3.maven.plugin.info.FileUploadInfo;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.Iterator;
import java.util.List;


/**
 * @prefix s3-maven
 * @requiresProject true
 * @requiresOnline true
 * @goal upload
 * @phase prepare-package
 * @description Uploads static resources to a AWS S3 Bucket
 *
 */
public class S3Mojo extends AbstractMojo {

	/**
	 * @parameter property="key"
	 * @required
	 */
	private String key;

	/**
	 * @parameter property="secret"
	 * @required
	 */
	private String secret;

	/**
	 * @parameter property="bucket"
	 * @required
	 */
	private String bucket;

	/**
	 * @parameter property="permissions" default-value="PublicRead"
	 */
	private String permissions;

	/**
	 * @parameter property="includes"
	 * @required
	 */
	private List<String> includes;

	/**
	 * @parameter property="excludes"
	 */
	private List<String> excludes;

	/**
	 * @parameter property="inputDirectory"
	 * @required
	 */
	private File inputDirectory;

	private static final MimetypesFileTypeMap mimeMap = new MimetypesFileTypeMap();

	@Override
	public void execute()
		throws MojoExecutionException, MojoFailureException {
		getLog().info("Started S3 Upload");
		getLog().info("Bucket " + bucket);
		getLog().info("Input Directory " + inputDirectory.getAbsolutePath());
		getLog().info("Includes " + includes.size());
		getLog().info("Excludes " + excludes.size());
		try {
			AmazonS3Client client = connect();
			List<String> fileNames = FileUtils.getFileNames(
				inputDirectory, convertToString(includes), convertToString(excludes), false, true
			);
			getLog().info("Processing total " + fileNames.size());
			FileUploadInfo info = null;
			CannedAccessControlList perm = CannedAccessControlList.valueOf(permissions);
			for (String fileName: fileNames) {
				info = new FileUploadInfo(fileName, inputDirectory, perm);
				processFile(client, info);
			}
		}catch (Throwable e){
			getLog().error(e);
		}
	}

	private AmazonS3Client connect(){
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(key, secret);
		AmazonS3Client client = new AmazonS3Client(awsCredentials);
		return client;
	}

	private void processFile(
		AmazonS3Client client, FileUploadInfo info
	) throws MojoExecutionException {
		getLog().info("Uploading " + info.getFile().getPath());
		PutObjectRequest request = new PutObjectRequest(
			bucket, info.getKey(), info.getFile()
		);
		request.setCannedAcl(info.getPermission());
		try {
			client.putObject(request);
		}catch (Throwable e){
			getLog().error(e);
		}
	}

	private String convertToString(List<String> list) {
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (Iterator<?> iterator = list.iterator(); iterator.hasNext(); i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		return builder.toString();
	}
}
