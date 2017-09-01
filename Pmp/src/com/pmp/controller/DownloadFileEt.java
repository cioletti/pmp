package com.pmp.controller;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import com.pmp.entity.PmpFileEt;

/**
 * Servlet implementation class DownloadFileEt
 */
public class DownloadFileEt extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadFileEt() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idOsPalm = request.getParameter("idOsPalm");

		EntityManager manager = null;
		
		try {
			manager = com.pmp.util.JpaUtil.getInstance();
			Query query = manager.createQuery("From PmpFileEt where idOsPalm =:idOsPalm");
			query.setParameter("idOsPalm", Long.valueOf(idOsPalm));
			PmpFileEt fileEt = (PmpFileEt)query.getSingleResult();

			byte[] bytes = (byte[]) fileEt.getFileEt();

			ServletOutputStream servletOutputStream = response
			.getOutputStream();
			response.setContentType("text/xml");
			
			response.setHeader("Content-disposition",
			"attachment; filename=file_et.xml");
			if (bytes != null) {
				Blob blob = new SerialBlob(bytes);

				java.io.InputStream blobStream = blob.getBinaryStream();


				byte[] buffer = new byte[10];
				int nbytes = 0;

				while ((nbytes = blobStream.read(buffer)) != -1) {
					servletOutputStream.write(buffer, 0, nbytes);
				}
				blobStream.close();

			}
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (NoResultException nre) {
			// TODO: handle exception		
		} catch (SerialException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}

	}

}
