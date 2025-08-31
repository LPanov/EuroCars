package app.eurocars.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;

@Component
public class SessionCheckInterceptor implements HandlerInterceptor {

    private Set<String> UNAUTHENTICATED_ENDPOINTS = Set.of("/", "/login", "/register", "/forgot-password");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //EndPoint(ex: /home)
        String endPoint = request.getServletPath();
        if(UNAUTHENTICATED_ENDPOINTS.contains(endPoint)) {
            return true;
        }

        //Create new session if not exist
        //Add false to getSession and does not create session
        HttpSession currentUserSession = request.getSession(false);

        if (currentUserSession == null) {
            response.sendRedirect("/login");
            return false;
        }


        return true;
    }
}
