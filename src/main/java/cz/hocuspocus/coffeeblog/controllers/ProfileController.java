package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.models.dto.ProfileDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.ProfileMapper;
import cz.hocuspocus.coffeeblog.models.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileMapper profileMapper;

    /*
    Get method for getting logged user detail
     */
    @GetMapping("userprofile")
    public String renderLoggedUserProfile(Model model) {
        ProfileDTO fetchedLoggedUserDTO = profileService.getLoggedUserProfile();
        model.addAttribute("loggedUserDTO", fetchedLoggedUserDTO);
        return "pages/profile/index";
    }

}
