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

import jp.co.seattle.library.dto.LendBookInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentBooksService;

/**
 * 
 * 貸出コントローラー
 *
 */
@Controller /** APIの入り口 */
public class RentBookController {
	final static Logger logger = LoggerFactory.getLogger(RentBookController.class);
	
	
	@Autowired
	private RentBooksService rentBooksService;
	
	@Autowired
	private BooksService booksService;
	/**
	 * 
	 * 貸出処理
	 * 
	 */
	@RequestMapping(value = "/rentBook", method = RequestMethod.POST)
	public String rentBook(Locale locale,
			@RequestParam("bookId") int bookId,Model model) {
		logger.info("Welcome rentBooks.java! The client locale is {}.", locale);
		 
        LendBookInfo rentRecord=rentBooksService.getRentBookInfo(bookId);
        
//	//貸出テーブルに書籍が存在するか、
		if(rentRecord == null) {
			rentBooksService.insertRentBook(bookId);
		} else {
			if(rentRecord.getRentDate() == null) {
				rentBooksService.updateRentBook();
				model.addAttribute("lendBookInfo",rentBooksService.getRentBookInfo(bookId));
			} else {
			model.addAttribute("error", "貸出中です。");
		    }
		}
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}
}
