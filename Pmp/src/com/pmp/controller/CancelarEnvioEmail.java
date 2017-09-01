package com.pmp.controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmp.entity.PmpContrato;
import com.pmp.util.JpaUtil;

/**
 * Servlet implementation class CancelarEnvioEmail
 */
public class CancelarEnvioEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CancelarEnvioEmail() {
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
		EntityManager manager = null;
		String idContrato = request.getParameter("idContrato");
		try{
			manager = JpaUtil.getInstance();
			manager.getTransaction().begin();
			PmpContrato contrato = manager.find(PmpContrato.class, Long.valueOf(idContrato));
			contrato.setHasSendEmail("N");
			manager.merge(contrato);
			manager.getTransaction().commit();
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("Solicitação realizada com sucesso!");
		}catch (Exception e) {
			if(manager != null && manager.getTransaction().isActive()){
				manager.getTransaction().rollback();
			}
			e.printStackTrace();
		}finally{
			if(manager != null && manager.isOpen()){
				manager.close();
			}
		}
	}

}
