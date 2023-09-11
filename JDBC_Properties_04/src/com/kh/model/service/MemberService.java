package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;

public class MemberService {
	//1) JDBC driver 등록
	//2)Connection 객체 생성
	//3)Connection 객체 처리
	
	public int insertMember(Member m) {
		Connection conn = JDBCTemplate.getConnection();
		int result = new MemberDao().insertMember(conn, m);
	
		if(result > 0) {
			JDBCTemplate.commit(conn);
		}else {
			JDBCTemplate.rollback(conn);
		}
		return result;
	}
	
	public ArrayList<Member> selectList() {
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<Member> list = new MemberDao().selectList(conn);
		JDBCTemplate.close(conn);
		return list;
	}
	
	public Member selectByUserId(String id) {
		Connection conn = JDBCTemplate.getConnection();
		Member m = new MemberDao().selectByUserId(conn, id);
		JDBCTemplate.close(conn);
		return m;
	}
	
	public ArrayList<Member> selectByUserName(String keyword) {
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<Member> list  = new MemberDao().selectByUserName(conn, keyword);
		JDBCTemplate.close(conn);
		return list;
	}
	
	public int updateMember(Member m) {
		Connection conn = JDBCTemplate.getConnection();
		int num  = new MemberDao().updateMember(conn, m);
		if(num > 0)
			JDBCTemplate.commit(conn);
		else
			JDBCTemplate.rollback(conn);
		
		JDBCTemplate.close(conn);
		return num;
	}
	
	public int selectDelete(String inputMemberId) {
		Connection conn = JDBCTemplate.getConnection();
		int num  = new MemberDao().deleteMember(conn, inputMemberId);
		if(num > 0)
			JDBCTemplate.commit(conn);
		else
			JDBCTemplate.rollback(conn);
		
		JDBCTemplate.close(conn);
		return num;
	}
	
	public Member loginMember(String inputMemberId, String inputMemberPw) {
		Connection conn = JDBCTemplate.getConnection();
		Member m  = new MemberDao().loginMember(conn, inputMemberId, inputMemberPw);
		JDBCTemplate.close(conn);
		return m;
	}

}
