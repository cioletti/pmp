package com.pmp.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import flex.messaging.FlexContext;

/**
 * Servlet implementation class BuscarSerie
 */
public class BuscarSerie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BuscarSerie() {
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
		String numSerie = request.getParameter("serie");
		request.getSession().setAttribute("serie", numSerie);
		String modelo = request.getParameter("modelo");
		request.getSession().setAttribute("modelo", modelo);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/AtualizarHorimetro.html");
		dispatcher.forward(request,response);
	}

}
