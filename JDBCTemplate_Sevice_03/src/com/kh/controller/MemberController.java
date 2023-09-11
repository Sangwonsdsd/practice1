package com.kh.controller;

import java.util.ArrayList;
import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;
import com.kh.common.JDBCTemplate;
import com.kh.model.dao.MemberDao;
//import com.kh.model.vo.Member;
//import com.kh.view.MemberMenu;
//Controller : View를 통해서 사용자가 요청한 기능에 대해서 처리하는 담당
//해당 메소드로 전달된 데이터 [가공처리후] dao 전달하여 호출
//dao로부터 반환받은 결과에따라서 성공인지 실패인지 판단 후 응답화면 결정(View 메소드 호출)
import com.kh.model.service.MemberService;

public class MemberController {

	/**
	 * 사용자의 추가요청을 처리해주는 메소드
	 * 
	 * @param userId ~ hobby : 사용자가 입력했던 정보들이 담겨있는 매개변수
	 */
	public void insertMember(String userId, String userPwd, String userName, String gender, String age, String email,
			String phone, String address, String hobby) {
		Member m = new Member(userId, userPwd, userName, gender, Integer.parseInt(age), email, phone, address, hobby);
		int result = new MemberService().insertMember(m);
		
		if(result > 0) {//insert가 성공
			new MemberMenu().displaySuccess("성공적으로 회원이 추가 되었습니다.");
		} else {//insert가 실패
			new MemberMenu().displayFail("회원추가를 실패하였습니다.");
		}
	}

	/**
	 * 사용자의 회원 전체 조회 요청을 처리해주는 메소드
	 */

	public void sellectAll() {
		ArrayList<Member> list = new MemberService().selectList();

		if (list.isEmpty()) {
			new MemberMenu().displayNoData("전체 조회 결과가 없습니다");
		} else {
			new MemberMenu().displayMemberList(list);
		}
	}

	/**
	 * 사용자의 아이디로 회원 검색 요청을 처리해주는 메소드
	 * 
	 * @param id : 사용자가 입력한 검색하고자하는 회원 아이디
	 */

	public void selectByUserId(String id) {
		Member m = new MemberService().selectByUserId(id);
		if (m == null) { // 검색 결과가 없는 경우
			new MemberMenu().displayNoData(id + "에 해당하는 조회된 결과가 없습니다.");
		} else { // 검색 결과가 없는 경우
			new MemberMenu().displayMember(m);
		}

	}

	/**
	 * 이름으로 키워드 검색 요청시 처리해주는 메소드
	 * 
	 * @param keyword : 사용자가 입력한 검색을 키워드명
	 */
	public void selectByUserName(String keyword) {
		
		ArrayList<Member> list = new MemberService().selectByUserName(keyword);
		if(list.size() > 0) {//리스트가 비어있을경우
			new MemberMenu().displayMemberList(list);
			
		}else {//조회된 리스트가 있을 경우
			new MemberMenu().displayNoData(keyword + "로 조회된 결과가 없습니다.");
		}

	}

	public void updateMember(String userId, String userPwd, String email, String phone, String address) {
		Member m = new Member();
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		m.setEmail(email);
		m.setPhone(phone);
		m.setAddress(address);
		
		int result = new MemberService().updateMember(m);
		if(result > 0) {//update가 성공
			new MemberMenu().displaySuccess("성공적으로 변경되었습니다.");
		} else {//update가 실패
			new MemberMenu().displayFail("변경이 실패하였습니다.");
		}
	}

	public void selectDelete(String inputMemberId) {
		int result = new MemberService().selectDelete(inputMemberId);
		if(result > 0) {//update가 성공
			new MemberMenu().displaySuccess("성공적으로 삭제되었습니다.");
		} else {//update가 실패
			new MemberMenu().displayFail("삭제가 실패하였습니다.");
		}
	}

}
