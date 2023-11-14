package cz.hocuspocus.coffeeblog.controllers;

import cz.hocuspocus.coffeeblog.models.dto.ArticleDTO;
import cz.hocuspocus.coffeeblog.models.dto.ProfileDTO;
import cz.hocuspocus.coffeeblog.models.dto.mappers.ProfileMapper;
import cz.hocuspocus.coffeeblog.models.services.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /*
    Get method for editing profile
     */
    @GetMapping("edit/{profileId}")
    public String renderEditProfile(@PathVariable Long profileId, ProfileDTO profileDTO){
        ProfileDTO fetchedProfile = profileService.getById(profileId);
        profileMapper.updateProfileDTO(fetchedProfile, profileDTO);
        return "pages/profile/edit";
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
            return renderEditProfile(profileId, profileDTO);
        }

        profileDTO.setId(profileId);
        profileService.editProfile(profileDTO);
        redirectAttributes.addFlashAttribute("success","The profile was successfully edited.");

        return "redirect:/profile/userprofile";
    }

}
