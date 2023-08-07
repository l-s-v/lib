package com.lsv.lib.spring.web.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Disables the favicon coming from the backend.
 *
 * @author Leandro da Silva Vieira
 */
@Hidden
@RestController
@RequestMapping("favicon.ico")
public class FaviconDisabledController {

    @GetMapping
    void returnNoFavicon() {
    }
}