package jp.co.seattle.library.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.seattle.library.service.RentBooksService;

@Controller //APIの入り口
public class LendingHistoryController {
	final static Logger logger = LoggerFactory.getLogger(LendingHistoryController.class);
    
    @Autowired
    private RentBooksService rentBooksService;
    
    @RequestMapping(value = "/lending", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    public String lending(Model model) {
    	model.addAttribute("lendbookList", rentBooksService.getRentBookList());
        
    	return "lendHistory";
    
    }
}
