package com.example.server;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SmartNewsReaderServer extends HttpServlet {

	private static final long serialVersionUID = 6421694655487416577L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestHandler client = new RequestHandler(resp);
		try {
			InputStreamReader out = new InputStreamReader(req.getInputStream());
			StringBuilder line = new StringBuilder();
			int charCode = -1;
			while ((charCode = out.read()) != -1) {
				line.append((char) charCode);
			}
			client.handleIncoming(line.toString().trim());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
