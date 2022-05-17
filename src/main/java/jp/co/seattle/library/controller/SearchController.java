package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;


/**
 * 
 *　検索コントローラー
 *
 */
@Controller /** APIの入り口 */
public class SearchController {
	final static Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private BooksService booksService;
	/**
	 * 
	 * 貸出処理
	 * @title 書籍名
	 * @return
	 * 
	 */
	@RequestMapping(value = "/searchBook", method = RequestMethod.POST)
	public String searchBook(Locale locale,
			@RequestParam("title") String title,
			@RequestParam("radiobutton") String selectedButton,Model model) {
		booksService.searchBook(title,selectedButton);
		model.addAttribute("bookList",booksService.searchBook(title,selectedButton));
		return "home";
	}
	
	
}
