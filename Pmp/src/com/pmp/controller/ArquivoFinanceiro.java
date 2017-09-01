package com.pmp.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ArquivoFinanceiro
 */
public class ArquivoFinanceiro extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArquivoFinanceiro() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pastaArquivos = request.getParameter("pastaArquivos");
		String nomeArquivo = request.getParameter("descricaoArquivo");

		try {
			
			
			InputStream in = getClass().getClassLoader().getResourceAsStream(com.pmp.util.IConstantAccess.CONF_FILE);
			Properties prop = new Properties();

			prop.load(in);
			String dirArquivos = prop.getProperty("dir.gestaoArquivos");

			File file = new File(dirArquivos + pastaArquivos + "/" + nomeArquivo);
			
			FileInputStream fis = new FileInputStream(file);

	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        byte[] buf = new byte[1024];
	        try {
	            for (int readNum; (readNum = fis.read(buf)) != -1;) {
	                bos.write(buf, 0, readNum); //no doubt here is 0
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	        byte[] bytes = bos.toByteArray();

			ServletOutputStream servletOutputStream = response.getOutputStream();
			
			response.setHeader("Content-disposition", "attachment; filename=" + nomeArquivo);
			
			servletOutputStream.write(bytes);
			servletOutputStream.flush();
			servletOutputStream.close();
			bos.close();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
