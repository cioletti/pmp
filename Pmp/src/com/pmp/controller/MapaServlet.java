package com.pmp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.pmp.bean.UsuarioBean;
import com.pmp.business.LocalizacaoMaquinaBusiness;

/**
 * Servlet implementation class MapaServlet
 */
public class MapaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MapaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuarioBean usuarioBean = (UsuarioBean)request.getSession().getAttribute("usuario");
		String nivel = request.getParameter("nivel");
		String campoPesquisa = request.getParameter("campoPesquisa");
		LocalizacaoMaquinaBusiness business = new LocalizacaoMaquinaBusiness(usuarioBean);
		JSONObject jsonObject = new JSONObject();
		Map map =  business.findAllMaquinaLocalPl(nivel, campoPesquisa);
		//Map map =  business.findAllMaquinaPl(nivel, "0MFG08902");
		List<JSONObject> result = (List<JSONObject>)map.get("json");
		jsonObject.put("maquinaList",result);
		
	
		jsonObject.put("tecnicoList", business.findAllLocalizacaoCarroTecnico());
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().print(jsonObject);
		response.getWriter().flush();
	}

}
