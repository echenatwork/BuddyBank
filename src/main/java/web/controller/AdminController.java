package web.controller;

import db.entity.InterestRateSchedule;
import db.entity.InterestRateScheduleBucket;
import db.entity.RoleCode;
import db.entity.User;
import manager.AccountManager;
import manager.AccountTransactionManager;
import manager.InterestRateScheduleManager;
import manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import web.model.CreateInterestRateScheduleRequest;
import web.model.CreateUserRequest;
import web.model.LoadInterestRateScheduleRequest;

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

    @Autowired
    private InterestRateScheduleManager interestRateScheduleManager;

    @GetMapping
    public String adminMainView(Model model, HttpServletRequest httpServletRequest) {
        addAdminMessages(model, httpServletRequest);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        model.addAttribute("createUserRequest", createUserRequest);

        // returns the view name
        return "admin";
    }

    @GetMapping("/create-interest-rate-schedule")
    public String createInterestRateScheduleView(Model model, HttpServletRequest httpServletRequest) {
        addAdminMessages(model, httpServletRequest);

        CreateInterestRateScheduleRequest createInterestRateScheduleRequest = new CreateInterestRateScheduleRequest();
        model.addAttribute("createInterestRateScheduleRequest", createInterestRateScheduleRequest);

        List<String> codes = interestRateScheduleManager.getInterestRateScheduleCodes();
        model.addAttribute("interestRateScheduleCodes", codes);
        model.addAttribute("loadInterestRateScheduleRequest", new LoadInterestRateScheduleRequest());

        // returns the view name
        return "create-interest-rate-schedule";
    }

    @PostMapping("/create-interest-rate-schedule")
    public String createInterestRateScheduleEndpoint(@ModelAttribute CreateInterestRateScheduleRequest createInterestRateScheduleRequest,
                                                     @ModelAttribute LoadInterestRateScheduleRequest loadInterestRateScheduleRequest,
                                                     HttpServletRequest httpServletRequest,
                                                     RedirectAttributes redirectAttributes) {

        /* TODO
         * This part of dynamic tables works, but this is really awkward, must be a better way to do the
         * dynamic tables purely on client side.
         */
        if (httpServletRequest.getParameter("addRow") != null) {
            return addRow(createInterestRateScheduleRequest);
        } else if (httpServletRequest.getParameter("removeRow") != null) {
            return removeRow(createInterestRateScheduleRequest, httpServletRequest);
        }

        List<InterestRateScheduleBucket> bucketEntities = new ArrayList<>();
        for (CreateInterestRateScheduleRequest.InterestRateBucket requestBucket : createInterestRateScheduleRequest.getInterestRateBuckets()) {
            InterestRateScheduleBucket interestRateScheduleBucket = new InterestRateScheduleBucket();
            interestRateScheduleBucket.setAmountFloor(requestBucket.getAmountFloor());
            interestRateScheduleBucket.setAmountCeiling(requestBucket.getAmountCeiling());
            interestRateScheduleBucket.setInterestRate(requestBucket.getInterestRate());
            bucketEntities.add(interestRateScheduleBucket);
        }

        List<String> messages = new ArrayList<>();

        try {
            InterestRateSchedule interestRateSchedule = interestRateScheduleManager.saveInterestRateSchedule(createInterestRateScheduleRequest.getCode(), createInterestRateScheduleRequest.getName(), bucketEntities);
            messages.add("Successfully created interest rate schedule with name=" + interestRateSchedule.getName());
        } catch (Exception e) {
            // TODO add logging
            messages.add(e.getMessage());
            e.printStackTrace();
        }

        redirectAttributes.addFlashAttribute(FLASH_MAP_ADMIN_MESSAGES, messages);

        // returns the view name
        return "redirect:/admin";
    }

    private String addRow(final CreateInterestRateScheduleRequest createInterestRateScheduleRequest) {
        if (createInterestRateScheduleRequest.getInterestRateBuckets() == null) {
            createInterestRateScheduleRequest.setInterestRateBuckets(new ArrayList<>());
        }
        createInterestRateScheduleRequest.getInterestRateBuckets().add(new CreateInterestRateScheduleRequest.InterestRateBucket());
        return "create-interest-rate-schedule";
    }

    private String removeRow(
            final CreateInterestRateScheduleRequest createInterestRateScheduleRequest, final HttpServletRequest req) {
        final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
        createInterestRateScheduleRequest.getInterestRateBuckets().remove(rowId.intValue());
        return "create-interest-rate-schedule";
    }

    @PostMapping("/load-interest-rate-schedule")
    public String loadInterestRateScheduleEndpoint(@ModelAttribute LoadInterestRateScheduleRequest loadInterestRateScheduleRequest,
                                                   @ModelAttribute CreateInterestRateScheduleRequest createInterestRateScheduleRequest,
                                                   HttpServletRequest httpServletRequest,
                                                   RedirectAttributes redirectAttributes) {

        InterestRateSchedule interestRateSchedule = interestRateScheduleManager.getInterestRateScheduleByCode(loadInterestRateScheduleRequest.getCode());

        createInterestRateScheduleRequest.setCode(interestRateSchedule.getInterestRateScheduleCode());
        createInterestRateScheduleRequest.setName(interestRateSchedule.getName());

        List<CreateInterestRateScheduleRequest.InterestRateBucket> modelBuckets = new ArrayList<>();
        for (InterestRateScheduleBucket bucket : interestRateSchedule.getInterestRateScheduleBuckets()) {
            CreateInterestRateScheduleRequest.InterestRateBucket modelBucket = new CreateInterestRateScheduleRequest.InterestRateBucket();
            modelBucket.setAmountCeiling(bucket.getAmountCeiling());
            modelBucket.setAmountFloor(bucket.getAmountFloor());
            modelBucket.setInterestRate(bucket.getInterestRate());
            modelBuckets.add(modelBucket);
        }
        createInterestRateScheduleRequest.setInterestRateBuckets(modelBuckets);

        // returns the view name
        return "create-interest-rate-schedule";
    }

    @GetMapping("/create-user")
    public String createUserView(Model model, HttpServletRequest httpServletRequest) {
        addAdminMessages(model, httpServletRequest);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        model.addAttribute("createUserRequest", createUserRequest);

        // returns the view name
        return "create-user";
    }

    private void addAdminMessages(Model model, HttpServletRequest httpServletRequest) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);
        List<String> messages = null;
        if (flashMap != null) {
            messages = (List<String>) flashMap.get(FLASH_MAP_ADMIN_MESSAGES);
        }
        model.addAttribute("messages", messages);
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

