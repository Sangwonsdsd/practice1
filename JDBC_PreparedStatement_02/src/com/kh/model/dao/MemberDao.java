package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import com.kh.model.vo.Member;

//DAO(Data Access Object) : DB에 직접적으로 접근해서 사용자의 요청에
//							맞는 sql문 실행 후 결과 반환(JDBC)
//							결과를 Controller로 다시 리턴
public class MemberDao {
	/*
	 * JDBC용 객체 -Connection : DB의 연결정보를 담고있는 객체 -[Prepared]Statement : 연결된
	 * 데이터베이스(DB)에 SQL문을 전달해서 실행하고, 결과를 받아내는 객체 -ResultSet : SELECT문 실행 후 조회된 결과물들이
	 * 담겨있는 객체
	 * 
	 * *JDBC 과정(순서 중요) 1) jdbc driver 등록 : 해당 DBMS(오라클)가 제공하는 클래스 등록 2) Connection
	 * 생성 : 연결하고자 하는 DB정보를 입력해서 해당 DB와 연결하면서 생성 3) Statement 생성 : Connection 객체를
	 * 이용해서 생성(sql문 실행 및 결과를 받는 객체) 4) sql문 전달하면서 실행 : Statement 객체를 이용해서 sql문 실행 5)
	 * 결과 받기 > SELECT문 객체 => ResultSet객체 (조회된 데이터들이 담겨있음) => 6_1) > DML문 실행 =>
	 * int(처리된 행 수)
	 * 
	 * 6_1) ResultSet에 담겨있는 데이터들을 하나씩 하나씩 뽑아서 vo객체에 차근차근 옮겨담기[+ ArrayList에 담아주기]
	 * 6_2) 트랜잭션 처리(성공했다면 commit, 실패했다면 rollback 실행)
	 * 
	 * 7) 다사용한 JDBC용 객체들 반드시 자원 반납(close) => 생성된 역순으로
	 */
	/**
	 * 사용자가 입력한 정보들을 db에 추가시켜주는 메소드
	 * 
	 * @param m : 사용자가 입력한 값들이 담겨있는 member 객체
	 * @return : insert문 실행 후 처리된 행 수
	 */
	public int insertMember(Member m) {

		// insert문 => 처리된 행수(int) => 트랜잭션 처리
		// 필요한 변수 세팅
		int result = 0; // 처리된 결과(처리된 행수)를 받아줄 변수
		Connection conn = null; // 연결된 db의 연결정보를 담는 객체
		PreparedStatement pstmt = null; // 완성된 sql문 전달해서 곱바로 실행 후 결과를 받는 객체

		// 실행 sql문(완성된 형태)

//		INSERT INTO MEMBER
//		VALUES(SEQ_USERNO.NEXTVAL, 'user01', 'pass01',
//		'홍길동', null, 23, 'user01@iei.or.kr', '01022222222', '부산', '등산, 영화보기', '2021-08-02');

		String sql = "INSERT INTO MEMBER VALUES(SEQ_USERNO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";

		try {
			// 1) jdbc driver등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2) Connection 객체 생성 => db연결
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			// 3)Statement 객체 생성
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPwd());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());

			// 4, 5) sql문 전달하면서 실행 후 결과받기(처리된 행수)
			
