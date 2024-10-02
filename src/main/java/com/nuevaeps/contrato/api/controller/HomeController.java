package com.nuevaeps.contrato.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";  // Retorna el nombre del archivo HTML (index.html) que está en /templates
    }
}