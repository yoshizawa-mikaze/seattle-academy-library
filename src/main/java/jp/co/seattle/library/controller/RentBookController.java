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

import jp.co.seattle.library.dto.BookDetailsInfo;
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
			@RequestParam("bookId") int bookId,
            Model model) {
		BookDetailsInfo bookInfo = new BookDetailsInfo();
		int rentedBookId = rentBooksService.getRentBookInfo(bookId);
		
	//貸出テーブルに書籍が存在するかチェック
		if(rentedBookId == 0) {
			rentBooksService.insertRentBook(bookId);
			System.out.println("b");
			
		} else {
			
			model.addAttribute("error", "貸出中です。");
			System.out.println("a");
		}
		
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}
}
