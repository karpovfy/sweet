package com.fyodork.hello.controller;


import com.fyodork.hello.domain.User;
import com.fyodork.hello.domain.dto.captchaResponseDto;
import com.fyodork.hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {


    private final  static  String CAPTCHA_URL="https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

     @Autowired
    private UserService userService;


     @Autowired
     private RestTemplate restTemplate;

     @Value("${recaptcha.secret}")


     private String secret;


    @GetMapping("/registration")

    public String registration()
    {
        return "registration";
    }


    @PostMapping("/registration")

    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid  User user,
            BindingResult bindingResult,
            Model model)
    {

        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        captchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), captchaResponseDto.class);

        if (!response.isSuccess()) {
            model.addAttribute("captchaError", "Fill captcha");
        }

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if(isConfirmEmpty)
        {
            model.addAttribute("password2Error","password confirmation не может быть пустым");
        }


        if(user.getPassword()!=null && !user.getPassword().equals(passwordConfirm))
        {
            model.addAttribute("passwordError","passwords are different");
        }


        if(isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess())
        {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.addUser(user))
        {
            model.addAttribute("usernameError","User exists");
            return "registration";

        }



        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")

    public  String activate(Model model, @PathVariable String code)
    {
        boolean isActivated=userService.activateUser(code);
        if(isActivated)
        {

            model.addAttribute("message","user successfully activated");
            model.addAttribute("messageType","success");
        }
        else {
            model.addAttribute("message", "activation code is not found");
            model.addAttribute("messageType","danger");
        }

        return "login";
    }
}
