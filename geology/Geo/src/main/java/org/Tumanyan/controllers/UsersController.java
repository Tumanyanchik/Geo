package org.Tumanyan.controllers;

import org.Tumanyan.entity.File;
import org.Tumanyan.entity.User;
import org.Tumanyan.services.DiskService;
import org.Tumanyan.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


// Класс отвечает за действия доступные пользователю

@Controller
@RequestMapping("/user")
public class UsersController {

    private final String TOKEN = "AQAAAAAT3cmsAADLW6DgBVJwJ0e_rnH9sVXL5hM";
    private final UserService userService;


    @Autowired
    public UsersController(UserService userService, DiskService diskService){
        this.diskS
        this.userService =userService;
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.showRoles());
        return "user/login";
    }


    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.showRoles());
        return "user/registration";
    }


    @GetMapping("/adminMenu")
    public String adminMenu(Model model){
        model.addAttribute("fileList", userService.showAcceptFiles());
        model.addAttribute("processingList", userService.showProcessingFiles());
        return "user/adminMenu";
    }

    @GetMapping("/employeeMenu")
    public String employeeMenu(Model model){
        User user = (User) model.getAttribute("user");
        model.addAttribute("processingList", userService.showProcessingFiles());
        model.addAttribute("file",new File());
        return "user/employeeMenu";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(Model model,@ModelAttribute("file") @Valid File file, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            System.out.println("ошибки в форме");
            return "/user/employeeMenu";
        }

        if( userService.checkFileInDb(file) == true){
            String messageErrorAvailabilityFile = "Файл c таким № дела уже существует";
            model.addAttribute("messageErrorAvailabilityFile",messageErrorAvailabilityFile);
            return "/user/employeeMenu";
        }

        userService.uploadFile(file);
        return "redirect:/user/employeeMenu";
    }

    @PostMapping("/acceptFile")
    public String acceptFile(Model model,@ModelAttribute("file") File file){
        userService.updateStatus(file);
        return "redirect:/user/adminMenu";
    }


    @PostMapping("reg")
    public String  checkRegistration(Model model,@ModelAttribute("user") @Valid User user, BindingResult bindingResult){
        model.addAttribute("roles", userService.showRoles());
        if (bindingResult.hasErrors()) {
            return "/user/registration";
        }

        if( userService.checkUserInDb(user) == true){
            String messageErrorAvailabilityRegistr = "Пользователь с таким логином и должностью уже существует";
            model.addAttribute("messageErrorAvailabilityRegistr",messageErrorAvailabilityRegistr);
            return "/user/registration";
        }
        userService.save(user);
        // Если все данные верны, то
        return "redirect:/user/adminMenu";
    }

    @PostMapping("log")
    public String  checkLogin( Model model, @ModelAttribute("user") @Valid User user, BindingResult bindingResult){
        model.addAttribute("roles", userService.showRoles());

        //Обрабатываем исключение валидации для fio оно пустое, но это не ошибка
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("fio"))
                .collect(Collectors.toList());

        bindingResult = new BeanPropertyBindingResult(user,"userBinding");

        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            return "/user/login";
        }

        if( userService.checkUserInDb(user) == false){
            String messageErrorAvailability = "Пользователя с таким логином и должностью нет";
            model.addAttribute("messageErrorAvailability",messageErrorAvailability);
            return "/user/login";
        }

        if(userService.checkPassword(user) == false){
            String messageErrorPassword = "Пароль неверен";
            model.addAttribute("messageErrorPassword",messageErrorPassword);
            return "/user/login";
        }

        String role = user.getRole().getRoleName();
        if (role.equals("Администратор"))
            return "redirect:/user/adminMenu";
        if ((role.equals("Сотрудник лаборатории"))||(role.equals("Инженер-геолог")))
            return "redirect:/user/employeeMenu";


        // Если все данные верны, то
        //TODO переход в главное меню а не на регистрацию
        return "redirect:/user/registration";
    }

    //Загрузать файл на диск
    @PutMapping("/diskUp")
    public String diskUp(){
        String  blob = "";
        return blob;
    }


}
