package jpa.jpabank.service;

import jpa.jpabank.domain.Friend;
import jpa.jpabank.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    /**
     * 친구 추가
     */
    @Transactional
    public Long join(Friend friend){
        validateDuplicateFriend(friend);
        friendRepository.save(friend);
        return friend.getId();
    }

    private void validateDuplicateFriend(Friend friend) {
        List<Friend> findFriend = friendRepository.findByName(friend.getName());
        if(!findFriend.isEmpty()){
            throw new IllegalStateException("이미 존재하는 친구입니다.");
        }
    }

    /**
     * 친구 전체 조회
     */
    public List<Friend> findFriends(){
        return friendRepository.findAll();
    }

    /**
     * 친구 단건 조회
     */
    public Friend findOne(Long id){
        return friendRepository.findOne(id);
    }

    /**
     * 친구 정보 수정
     */
    @Transactional
    public void update(Long id, String name){
        Friend findFriend = friendRepository.findOne(id);
        findFriend.setName(name);
    }
}
