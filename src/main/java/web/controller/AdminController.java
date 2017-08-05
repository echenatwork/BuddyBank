package web.controller;

import db.entity.RoleCode;
import db.entity.User;
import manager.AccountManager;
import manager.AccountTransactionManager;
import manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import web.model.CreateUserRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Eric on 4/1/2017.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String FLASH_MAP_ADMIN_MESSAGES = "messages";
    private static final String FLASH_MAP_CREATE_USER_SUCCESS = "createUserSuccessful";


    @Autowired
    private UserManager userManager;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private AccountTransactionManager accountTransactionManager;

    @GetMapping
    public String adminMainView(Model model, HttpServletRequest httpServletRequest) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);
        List<String> messages = null;
        if (flashMap != null) {
            messages = (List<String>)flashMap.get(FLASH_MAP_ADMIN_MESSAGES);
        }
        model.addAttribute("messages", messages);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        model.addAttribute("createUserRequest", createUserRequest);

        // returns the view name
        return "admin";
    }

    @PostMapping("/create-user")
    public String createUserEndpoint(@ModelAttribute CreateUserRequest createUserRequest, HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        Set<RoleCode> roleCodes = new HashSet<>();
        roleCodes.add(RoleCode.USER);

        if (createUserRequest.getIsAdmin()) {
            roleCodes.add(RoleCode.ADMIN);
        }

        List<String> messages = new ArrayList<>();

        boolean successfullyCreatedUser = false;
        User user = null;
        try {
            user = userManager.createNewUser(createUserRequest.getUsername(),
                    createUserRequest.getFirstName(), createUserRequest.getLastName(),
                    createUserRequest.getPassword(), roleCodes, createUserRequest.getAccountCode(),
                    createUserRequest.getInitialBalance());
            successfullyCreatedUser = true;
        } catch (Exception e) {
            // TODO add more logging
            messages.add(e.getMessage());
            e.printStackTrace();
        }

        if (successfullyCreatedUser) {
            messages.add("Successfully created user " + user.getUserName());
        }

        redirectAttributes.addFlashAttribute(FLASH_MAP_ADMIN_MESSAGES, messages);
        redirectAttributes.addFlashAttribute(FLASH_MAP_CREATE_USER_SUCCESS, successfullyCreatedUser);

        // returns the view name
        return "redirect:/admin";
    }
}

