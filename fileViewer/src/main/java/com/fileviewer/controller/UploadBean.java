package com.fileviewer.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Named
public class UploadBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static FacesContext facesContext = FacesContext.getCurrentInstance();
	static ExternalContext externalContext = facesContext.getExternalContext();
	
	private UploadedFile file;
	private static String PATH = externalContext.getRealPath("/uploads/");
	
	public void upload(FileUploadEvent event) throws IOException{
		
		this.file = event.getFile();
		
		String fileNameOutput = PATH + UUID.randomUUID().toString() + "___" + file.getFileName();
		byte[] bytes = null;
		
		if (null != file) {
			bytes = file.getContents();
		
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(fileNameOutput)));
			stream.write(bytes);
			stream.close();
		}
		
		FacesContext.getCurrentInstance().addMessage("messages",
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo(s) importado(s) com sucesso", ""));
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}
	
}