			result = pstmt.executeUpdate();
			// 6) 트랜잭션 처리
			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 7) 다쓴 jdbc객체들 반환
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public ArrayList<Member> selectList() {

		// select문(여러행 조회) => ResultSet객체 => ArrayList<Member>에 담기
		// 담을 객체 리스트
		ArrayList<Member> mlist = new ArrayList<Member>(); // 비어있는 상태
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null; // select문 실행시 조회된결과값들이 최초로 담기는 객체
		String sql = "SELECT * FROM MEMBER";

		try {
			// 1)JDBC 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2) Connection 객체 생성 => db연결
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			// 3)Statement 객체 생성
			pstmt = conn.prepareStatement(sql);
			// 4,5) sql문 실행및 결과 받기
			rset = pstmt.executeQuery();
			// 6) ResultSet으로부터 데이터를 하나씩 꺼내서
			while (rset.next()) {
				Member mem = new Member();
				mem.setUserNo(rset.getInt("USERNO"));
				mem.setUserId(rset.getString("USERID"));
				mem.setUserPwd(rset.getString("USERPWD"));
				mem.setUserName(rset.getString("USERNAME"));
				mem.setGender(rset.getString("GENDER"));
				mem.setAge(rset.getInt("AGE"));
				mem.setEmail(rset.getString("EMAIL"));
				mem.setPhone(rset.getString("PHONE"));
				mem.setAddress(rset.getString("ADDRESS"));
				mem.setHobby(rset.getString("HOBBY"));
				mem.setEnrollDate(rset.getDate("ENROLLDATE"));
				// 현재 참조하고있는 행에 대한 모든 컬럼에 ㄷ데이터들을 한 Member객체에 담기! 끝!
				mlist.add(mem); // 리스트에 담기
			}
			// 조회된 데이터가 없다면 리스트는 비어있다.
			// 조회된 데이터가 있다면 list에는 한개이상 담겨있을 것이다.
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mlist;
	}

	public Member selectByUserId(String id) {
		Member m = new Member();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = "SELECT * FROM MEMBER WHERE USERID = ?";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2) Connection 객체 생성 => db연결
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			// 3)Statement 객체 생성
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			//4)
			rset = pstmt.executeQuery();
			if (rset.next()) {
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserPwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("GENDER"));
				m.setAge(rset.getInt("AGE"));
				m.setEmail(rset.getString("EMAIL"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setHobby(rset.getString("HOBBY"));
				m.setEnrollDate(rset.getDate("ENROLLDATE"));
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return m;
	}

	public ArrayList<Member> selectByUserName(String keyword) {
		// select문(여러행) => ResultSet객체 => ArrayList<Member>객체에 담기
		ArrayList<Member> list = new ArrayList<>(); // 텅빈상태

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%XXX%'
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?  ";

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, "%" + keyword + "%");;
			rset =  pstmt.executeQuery();
			while (rset.next()) {
				Member m = new Member();
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserPwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("GENDER"));
				m.setAge(rset.getInt("AGE"));
				m.setEmail(rset.getString("EMAIL"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setHobby(rset.getString("HOBBY"));
				m.setEnrollDate(rset.getDate("ENROLLDATE"));
				list.add(m);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}
	
	public int updateMember(Member m ) {
		
		int result = 0; // 처리된 결과(처리된 행수)를 받아줄 변수
		Connection conn = null; // 연결된 db의 연결정보를 담는 객체
		PreparedStatement pstmt = null; // 완성된 sql문 전달해서 곱바로 실행 후 결과를 받는 객체
		
		
		String sql = "UPDATE MEMBER "
				+ "SET USERPWD = ?"
				+ " , EMAIL =  ?"
				+ ", PHONE = ?"
				+ ", ADDRESS = ?"
				+" WHERE USERID = ?";
		
		try {
			// 1) jdbc driver등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2) Connection 객체 생성 => db연결
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			// 3)Statement 객체 생성
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getUserPwd());
			pstmt.setString(2, m.getEmail());
			pstmt.setString(3, m.getPhone());
			pstmt.setString(4, m.getAddress());
			pstmt.setString(5, m.getUserId());


			// 4, 5) sql문 전달하면서 실행 후 결과받기(처리된 행수)
			result = pstmt.executeUpdate();
			
			// 6) 트랜잭션 처리
			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 7) 다쓴 jdbc객체들 반환
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public int deleteMember(String inputMemberId) {
		int result = 0; // 처리된 결과(처리된 행수)를 받아줄 변수
		Connection conn = null; // 연결된 db의 연결정보를 담는 객체
		PreparedStatement pstmt = null; // 완성된 sql문 전달해서 곱바로 실행 후 결과를 받는 객체
		
		String sql = "DELETE MEMBER WHERE USERID = ? ";
		try {
			// 1) jdbc driver등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2) Connection 객체 생성 => db연결
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC");
			// 3)Statement 객체 생성
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inputMemberId);

			// 4, 5) sql문 전달하면서 실행 후 결과받기(처리된 행수)
			result = pstmt.executeUpdate();
			
			// 6) 트랜잭션 처리
			if (result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 7) 다쓴 jdbc객체들 반환
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
