package com.fileviewer.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;

@Named
@ViewScoped
public class ListFilesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static FacesContext facesContext = FacesContext.getCurrentInstance();
	static ExternalContext externalContext = facesContext.getExternalContext();

	private static String PATH = externalContext.getRealPath("/uploads/");

	private File file = new File(PATH);
	
	private List<File> listFiles;
	private String fileName; 
	private int amount = 0;
	
	public ListFilesBean() {
	
	}
	
	public void search() {
		this.listFiles = list();
		this.amount = file.listFiles().length;
	}

	public void remove(File file) {
		if (file.isFile()) {
			file.delete();
			search();
		}
	}

	public List<File> list() {

		File[] names;
		
		File[] filter = file.listFiles(new FileFilter() {
			public boolean accept(File b) {
				return b.getName().contains(fileName);
			}
		});

		names = filter; // lista os arquivos
		this.listFiles = Arrays.asList(names); // Array para List
		Collections.sort(this.listFiles); // ordenar lista
		return this.listFiles;

	}
	
	public String showCreateDate(Long date) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return format.format(date);
	}

	public static String fileInfo(File file) {
		long fileSize = file.length();
		String fileSizeDisplay = FileUtils.byteCountToDisplaySize(fileSize);
		return fileSizeDisplay;
	}

	public void download(File name) throws IOException {

		String fileName = name.getName();

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();

		if (fileName.endsWith(".pdf") || fileName.endsWith(".png") || fileName.endsWith(".jpeg")
				|| fileName.endsWith(".jpg") || fileName.endsWith(".tif") || fileName.endsWith(".tiff")) {

			/*
			 * externalContext.responseReset();
			 * externalContext.setResponseContentType("application/pdf");
			 */
			externalContext.setResponseHeader("Content-Disposition", " filename=\"" + fileName + "\"");

		} else {
			externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		}

		OutputStream out = externalContext.getResponseOutputStream();

		File file = new File(PATH + fileName);
	
		// pega os bytes e envia para a saída
		try (InputStream is = new FileInputStream(file)) {
			int read = -1;
			byte[] buffer = new byte[1024];

			while ((read = is.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
		}

		facesContext.responseComplete();
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<File> getListFiles() {
		return listFiles;
	}
	
	public void setListFiles(List<File> listFiles) {
		this.listFiles = listFiles;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
