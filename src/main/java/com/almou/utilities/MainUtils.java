package com.almou.utilities;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MainUtils{
    public static void sendError(String message, int status, HttpServletResponse response) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(message);
    }
}
