package jpa.jpabank.controller;

import jpa.jpabank.domain.Friend;
import jpa.jpabank.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/friends/new")
    public String createForm(Model model){
        model.addAttribute("friendForm", new FriendForm());
        return "friends/createFriendForm";
    }

    @PostMapping("/friends/new")
    public String create(@Valid FriendForm form, BindingResult result){

        if(result.hasErrors()){
            return "friends/createFriendForm";
        }

        Friend friend = new Friend();
        friend.setName(form.getName());

        friendService.join(friend);
        return "redirect:/";
    }

    @GetMapping("/friends")
    public String list(Model model){
        List<Friend> friends = friendService.findFriends();
        model.addAttribute("friends", friends);
        return "friends/friendList";
    }
}
