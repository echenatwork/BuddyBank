package web.controller;

import com.github.dozermapper.core.Mapper;
import db.entity.*;
import error.RequestException;
import manager.AccountManager;
import manager.AccountTransactionManager;
import manager.InterestRateScheduleManager;
import manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import util.Util;
import web.model.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
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

    @Autowired
    private Mapper mapper;

    @GetMapping
    public String adminMainView(Model model, HttpServletRequest httpServletRequest) {
        addAdminMessages(model, httpServletRequest);

        CreateUserRequest createUserRequest = new CreateUserRequest();
        model.addAttribute("createUserRequest", createUserRequest);

        // returns the view name
        return "admin";
    }

    @GetMapping("/assign-schedule-to-account")
    public String assignScheduleToAccount(Model model, HttpServletRequest httpServletRequest) {
        addAdminMessages(model, httpServletRequest);

        CreateInterestRateScheduleRequest createInterestRateScheduleRequest = new CreateInterestRateScheduleRequest();
        model.addAttribute("createInterestRateScheduleRequest", createInterestRateScheduleRequest);

        List<String> codes = interestRateScheduleManager.getInterestRateScheduleCodes();
        model.addAttribute("interestRateScheduleCodes", codes);
        model.addAttribute("loadInterestRateScheduleRequest", new LoadInterestRateScheduleRequest());

        // returns the view name
        return "assign-schedule-to-account";
    }

    @GetMapping(value = "/api/get-accounts", produces = "application/json")
    @ResponseBody
    public List<AccountBean> getAccounts(HttpServletRequest httpServletRequest) {
        List<Account> accounts = accountManager.getAllAccounts();

        List<AccountBean> accountBeans = new ArrayList<>();


        for (Account account : accounts) {
            AccountBean accountBean = new AccountBean();
            mapper.map(account, accountBean);
            accountBeans.add(accountBean);
        }

        accountBeans.sort(Comparator.comparing(AccountBean::getCode));
        return accountBeans;
    }

    @GetMapping(value = "/api/get-schedules-by-account", produces = "application/json")
    @ResponseBody
    public List<AccountToScheduleBean> getSchedulesByAccount(HttpServletRequest httpServletRequest, @RequestParam(name = "account") String accountCode) {
        Account account = accountManager.findByAccountCode(accountCode);

        List<AccountToScheduleBean> accountToScheduleBeans = new ArrayList<>();
        for(AccountToInterestRateSchedule accountToInterestRateSchedule : account.getAccountToInterestRateSchedules()) {
            AccountToScheduleBean accountToScheduleBean = new AccountToScheduleBean();
            mapper.map(accountToInterestRateSchedule, accountToScheduleBean);
            accountToScheduleBeans.add(accountToScheduleBean);
        }
        return accountToScheduleBeans;
    }

    @GetMapping(value = "/api/get-schedules", produces = "application/json")
    @ResponseBody
    public List<ScheduleBean> getSchedules(HttpServletRequest httpServletRequest, @RequestParam(name = "scheduleCode", required = false) String scheduleCode) {
        List<ScheduleBean> scheduleBeans = new ArrayList<>();
        List<InterestRateSchedule> interestRateSchedules = null;

        if (StringUtils.isEmpty(scheduleCode)) {
            interestRateSchedules = interestRateScheduleManager.getInterestRateSchedules();
        } else {
            interestRateSchedules = new ArrayList<>();
            interestRateSchedules.add(interestRateScheduleManager.getInterestRateScheduleByCode(scheduleCode));
        }

        for(InterestRateSchedule interestRateSchedule : interestRateSchedules) {
            ScheduleBean scheduleBean = new ScheduleBean();
            mapper.map(interestRateSchedule, scheduleBean);
            scheduleBeans.add(scheduleBean);
        }
        return scheduleBeans;
    }

    @PostMapping(value = "/api/add-schedule-to-account", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> addScheduleToAccount(HttpServletRequest httpServletRequest,
                                               @RequestParam(name = "scheduleCode") String scheduleCode,
                                               @RequestParam(name = "accountCode") String accountCode,
                                               @RequestParam(name = "startDate") String startDateString,
                                               @RequestParam(name = "endDate") String endDateString) throws Exception {
        try {
            Instant startDate = Util.parseFromDatePicker(startDateString).toInstant();
            Instant endDate = Util.parseFromDatePicker(endDateString).toInstant();

            if (endDate.isBefore(startDate)) {
                throw new RequestException("End date is before start date");
            }

            if (StringUtils.isEmpty(accountCode)) {
                throw new RequestException("Account code is empty");
            } else if (StringUtils.isEmpty(scheduleCode)) {
                throw new RequestException("Schedule code is empty");
            }


            accountManager.addScheduleToAccount(startDate, endDate, accountCode, scheduleCode);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/api/account/schedule/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> deleteAccountToSchedule(HttpServletRequest httpServletRequest,
                                                       @PathVariable(value="id") Long accountToScheduleId) throws Exception {
        accountManager.deleteScheduleFromAccount(accountToScheduleId);
        return ResponseEntity.ok().build();
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

