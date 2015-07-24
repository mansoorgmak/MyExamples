package com.cloud.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fedbid.cloud.dao.AmazonS3ClientDAO;

public class AmazonS3ClientDAOImpl implements AmazonS3ClientDAO{
	@Autowired
	private AmazonS3 s3client;
	
	public void writeSuccessMessage(String input){
		s3client.putObject(new PutObjectRequest("attachdev", "testKey", new java.io.StringBufferInputStream(input), null));
	}
}
