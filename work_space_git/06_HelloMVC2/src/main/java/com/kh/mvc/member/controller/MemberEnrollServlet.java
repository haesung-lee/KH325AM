package com.kh.mvc.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kh.common.util.MyHttpServlet;
import com.kh.mvc.member.model.service.MemberService;
import com.kh.mvc.member.model.vo.Member;


/**
 * 1. get 요청이 올 경우 -> 회원가입 페이지로 이동
 * 2. post 요청이 올 경우 -> 회원가입 기능 동작
 */
@WebServlet(name="enroll", urlPatterns = "/member/enroll")
public class MemberEnrollServlet extends MyHttpServlet{
	private static final long serialVersionUID = 1L;
	
	private MemberService service = new MemberService();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//		Member member = getSessionMember(req);
		resp.sendRedirect(req.getContextPath() + "/views/member/enroll.jsp");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Member member = new Member();
		try {
			member.setId(req.getParameter("newId").trim());  // strip/trim으로 white space 제거
			member.setPassword(req.getParameter("userPwd"));
			member.setName(req.getParameter("userName").trim());
			member.setPhone(req.getParameter("phone").trim());
			member.setEmail(req.getParameter("email").strip());
			member.setAddress(req.getParameter("address").strip());
			if(req.getParameter("hobby") != null) {
				String hobby = String.join(",", req.getParameterValues("hobby"));
				member.setHobby(hobby.trim());
			}
			System.out.println("회원가입 : " + member);
		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("msg", "회원가입 실패!! (code:100=입력값 문제)");
			req.setAttribute("location", "/member/enroll");
			req.getRequestDispatcher("/views/common/msg.jsp").forward(req, resp);
//			sendCommonPage("회원가입 실패", "/member/enroll", req, resp);
			return;
		}
		
		int result = service.save(member);
		
		if(result > 0) {
			req.setAttribute("msg", "회원가입에 성공하였습니다!");
			req.setAttribute("location", "/");
		}else {
			req.setAttribute("msg", "회원가입 실패!! (code:101=DB이슈)");
			req.setAttribute("location", "/");
		}
		req.getRequestDispatcher("/views/common/msg.jsp").forward(req, resp);
	}

	@Override
	public String getServletName() {
		return "enroll";
	}
	
}
