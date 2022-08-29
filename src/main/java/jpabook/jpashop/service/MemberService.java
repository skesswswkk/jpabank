package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service //@Component 내장하고 있어 컴포넌트 스캔의 대상이 되어, 자동으로 스프링 빈으로 등록
@Transactional(readOnly = true) //JPA에서 모든 데이터 변경은 Transaction 안에서 발생해야 한다
@RequiredArgsConstructor //final 보고 생성자 생성
public class MemberService {

//    @Autowired //스프링이 스프링빈에 등록되어 있는 MemberRepository -> memberRepository 에 인젝션
    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMember = memberRepository.findByName(member.getName());
        if(!findMember.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    //회원 단건 조회
    public Member findOne(Long id){
        return memberRepository.findOne(id);
    }

    //수정
    @Transactional
    public void update(Long id, String name){
        Member findMember = memberRepository.findOne(id);
        findMember.setName(name);
    }
}
