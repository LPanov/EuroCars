package app.eurocars.web;

import app.eurocars.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(EmailAlreadyExistException.class)
    public String handleEmailAlreadyExist(RedirectAttributes redirectAttributes, EmailAlreadyExistException exception) {
        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("EmailAlreadyExistMessage", message);
        return "redirect:/register";
    }

    @ExceptionHandler(NotMatchingPasswords.class)
    public String handleNotMatchingPasswords(RedirectAttributes redirectAttributes, NotMatchingPasswords exception) {
        String message = exception.getMessage();

        redirectAttributes.addFlashAttribute("notMatchingPasswords", message);
        return "redirect:/forgotten-password";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            AccessDeniedException.class,
            NoResourceFoundException.class,
            MethodArgumentTypeMismatchException.class,
            MissingRequestValueException.class,
            EngineNotFound.class,
            CategoryNotFound.class,
            ModelNotFound.class
    })
    public ModelAndView handleNotFoundExceptions(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("not-found");
        modelAndView.addObject("exception", exception.getClass().getSimpleName());

        return modelAndView;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("internal-server-error");
        modelAndView.addObject("exception", exception.getClass().getSimpleName());

        return modelAndView;
    }
}
