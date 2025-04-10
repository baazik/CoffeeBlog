package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.ProfileDTO;
import cz.hocuspocus.coffeeblog.models.dto.UserDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.ProfileMapper;
import cz.hocuspocus.coffeeblog.models.services.ProfileService;
import cz.hocuspocus.coffeeblog.models.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

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

    /*
    Get method for getting user profile detail via id
     */
    @GetMapping("{profileId}")
    public String renderUserProfile(@PathVariable Long profileId, Model model){
        ProfileDTO profileDTO = profileService.getById(profileId);
        model.addAttribute("loggedUserDTO", profileDTO);
        return "pages/profile/detail";
    }

    /*
    Get method for editing profile
     */
    @GetMapping("edit/{profileId}")
    public String renderEditProfile(@PathVariable Long profileId, ProfileDTO profileDTO, RedirectAttributes redirectAttributes){
        ProfileDTO fetchedProfile = profileService.getById(profileId);
        ProfileDTO fetchedLoggedUserDTO = profileService.getLoggedUserProfile();
        if (fetchedProfile.getId() == fetchedLoggedUserDTO.getId()) {
            profileMapper.updateProfileDTO(fetchedProfile, profileDTO);
            return "pages/profile/edit";
        }

        redirectAttributes.addFlashAttribute("error","You can only edit your profile.");
        return "redirect:/profile/userprofile";
    }

    /*
    Post method for editing profile
     */

    @PostMapping("edit/{profileId}")
    public String editProfile(
            @PathVariable long profileId,
            @Valid ProfileDTO profileDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ){

        if (result.hasErrors()){
            return renderEditProfile(profileId, profileDTO, redirectAttributes);
        }

        profileDTO.setId(profileId);
        // with this, we make sure, the nickname will be also changed in user entity, not only in profile entity
        UserDTO userDTO = userService.getLoggedUserDTO();
        userDTO.setNickName(profileDTO.getNickName());

        profileService.editProfile(profileDTO, userDTO);
        redirectAttributes.addFlashAttribute("success","The profile was successfully edited.");

        return "redirect:/profile/userprofile";
    }

    /*
    Giving the list of articles to model for view
     */
    @GetMapping("list")
    public String renderProfiles(Model model)
    {
        List<ProfileDTO> profiles = profileService.getAll();
        model.addAttribute("profiles",profiles);
        return "pages/profile/list";
    }
    @GetMapping("list")
    public String renderProfiles(Model model)
    {
        List<ProfileDTO> profiles = profileService.getAll();
        model.addAttribute("profiles",profiles);
        return "pages/profile/list";
    }

}
