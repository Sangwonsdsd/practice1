package com.kh.controller;
import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;
//Controller : View를 통해서 사용자가 요청한 기능에 대해서 처리하는 담당
//해당 메소드로 전달된 데이터 [가공처리후] dao 전달하여 호출
//dao로부터 반환받은 결과에따라서 성공인지 실패인지 판단 후 응답화면 결정(View 메소드 호출)


public class MemberController {
	
	/**
	 * 사용자의 추가요청을 처리해주는 메소드 
	 * @param userId ~ hobby : 사용자가 입력했던 정보들이 담겨있는 매개변수
	 */
	public void insertMember(String userId, String userPwd, String userName, String gender, String age, String email,
			String phone, String address, String hobby) {
		//view로부터 전달받은 값을 바로 dao쪽으로 전달x
		//어딘가(Member객체(vo))에 담아서 전달
		//방법1) 기본생성자로 생산한 후에 각 필드에 setter메소드를 통해서 일일히 담는 방법
		//방법2) 매개변수 생성자로 생성과 동시에 담는 방법
		
		Member m = new Member(userId, userPwd, userName, gender, Integer.parseInt(age), email, phone, address, hobby);
		System.out.println(m);
	
		int result = new MemberDao().insertMember(m);
		
		if(result > 0) {//insert가 성공
			new MemberMenu().displaySuccess("성공적으로 회원이 추가 되었습니다.");
		} else {//insert가 실패
			new MemberMenu().displayFail("회원추가를 실패하였습니다.");
		}
			
	}

	public void sellectAll() {
		//모든 사용자의 정보를 넣는다.
		ArrayList<Member> mlist = new MemberDao().selectList();
		
//		if(mlist.size() > 0) {
//			new MemberMenu().displayMemberList(mlist);
//		}else {
//			System.out.println("추가된 회원이 없습니다.");
//		}
		
		if(mlist.isEmpty()) {//리스트가 비어있을경우
			new MemberMenu().displayNoData("조회된 결과가 없습니다.");
		}else {//조회된 리스트가 있을 경우
			new MemberMenu().displayMemberList(mlist);
		}
			
	}
	/**
	 * 사용자의 아이디로 회원 검색 요청을 처리해주는 메소드
	 * @param id : 사용자가 입력한 검색하고자하는 회원 아이디
	 */

	public void selectByUserId(String id) {
		Member mem = new MemberDao().selectByUserId(id);
		if(mem == null) { //검색 결과가 없는 경우
			new MemberMenu().displayNoData(id + "에 해당하는 조회된 결과가 없습니다.");
		}else { //검색 결과가 없는 경우
			new MemberMenu().displayMember(mem);
		}
		
	}
	/**
	 * 이름으로 키워드 검색 요청시 처리해주는 메소드
	 * @param keyword : 사용자가 입력한 검색을 키워드명
	 */
	public void selectByUserName(String keyword) {
		ArrayList<Member> list = new MemberDao().selectByUserName(keyword);
		
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
		
		int result = new MemberDao().updateMember(m);
		if(result > 0) {//update가 성공
			new MemberMenu().displaySuccess("성공적으로 변경되었습니다.");
		} else {//update가 실패
			new MemberMenu().displayFail("변경이 실패하였습니다.");
		}
		
	}

	public void selectDelete(String inputMemberId) {
		int result = new MemberDao().deleteMember(inputMemberId);
		if(result > 0) {//delete가 성공
			new MemberMenu().displaySuccess("성공적으로 삭제되었습니다.");
		} else {//delete가 실패
			new MemberMenu().displayFail("삭제가 실패하였습니다.");
		}
		
	}

}
