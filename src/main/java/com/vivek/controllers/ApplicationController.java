package com.vivek.controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vivek.services.DownloadService;

@RestController
public class ApplicationController {
	@Autowired
	DownloadService downloadService;
	Logger log = Logger.getLogger(getClass().getName());
	@RequestMapping("/downloadVideos")
	public String downloadVideos(@RequestParam String baseUrl) {
		try{
			downloadService.downloadVideos(baseUrl);
		}
		catch(IOException ie) {
			log.log(Level.WARNING, ie.getCause()+" "+ie.getMessage());
			return "Failed";
		}
		return "Success";
	}
}
