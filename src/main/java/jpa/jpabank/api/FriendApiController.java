package jpa.jpabank.api;

import jpa.jpabank.domain.Friend;
import jpa.jpabank.service.FriendService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class FriendApiController {

    private final FriendService friendService;

    /** 등록 */
    @PostMapping("api/v1/friends")
    public CreateFriendResponse saveFriendV1(@RequestBody @Valid Friend friend){
        Long id = friendService.join(friend);
        return new CreateFriendResponse(id);
    }

    @PostMapping("api/v2/friends")
    public CreateFriendResponse saveFriendV2(@RequestBody @Valid CreateFriendRequest request){

        Friend friend = new Friend();
        friend.setName(request.getName());

        Long id = friendService.join(friend);
        return new CreateFriendResponse(id);
    }

    @Data
    static class CreateFriendRequest {
        private String name;
    }

    @Data
    static class CreateFriendResponse{
        private Long id;

        public CreateFriendResponse(Long id) {
            this.id = id;
        }
    }

    /** 조회 */
    @GetMapping("api/v1/friends")
    public List<Friend> friends(){
        return friendService.findFriends();
    }

    @GetMapping("api/v2/friends")
    public Result friendV2(){
        List<Friend> findFriends = friendService.findFriends();

        List<FriendDto> collect = findFriends.stream()
                .map(m -> new FriendDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int transferMoney;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class FriendDto{
        private String name;
    }

    /** 수정 */
    @PutMapping("api/v2/friends/{id}")
    public UpdateFriendResponse updateFriendV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateFriendRequest request){

        friendService.update(id, request.getName());
        return new UpdateFriendResponse(id, request.getName());
    }

    @Data
    static class UpdateFriendRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateFriendResponse{
        private Long id;
        private String name;
    }
}
